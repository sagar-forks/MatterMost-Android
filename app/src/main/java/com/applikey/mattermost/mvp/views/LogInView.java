package com.applikey.mattermost.mvp.views;

import com.applikey.mattermost.models.team.Team;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Map;

@StateStrategyType(value = SkipStrategy.class)
public interface LogInView extends MvpView {

    void showLoading();

    void hideLoading();

    void onSuccessfulAuth();

    void onUnsuccessfulAuth(String message);

    void onTeamsRetrieved(Map<String, Team> teams);

    void onTeamsReceiveFailed(Throwable cause);

    void showPresetCredentials(String userName, String password);
}
