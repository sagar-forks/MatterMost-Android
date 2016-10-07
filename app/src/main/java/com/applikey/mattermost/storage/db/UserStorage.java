package com.applikey.mattermost.storage.db;

import com.applikey.mattermost.App;
import com.applikey.mattermost.models.user.User;
import com.applikey.mattermost.models.user.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;

public class UserStorage {

    @Inject
    Db mDb;

    public UserStorage() {
        App.getComponent().inject(this);
    }

    public void saveUsers(Map<String, User> directProfiles) {
        mDb.saveTransactionalWithRemoval(directProfiles.values());
    }

    public void saveStatuses(Map<String, String> userStatuses) {
        final List<UserStatus> result = new ArrayList<>();

        for (String id : userStatuses.keySet()) {
            final String status = userStatuses.get(id);

            result.add(new UserStatus(id, status));
        }

        mDb.saveTransactionalWithRemoval(result);
    }

    public Observable<List<User>> listDirectProfiles() {
        return mDb.listRealmObjects(User.class);
    }

    public Observable<List<UserStatus>> listStatuses() {
        return mDb.listRealmObjects(UserStatus.class);
    }
}