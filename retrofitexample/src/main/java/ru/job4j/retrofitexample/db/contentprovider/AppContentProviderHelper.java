package ru.job4j.retrofitexample.db.contentprovider;

import android.net.Uri;

import ru.job4j.retrofitexample.db.models.Comment;
import ru.job4j.retrofitexample.db.models.Post;

public class AppContentProviderHelper {
    public static class TableNames {
        public static final String POST_TABLE_NAME = Post.class.getSimpleName();
        public static final String COMMENT_TABLE_NAME = Comment.class.getSimpleName();
    }

    public static class Uris {
        public static final Uri URI_POST_TABLE = Uri.withAppendedPath(AppContentProviderData.CONTENT_URI, TableNames.POST_TABLE_NAME);
        public static final Uri URI_COMMENT_TABLE = Uri.withAppendedPath(AppContentProviderData.CONTENT_URI, TableNames.COMMENT_TABLE_NAME);
    }

    public static class UrisBuilder {
        public static final Uri.Builder POST_URI_BUILDER = Uris.URI_POST_TABLE.buildUpon();
        public static final Uri.Builder COMMENT_URI_BUILDER = Uris.URI_COMMENT_TABLE.buildUpon();
    }
}
