package ru.job4j.retrofitexample.db.mappers;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.job4j.retrofitexample.db.dbhelper.DBClass;

public abstract class BaseMapper<T> {
    private static final String TAG = BaseMapper.class.getSimpleName();

    private static final String STRING_TYPE = "java.lang.String";
    private static final String INTEGER_TYPE = "java.lang.Integer";
    private static final String LONG_TYPE = "java.lang.Long";

    public abstract ContentValues getContentValues(DBClass object);

    public abstract T getFromCursor(Cursor cursor);

    public List<T> getListFromCursor(Cursor cursor) {
        List<T> list = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            T obj = getFromCursor(cursor);
            list.add(obj);
        }
        Log.d(TAG, "getListFromCursor, size[" + cursor.getCount() + "]");
        return list;
    }

    public ContentValues[] getContentValuesList(List<T> objects) {
        ContentValues[] cv = new ContentValues[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            cv[i] = getContentValues((DBClass) objects.get(i));
        }
        Log.d(TAG, "getContentValuesList, size[" + objects.size() + "]");
        return cv;
    }

    Object getDataFromCursor(@NonNull Cursor cursor, @NonNull String column, @NonNull Class<?> columnClass) {
        int columnIndex = cursor.getColumnIndex(column);
        Object resObject = null;
        if (columnIndex != -1) {
            if (cursor.isNull(columnIndex)) {
                resObject = getDefaultValueForType(columnClass);
            } else {
                switch (columnClass.getName()) {
                    case STRING_TYPE:
                        resObject = cursor.getString(columnIndex);
                        break;
                    case INTEGER_TYPE:
                        resObject = cursor.getInt(columnIndex);
                        break;
                    case LONG_TYPE:
                        resObject = cursor.getLong(columnIndex);
                        break;
                }
            }
        }
        return resObject;
    }

    private Object getDefaultValueForType(@NonNull Class<?> columnClass) {
        switch (columnClass.getName()) {
            case LONG_TYPE:
                return 0L;
            case INTEGER_TYPE:
                return 0;
            default:
                return null;
        }
    }
}
