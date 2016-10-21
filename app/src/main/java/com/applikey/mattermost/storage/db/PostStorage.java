package com.applikey.mattermost.storage.db;

import com.applikey.mattermost.models.post.Post;

import java.util.List;

import rx.Observable;

public class PostStorage {

    private final Db mDb;

    public PostStorage(Db db) {
        mDb = db;
    }

    public void saveAllWithRemoval(List<Post> posts) {
        mDb.saveTransactionalWithRemoval(posts);
    }

    public void delete(Post post) {
        mDb.deleteTransactional(post);
    }

    public void update(Post post) {
        mDb.saveTransactional(post);
    }

    public void saveAll(List<Post> posts) {
        mDb.saveTransactional(posts);
    }

    public Observable<List<Post>> listByChannel(String channelId) {
        return mDb.listRealmObjectsFilteredSorted(Post.class, Post.FIELD_NAME_CHANNEL_ID,
                Post.FIELD_NAME_CHANNEL_CREATE_AT, channelId);
    }
}