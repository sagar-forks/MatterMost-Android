package com.applikey.mattermost.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.applikey.mattermost.R;
import com.applikey.mattermost.adapters.NotJoinedChannelsAdapter;
import com.applikey.mattermost.models.channel.Channel;
import com.applikey.mattermost.mvp.presenters.FindMoreChannelsPresenter;
import com.applikey.mattermost.mvp.views.FindMoreChannelsView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindMoreChannelsActivity extends BaseMvpActivity
        implements FindMoreChannelsView, NotJoinedChannelsAdapter.OnNotJoinedChannelClickListener {

    @BindView(R.id.rv_more_channels)
    RecyclerView mRvNotJoinedChannels;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_empty_state)
    TextView mTvEmptyState;

    @BindView(R.id.refresh_find_more_channel)
    SwipeRefreshLayout mRefreshFindMoreChannels;

    @InjectPresenter
    FindMoreChannelsPresenter mPresenter;

    private NotJoinedChannelsAdapter mAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, FindMoreChannelsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_more_channels);
        ButterKnife.bind(this);
        initView();
        initToolbar();
    }

    @Override
    public void showNotJoinedChannels(List<Channel> notJoinedChannels) {
        mAdapter.setChannels(notJoinedChannels);
    }

    @Override
    public void onChannelClick(Channel channel) {
        startActivity(ChatActivity.getIntent(this, channel));
    }

    @Override
    public void showEmptyState() {
        mTvEmptyState.setVisibility(View.VISIBLE);
        mRvNotJoinedChannels.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyState() {
        mTvEmptyState.setVisibility(View.GONE);
        mRvNotJoinedChannels.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        if (!mRefreshFindMoreChannels.isRefreshing()) {
            mRefreshFindMoreChannels.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        mRefreshFindMoreChannels.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        mAdapter = new NotJoinedChannelsAdapter(this);
        mRvNotJoinedChannels.setAdapter(mAdapter);
        mRefreshFindMoreChannels.setOnRefreshListener(mPresenter::requestNotJoinedChannels);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.find_more_channels);
    }

}
