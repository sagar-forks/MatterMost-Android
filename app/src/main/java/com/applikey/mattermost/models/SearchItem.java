package com.applikey.mattermost.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface SearchItem {

    int USER = 0;
    int CHANNEL = 1;
    @Retention(SOURCE)
    @IntDef({CHANNEL, USER})
    @interface Type {

    }

    @Type
    int getSearchType();

}
