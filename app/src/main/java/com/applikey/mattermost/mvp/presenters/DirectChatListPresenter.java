package com.applikey.mattermost.mvp.presenters;

import com.applikey.mattermost.App;
import com.applikey.mattermost.models.channel.Channel;
import com.applikey.mattermost.storage.db.UserStorage;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.Observable;

@InjectViewState
public class DirectChatListPresenter extends BaseChatListPresenter {

    @Inject
    UserStorage mUserStorage;

    public DirectChatListPresenter() {
        super();
        App.getUserComponent().inject(this);
    }

    @Override
    public Observable<RealmResults<Channel>> getInitData() {
        return mChannelStorage.listDirect();
    }
}