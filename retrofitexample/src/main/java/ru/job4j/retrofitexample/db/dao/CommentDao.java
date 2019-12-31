package ru.job4j.retrofitexample.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.job4j.retrofitexample.db.AppContentResolverWrapper;
import ru.job4j.retrofitexample.db.contentprovider.AppContentProviderHelper;
import ru.job4j.retrofitexample.db.mappers.CommentMapper;
import ru.job4j.retrofitexample.db.models.Comment;

public class CommentDao {
    private final AppContentResolverWrapper appContentResolverWrapper;
    private final CommentMapper mapper;

    public CommentDao(AppContentResolverWrapper appContentResolverWrapper) {
        this.appContentResolverWrapper = appContentResolverWrapper;
        this.mapper = new CommentMapper();
    }

    private List<Comment> getComments(String id) {
        Cursor cursor = appContentResolverWrapper._query(AppContentProviderHelper.Uris.URI_COMMENT_TABLE, null, "post_id = ?", new String[]{id}, null);
        List<Comment> comments = new ArrayList<>();
        if (cursor != null) {
            comments.addAll(mapper.getListFromCursor(cursor));
            cursor.close();
        }
        return comments;
    }

    private int create(Comment comment) {
        Uri newUri = AppContentProviderHelper.Uris.URI_COMMENT_TABLE;
        ContentValues cv = mapper.getContentValues(comment);
        Uri uri = appContentResolverWrapper._insert(newUri, cv);
        int localIndexId = 0;
        if (uri != null) {
            localIndexId = Integer.parseInt(uri.getLastPathSegment());
        }
        return localIndexId;
    }

    private int update(Comment comment) {
        Uri newUri = AppContentProviderHelper.Uris.URI_COMMENT_TABLE;
        ContentValues cv = mapper.getContentValues(comment);
        return appContentResolverWrapper._update(newUri, cv, null, null);
    }

    private int delete(String id) {
        return appContentResolverWrapper._delete(AppContentProviderHelper.Uris.URI_COMMENT_TABLE, "_id = '" + id + "'", null);
    }

    private int bulkInsert(List<Comment> comments) {
        return appContentResolverWrapper._bulkInsert(AppContentProviderHelper.Uris.URI_COMMENT_TABLE, mapper.getContentValuesList(comments));
    }


    public Single<List<Comment>> getCommentsByPostId(String id) {
        return Single.create(emitter -> emitter.onSuccess(getComments(id)));
    }

    public Single<Integer> createComment(Comment comment) {
        return Single.create(emitter -> emitter.onSuccess(create(comment)));
    }

    public Single<Integer> updateComment(Comment comment) {
        return Single.create(emitter -> emitter.onSuccess(update(comment)));
    }

    public Single<Integer> deleteComment(String id) {
        return Single.create(emitter -> emitter.onSuccess(delete(id)));
    }

    public Single<Integer> insertList(List<Comment> comments) {
        return Single.create(emitter -> emitter.onSuccess(bulkInsert(comments)));
    }

}
