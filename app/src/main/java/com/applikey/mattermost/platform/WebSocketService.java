package com.applikey.mattermost.platform;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.applikey.mattermost.App;
import com.applikey.mattermost.models.post.Post;
import com.applikey.mattermost.models.socket.MessagePostedEventData;
import com.applikey.mattermost.models.socket.Props;
import com.applikey.mattermost.models.socket.WebSocketEvent;
import com.applikey.mattermost.storage.db.ChannelStorage;
import com.applikey.mattermost.storage.db.PostStorage;
import com.applikey.mattermost.storage.preferences.Prefs;
import com.applikey.mattermost.web.BearerTokenFactory;
import com.applikey.mattermost.web.ErrorHandler;
import com.applikey.mattermost.web.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public class WebSocketService extends Service {

    private static final String TAG = WebSocketService.class.getSimpleName();

    private static final String EVENT_STATUS_CHANGE = "status_change";
    private static final String EVENT_TYPING = "typing";
    private static final String EVENT_MESSAGE_POSTED = "posted";


    @Inject
    PostStorage mPostStorage;

    @Inject
    ChannelStorage mChannelStorage;

    @Inject
    ErrorHandler mErrorHandler;

    @Inject
    Socket mMessagingSocket;

    private Handler mHandler;

    public static Intent getIntent(Context context) {
        return new Intent(context, WebSocketService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.getUserComponent().inject(this);

        mHandler = new Handler(Looper.getMainLooper());

        openSocket();
    }

    private void openSocket() {
        mMessagingSocket.listen()
                .retryWhen(mErrorHandler::tryReconnectSocket)
                .observeOn(Schedulers.computation())
                .subscribe(this::handleSocketEvent, mErrorHandler::handleError);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        closeSocket();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void closeSocket() {
        if (mMessagingSocket.isOpen()) {
            mMessagingSocket.close();
        }
    }

    private void handleSocketEvent(WebSocketEvent event) {
        String eventType = event.getEvent();
        // Mattermost Old API fix
        if (eventType == null) {
            eventType = event.getAction();
        }

        Log.d(TAG, "Got event: " + eventType);

        switch (eventType) {
            case EVENT_MESSAGE_POSTED: {
                final Post post = extractPostFromSocket(event);
                Log.d(TAG, "Post message: " + post.getMessage());
                mHandler.post(() -> {
                    mPostStorage.save(post);
                    mChannelStorage.findByIdAndCopy(post.getChannelId())
                            .first()
                            .doOnNext(channel -> channel.setLastPost(post))
                            .subscribe(mChannelStorage::updateLastPost, mErrorHandler::handleError);
                });
            }
        }
    }

    private Post extractPostFromSocket(WebSocketEvent event) {
        final Gson gson = GsonFactory.INSTANCE.getGson();
        final JsonObject eventData = event.getData();
        final String postObject;
        if (eventData != null) {
            final MessagePostedEventData data = gson.fromJson(eventData, MessagePostedEventData.class);
            postObject = data.getPostObject();
        } else {
            final JsonObject eventProps = event.getProps();
            final Props props = gson.fromJson(eventProps, Props.class);
            postObject = props.getPost();
        }
        return gson.fromJson(postObject, Post.class);
    }
}
