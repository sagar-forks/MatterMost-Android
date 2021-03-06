package com.applikey.mattermost.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.applikey.mattermost.R;
import com.applikey.mattermost.adapters.TeamListAdapter;
import com.applikey.mattermost.models.team.Team;
import com.applikey.mattermost.mvp.presenters.ChooseTeamPresenter;
import com.applikey.mattermost.mvp.views.ChooseTeamView;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseTeamActivity extends BaseMvpActivity implements ChooseTeamView {

    @BindView(R.id.content)
    LinearLayout mLlContent;

    @BindView(R.id.rv_teams)
    RecyclerView mRvTeams;

    @InjectPresenter
    ChooseTeamPresenter mPresenter;

    public static Intent getIntent(Context context) {
        return new Intent(context, ChooseTeamActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);

        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.getInitialData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
    }

    @Override
    public void displayTeams(List<Team> teams) {
        final TeamListAdapter adapter = new TeamListAdapter(teams);
        adapter.setItemClickAdapter(mTeamClickListener);

        mRvTeams.setLayoutManager(new LinearLayoutManager(this));
        mRvTeams.setAdapter(adapter);
    }

    @Override
    public void onTeamChosen() {
        hideLoadingDialog();
        startActivity(ChatListActivity.getIntent(this));
    }

    @Override
    public void onFailure(String message) {
        final String resultMessage = getResources().getString(R.string.could_not_receive_teams)
                + ": " + message;
        Snackbar.make(mLlContent, resultMessage + message,
                Snackbar.LENGTH_INDEFINITE).show();
    }

    @OnClick(R.id.back)
    void onBack() {
        finish();
    }

    private final TeamListAdapter.TeamClickListener mTeamClickListener = team -> {
        showLoadingDialog();
        mPresenter.chooseTeam(team);
    };
}
