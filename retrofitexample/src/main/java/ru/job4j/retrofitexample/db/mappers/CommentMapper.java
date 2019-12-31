package ru.job4j.retrofitexample.db.mappers;

import android.content.ContentValues;
import android.database.Cursor;

import ru.job4j.retrofitexample.db.dbhelper.DBClass;
import ru.job4j.retrofitexample.db.models.Comment;

public class CommentMapper extends BaseMapper<Comment> {
    @Override
    public ContentValues getContentValues(DBClass object) {
        ContentValues cv = new ContentValues();
        if (object instanceof Comment) {
            Comment comment = (Comment) object;
            if (comment.get_id() != null) {
                cv.put("_id", comment.get_id());
            }
            if (comment.getPost_id() != null) {
                cv.put("post_id", comment.getPost_id());
            }
            if (comment.getName() != null) {
                cv.put("name", comment.getName());
            }
            if (comment.getEmail() != null) {
                cv.put("email", comment.getEmail());
            }
            if (comment.getText() != null) {
                cv.put("text", comment.getText());
            }
        }
        return cv;
    }

    @Override
    public Comment getFromCursor(Cursor cursor) {
        Comment comment = new Comment();
        comment.set_id((Integer) getDataFromCursor(cursor, "_id", Integer.class));
        comment.setPost_id((Integer) getDataFromCursor(cursor, "post_id", Integer.class));
        comment.setName((String) getDataFromCursor(cursor, "name", String.class));
        comment.setEmail((String) getDataFromCursor(cursor, "email", String.class));
        comment.setText((String) getDataFromCursor(cursor, "text", String.class));
        return comment;
    }
}
