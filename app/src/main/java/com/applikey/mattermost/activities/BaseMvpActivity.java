package com.applikey.mattermost.activities;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;

/**
 * This class is taken from Moxy samples, and extended from {@link BaseActivity}.
 */
public abstract class BaseMvpActivity extends BaseActivity {

    private MvpDelegate<? extends BaseMvpActivity> mMvpDelegate;

    private MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }
        return mMvpDelegate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getMvpDelegate().onAttach();
    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroy() {
        if (isFinishing()) {
            getMvpDelegate().onDestroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getMvpDelegate().onSaveInstanceState(outState);
    }
}
