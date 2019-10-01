package ru.job4j.workersstore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, int version) {
        super(context, "workers", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table profession(" +
                "_id integer primary key autoincrement, "+
                "name text, " +
                "code integer)"
        );
        db.execSQL("create table worker(" +
                "_id integer primary key autoincrement, " +
                "first_name text, " +
                "last_name text, " +
                "date_of_birth long, " +
                "link text, " +
                "prof_id integer references profession(_id) on update cascade on delete restrict)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
