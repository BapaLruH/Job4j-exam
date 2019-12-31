package ru.job4j.retrofitexample.db.mappers;

import android.content.ContentValues;
import android.database.Cursor;

import ru.job4j.retrofitexample.db.dbhelper.DBClass;
import ru.job4j.retrofitexample.db.models.Post;

public class PostMapper extends BaseMapper<Post> {
    @Override
    public ContentValues getContentValues(DBClass object) {
        ContentValues cv = new ContentValues();
        if (object instanceof Post) {
            Post post = (Post) object;
            if (post.getUser_id() != null) {
                cv.put("user_id", post.getUser_id());
            }
            if (post.get_id() != null) {
                cv.put("_id", post.get_id());
            }
            if (post.getTitle() != null) {
                cv.put("title", post.getTitle());
            }
            if (post.getText() != null) {
                cv.put("text", post.getText());
            }
        }
        return cv;
    }

    @Override
    public Post getFromCursor(Cursor cursor) {
        Post post = new Post();
        post.setUser_id((Integer) getDataFromCursor(cursor, "user_id", Integer.class));
        post.set_id((Integer) getDataFromCursor(cursor, "_id", Integer.class));
        post.setTitle((String) getDataFromCursor(cursor, "title", String.class));
        post.setText((String) getDataFromCursor(cursor, "text", String.class));
        return post;
    }
}
