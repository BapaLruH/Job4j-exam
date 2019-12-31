package ru.job4j.retrofitexample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ru.job4j.retrofitexample.db.contentprovider.DBHelper;
import ru.job4j.retrofitexample.db.dao.CommentDao;
import ru.job4j.retrofitexample.db.dao.PostDao;

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();

    private PostDao postDao;
    private CommentDao commentDao;

    private static DBManager INSTANCE;

    public static DBManager init(AppContentResolverWrapper contentResolverWrapper) {
        DBManager localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (DBManager.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new DBManager(contentResolverWrapper);
                }
            }
        }
        return localInstance;
    }

    public static DBManager getInstance() {
        return INSTANCE;
    }

    private DBManager(AppContentResolverWrapper contentResolverWrapper) {
       postDao = new PostDao(contentResolverWrapper);
       commentDao = new CommentDao(contentResolverWrapper);
    }

    public SQLiteDatabase getCurrentDatabase(Context context) {
        return new DBHelper(context, "db_name").getWritableDatabase();
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }
}
