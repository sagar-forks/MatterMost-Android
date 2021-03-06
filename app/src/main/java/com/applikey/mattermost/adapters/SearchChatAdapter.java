package com.applikey.mattermost.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.applikey.mattermost.Constants;
import com.applikey.mattermost.fragments.SearchAllFragment;
import com.applikey.mattermost.fragments.SearchChannelFragment;
import com.applikey.mattermost.fragments.SearchMessageFragment;
import com.applikey.mattermost.fragments.SearchUserFragment;

public class SearchChatAdapter extends FragmentPagerAdapter {

    private final static int PAGE_NUMBER = 4;

    public SearchChatAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // Indexes should equal to tab behavior indexes
        switch (position) {
            case 0: {
                return SearchAllFragment.newInstance();
            }
            case 1: {
                return SearchMessageFragment.newInstance();
            }
            case 2: {
                return SearchChannelFragment.newInstance();
            }
            case 3: {
                return SearchUserFragment.newInstance();
            }
            default: {
                return SearchUserFragment.newInstance();
            }
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.EMPTY_STRING;
    }
}
