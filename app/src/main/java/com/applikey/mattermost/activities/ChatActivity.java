package com.applikey.mattermost.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.applikey.mattermost.R;
import com.applikey.mattermost.models.channel.Channel;
import com.applikey.mattermost.models.post.Post;
import com.applikey.mattermost.mvp.presenters.ChatPresenter;
import com.applikey.mattermost.mvp.views.ChatView;
import com.applikey.mattermost.web.ErrorHandler;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChatActivity extends BaseMvpActivity implements ChatView {

    private static final String CHANNEL_ID_KEY = "channel-id";
    private static final String CHANNEL_NAME_KEY = "channel-name";
    private static final String CHANNEL_TYPE_KEY = "channel-type";

    private static final String CHANNEL_PREFIX = "#";
    private static final String DIRECT_PREFIX = "";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tv_empty_state)
    TextView mTvEmptyState;

    @Bind(R.id.rv_messages)
    RecyclerView mRvMessages;

    @InjectPresenter
    ChatPresenter mPresenter;

    private String mChannelId;
    private String mChannelName;
    private String mChannelType;

    public static Intent getIntent(Context context, Channel channel) {
        final Intent intent = new Intent(context, ChatActivity.class);

        final Bundle bundle = new Bundle();
        bundle.putString(CHANNEL_ID_KEY, channel.getId());
        bundle.putString(CHANNEL_NAME_KEY, channel.getDisplayName());
        bundle.putString(CHANNEL_TYPE_KEY, channel.getType());

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        initParameters();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter.getInitialData(mChannelId);
    }

    private void initParameters() {
        final Bundle extras = getIntent().getExtras();
        mChannelId = extras.getString(CHANNEL_ID_KEY);
        mChannelName = extras.getString(CHANNEL_NAME_KEY);
        mChannelType = extras.getString(CHANNEL_TYPE_KEY);
    }

    private void initView() {
        setSupportActionBar(mToolbar);

        final String prefix = !mChannelType.equals(Channel.ChannelType.DIRECT.getRepresentation())
                ? CHANNEL_PREFIX : DIRECT_PREFIX;

        mToolbar.setTitle(prefix + mChannelName);
    }

    @Override
    public void displayData(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            displayEmptyState();
            return;
        }

        hideEmptyState();
    }

    @Override
    public void onFailure(Throwable cause) {
        ErrorHandler.handleError(cause);
    }

    private void displayEmptyState() {
        mRvMessages.setVisibility(GONE);
        mTvEmptyState.setVisibility(VISIBLE);
    }

    private void hideEmptyState() {
        mTvEmptyState.setVisibility(GONE);
        mRvMessages.setVisibility(VISIBLE);
    }
}
