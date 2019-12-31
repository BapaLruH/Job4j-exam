package ru.job4j.retrofitexample.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.Log;

public class AppContentResolverWrapper {
    private static final String TAG = AppContentResolverWrapper.class.getSimpleName();
    private static ContentResolver contentResolver;

    public AppContentResolverWrapper(Context context) {
        contentResolver = context.getContentResolver();
    }

    public final @Nullable
    Cursor _query(@RequiresPermission.Read @NonNull Uri uri,
                  @Nullable String[] projection, @Nullable String selection,
                  @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            return contentResolver.query(uri,projection,selection,selectionArgs,sortOrder);
        }  catch (IllegalStateException | IllegalArgumentException | SQLiteException ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return null;
    }

    public final int _update(@RequiresPermission.Write @NonNull Uri uri,
                             @Nullable ContentValues values, @Nullable String where,
                             @Nullable String[] selectionArgs) {
        try {
            return contentResolver.update(uri,values,where,selectionArgs);
        }  catch (IllegalStateException | IllegalArgumentException | SQLiteException ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return 0;
    }

    public final @Nullable Uri _insert(@RequiresPermission.Write @NonNull Uri url,
                                       @Nullable ContentValues values) {
        try {
            return contentResolver.insert(url,values);
        }  catch (IllegalStateException | IllegalArgumentException | SQLiteException  ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return null;
    }

    public final int _bulkInsert(@RequiresPermission.Write @NonNull Uri url,
                                 @NonNull ContentValues[] values) {
        try {
            return contentResolver.bulkInsert(url,values);
        }  catch (IllegalStateException | IllegalArgumentException | SQLiteException ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return 0;
    }

    public final int _delete(@RequiresPermission.Write @NonNull Uri url, @Nullable String where,
                             @Nullable String[] selectionArgs) {
        try {
            return contentResolver.delete(url,where,selectionArgs);
        }  catch (IllegalStateException | IllegalArgumentException | SQLiteException  ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return 0;
    }
}
