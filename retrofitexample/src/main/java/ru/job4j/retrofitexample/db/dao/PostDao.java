package ru.job4j.retrofitexample.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.job4j.retrofitexample.db.AppContentResolverWrapper;
import ru.job4j.retrofitexample.db.contentprovider.AppContentProviderHelper;
import ru.job4j.retrofitexample.db.mappers.PostMapper;
import ru.job4j.retrofitexample.db.models.Post;

public class PostDao {
    private final AppContentResolverWrapper appContentResolverWrapper;
    private final PostMapper mapper;

    public PostDao(AppContentResolverWrapper appContentResolverWrapper) {
        this.appContentResolverWrapper = appContentResolverWrapper;
        this.mapper = new PostMapper();
    }

    private List<Post> getPosts() {
        Cursor cursor = appContentResolverWrapper._query(AppContentProviderHelper.Uris.URI_POST_TABLE, null, null, null, null);
        List<Post> posts = new ArrayList<>();
        if (cursor != null) {
            posts.addAll(mapper.getListFromCursor(cursor));
            cursor.close();
        }
        return posts;
    }

    private List<Post> getPost(String id) {
        Cursor cursor = appContentResolverWrapper._query(AppContentProviderHelper.Uris.URI_POST_TABLE, null, "_id = ?", new String[]{id}, null);
        List<Post> postList = new ArrayList<>();
        if (cursor != null && cursor.moveToNext()) {
            postList.add(mapper.getFromCursor(cursor));
        }
        if (cursor != null && cursor.isClosed()) {
            cursor.close();
        }
        return postList;
    }

    private int create(Post post) {
        Uri newUri = AppContentProviderHelper.Uris.URI_POST_TABLE;
        ContentValues cv = mapper.getContentValues(post);
        Uri uri = appContentResolverWrapper._insert(newUri, cv);
        int localIndexId = 0;
        if (uri != null) {
            localIndexId = Integer.parseInt(uri.getLastPathSegment());
        }
        return localIndexId;
    }

    private int update(Post post) {
        Uri newUri = AppContentProviderHelper.Uris.URI_POST_TABLE;
        ContentValues cv = mapper.getContentValues(post);
        return appContentResolverWrapper._update(newUri, cv, null, null);
    }

    private int delete(String id) {
        return appContentResolverWrapper._delete(AppContentProviderHelper.Uris.URI_POST_TABLE, "_id = '" + id + "'", null);
    }

    private int bulInsert(List<Post> posts) {
        return appContentResolverWrapper._bulkInsert(AppContentProviderHelper.Uris.URI_POST_TABLE, mapper.getContentValuesList(posts));
    }

    public Single<List<Post>> getPostById(String id) {
        return Single.create(emitter -> emitter.onSuccess(getPost(id)));
    }

    public Single<List<Post>> getAllPosts() {
        return Single.create(emitter -> emitter.onSuccess(getPosts()));
    }

    public Single<Integer> deletePost(String id) {
        return Single.create(emitter -> emitter.onSuccess(delete(id)));
    }

    public Single<Integer> updatePost(Post post) {
        return Single.create(emitter -> emitter.onSuccess(update(post)));
    }

    public Single<Integer> createPost(Post post) {
        return Single.create(emitter -> emitter.onSuccess(create(post)));
    }

    public Single<Integer> updateList(List<Post> posts) {
        return Single.create(emitter -> emitter.onSuccess(bulInsert(posts)));
    }
}
