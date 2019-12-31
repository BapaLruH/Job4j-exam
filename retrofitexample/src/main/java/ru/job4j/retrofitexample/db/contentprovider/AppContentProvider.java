package ru.job4j.retrofitexample.db.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;

import ru.job4j.retrofitexample.db.DBManager;

public class AppContentProvider extends ContentProvider {

    private static final String TAG = AppContentProvider.class.getSimpleName();
    public static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(0);
        mUriMatcher.addURI(AppContentProviderData.CONTENT_PROVIDER_AUTHORITY_NAME, AppContentProviderHelper.TableNames.POST_TABLE_NAME, AppContentProviderData.ID_EMPLOYEE_TABLE);
        mUriMatcher.addURI(AppContentProviderData.CONTENT_PROVIDER_AUTHORITY_NAME, AppContentProviderHelper.TableNames.COMMENT_TABLE_NAME, AppContentProviderData.ID_SPECIALTY_TABLE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;
        switch (mUriMatcher.match(uri)) {
            case AppContentProviderData.ID_EMPLOYEE_TABLE:
                type = AppContentProviderHelper.TableNames.POST_TABLE_NAME;
                break;
            case AppContentProviderData.ID_SPECIALTY_TABLE:
                type = AppContentProviderHelper.TableNames.COMMENT_TABLE_NAME;
                break;
        }
        return type;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri newUri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor resultCursor = null;
        String groupBy = getGroupByColumnsFromUri(newUri);
        Uri uri = newUri.buildUpon().clearQuery().build();
        SQLiteDatabase db = DBManager.getInstance().getCurrentDatabase(getContext());
        if (db != null) {
            try {
                db.beginTransaction();
                resultCursor = db.query(getType(uri), projection, selection, selectionArgs, groupBy, null, sortOrder);
                db.setTransactionSuccessful();
            } catch (IllegalArgumentException | IllegalStateException | SQLiteException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            } finally {
                db.endTransaction();
            }
        }
        return resultCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri newUri, @Nullable ContentValues values) {
        Uri resultUri = null;
        Uri uri = newUri.buildUpon().clearQuery().build();
        SQLiteDatabase db = DBManager.getInstance().getCurrentDatabase(getContext());
        if (db != null) {
            long result = 0;
            try {
                db.beginTransaction();
                result = db.insertWithOnConflict(getType(uri), null, values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
            } catch (SQLiteConstraintException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                update(newUri, values, null, null);
            } catch (IllegalArgumentException | SQLiteException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            } finally {
                db.endTransaction();
            }

            if (result != -1) {
                resultUri = Uri.withAppendedPath(uri, result + "");
            }
        }
        //TODO notifyChange????
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri newUri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = 0;
        Uri uri = newUri.buildUpon().clearQuery().build();
        SQLiteDatabase db = DBManager.getInstance().getCurrentDatabase(getContext());
        if (db != null) {
            db.beginTransaction();
            try {
                result = db.delete(getType(uri), selection, selectionArgs);
                Log.d(TAG, "table[" + getType(newUri) + "], deleted row count[" + result + "]");
                db.setTransactionSuccessful();
            } catch (IllegalArgumentException | IllegalStateException | SQLiteException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            } finally {
                db.endTransaction();
            }
        }
        //TODO notifyChange????
        return result;
    }

    @Override
    public int update(@NonNull Uri newUri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = -1;
        Uri uri = newUri.buildUpon().clearQuery().build();
        SQLiteDatabase db = DBManager.getInstance().getCurrentDatabase(getContext());
        String table = getType(uri);
        if (db != null) {
            try {
                db.beginTransaction();
                if (selection == null && values.get("_id") != null) {
                    selection = "_id=?";
                    selectionArgs = new String[]{values.get("_id").toString()};
                    Cursor c = db.query(table, new String[]{"_id"}, selection, selectionArgs, null, null, null, "1");
                    if (c != null && !c.isClosed()) {
                        if (c.getCount() > 0) {
                            result = db.update(table, values, selection, selectionArgs);
                        } else {
                            result = (int) db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                        }
                    }
                    // close cursor here
                    if (c != null && !c.isClosed()) {
                        c.close();
                    }
                } else if ("*".equals(selection)) {
                    result = db.update(table, values, null, null);
                } else if (selection != null) {
                    result = db.update(table, values, selection, selectionArgs);
                } else {
                    db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                }

                db.setTransactionSuccessful();
            } catch (IllegalArgumentException | IllegalStateException | SQLiteException ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            } finally {
                db.endTransaction();
            }
            //TODO notifyChange????
        }
        return result;
    }

    @Override
    public int bulkInsert(@NonNull Uri newUri, @NonNull ContentValues[] values) {
        int result = 0;
        Uri uri = newUri.buildUpon().clearQuery().build();
        SQLiteDatabase db = DBManager.getInstance().getCurrentDatabase(getContext());
        String table = getType(uri);
        try {
            db.beginTransaction();
            ContentValues[][] arraysValues = splitArray(values, 20);
            for (ContentValues[] arraysValue : arraysValues)
                for (ContentValues value : arraysValue) {
                    long id = db.insertWithOnConflict(table, null, value, SQLiteDatabase.CONFLICT_IGNORE);
                    if (id > 0) {
                        result++;
                    } else {
                        db.updateWithOnConflict(table, value, null, null, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }
            db.setTransactionSuccessful();
        } catch (IllegalArgumentException | IllegalStateException | SQLiteException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            db.endTransaction();
        }
        //TODO notifyChange????
        return result;
    }

    private String getGroupByColumnsFromUri(Uri uri) {
        return uri.getQueryParameter(AppContentProviderData.PARAM_GROUP_BY);
    }

    public ContentValues[][] splitArray(ContentValues[] arrayToSplit, int chunkSize) {
        if (chunkSize <= 0) {
            return null;
        }
        int rest = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
        ContentValues[][] arrays = new ContentValues[chunks][];
        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0) {
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays;
    }
}
