package com.applikey.mattermost.mvp.views;

import com.applikey.mattermost.models.channel.Channel;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = SingleStateStrategy.class)
public interface ChatListScreenView extends MvpView {
    void setToolbarTitle(String title);

    @StateStrategyType(value = SkipStrategy.class)
    void onChannelLoaded(Channel channel);
}
