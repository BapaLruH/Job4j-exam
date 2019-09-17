package ru.job4j.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoStoreImpl implements ToDoStore {
    private static DBHelper dbHelper;
    private static ToDoStoreImpl instance;

    private ToDoStoreImpl() {
    }

    private ToDoStoreImpl(Context context, int version) {
        dbHelper = new DBHelper(context, version);
    }

    public static ToDoStoreImpl getInstance(Context context, int version) {
        ToDoStoreImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (ToDoStoreImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ToDoStoreImpl(context, version);
                }
            }
        }
        return localInstance;
    }

    @Override
    public Task create(Task task) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues cv = getContentValues(task);
        long id = database.insert("tasks", null, cv);
        task.setId((int) id);
        database.close();
        return task;
    }

    @Override
    public int update(int id, Task task) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues cv = getContentValues(task);
        int updatedCount = database.update("tasks", cv, "_id = ?", new String[]{String.valueOf(task.getId())});
        database.close();
        return updatedCount;
    }

    private ContentValues getContentValues(Task task) {
        ContentValues cv = new ContentValues();
        cv.put("name", task.getName());
        cv.put("description", task.getDescription());
        cv.put("created", task.getCreated() == 0 ? new Date().getTime() : task.getCreated());
        cv.put("closed", task.getClosed());
        return cv;
    }

    @Override
    public int delete(int id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int deletedRows = database.delete("tasks", "_id = ?", new String[]{String.valueOf(id)});
        database.close();
        return deletedRows;
    }

    @Override
    public Task findTaskById(int id) {
        List<Task> tasks = findTasksByCondition("name = ?", new String[]{String.valueOf(id)});
        return tasks.size() > 0 ? tasks.get(0) : null;
    }

    @Override
    public List<Task> findAll() {
        return findTasksByCondition(null, null);
    }

    @Override
    public List<Task> findTaskByName(String name) {
        return findTasksByCondition("name = ?", new String[]{name});
    }

    private List<Task> findTasksByCondition(String condition, String[] args) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query("tasks", null, condition, args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                long created = cursor.getLong(cursor.getColumnIndex("created"));
                long closed = cursor.getLong(cursor.getColumnIndex("closed"));
                tasks.add(new Task(
                        id, name, description, created, closed
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return tasks;
    }
}
