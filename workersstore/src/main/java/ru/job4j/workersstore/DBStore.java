package ru.job4j.workersstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DBStore implements Store {
    private static DBHelper dbHelper;
    private static DBStore instance;
    private static final String LOG_TAG = "WSTag";

    private DBStore() {
    }

    private DBStore(Context context, int version) {
        dbHelper = new DBHelper(context, version);
    }

    public static DBStore getInstance(Context context, int version) {
        DBStore localInstance = instance;
        if (localInstance == null) {
            synchronized (DBStore.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = instance = new DBStore(context, version);
                }
            }
        }
        return localInstance;
    }

    @Override
    public Profession createProfession(Profession profession) {
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            ContentValues cv = getContentValueProf(profession);
            long id = database.insert("profession", null, cv);
            profession.setId((int) id);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return profession;
    }

    private ContentValues getContentValueProf(Profession profession) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", profession.getName());
        contentValues.put("code", profession.getSpecialtyCode());
        return contentValues;
    }

    @Override
    public Worker createWorker(Worker worker) {
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            ContentValues cv = getContentValueWorker(worker);
            long id = database.insert("worker", null, cv);
            worker.setId((int) id);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return worker;
    }

    private ContentValues getContentValueWorker(Worker worker) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", worker.getFirstName());
        contentValues.put("last_name", worker.getLastName());
        contentValues.put("date_of_birth", worker.getDateOfBirth());
        contentValues.put("link", worker.getLinkToPhoto());
        contentValues.put("prof_id", worker.getProfession().getId());
        return contentValues;
    }

    @Override
    public Profession updateProfession(int id, Profession profession) {
        int updatedCount = 0;
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            ContentValues cv = getContentValueProf(profession);
            updatedCount = database.update("profession", cv, "_id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return updatedCount > 0 ? profession : null;
    }

    @Override
    public Worker updateWorker(int id, Worker worker) {
        int updatedCount = 0;
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            ContentValues cv = getContentValueWorker(worker);
            updatedCount = database.update("worker", cv, "_id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return updatedCount > 0 ? worker : null;
    }

    @Override
    public boolean deleteProfession(int id) {
        boolean rsl = false;
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            database.execSQL("pragma foreign_keys = on;");
            int deletedRows = database.delete("profession", "_id = ?", new String[]{String.valueOf(id)});
            rsl = deletedRows > 0;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return rsl;
    }

    @Override
    public boolean deleteWorker(int id) {
        int deletedRows = 0;
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            deletedRows = database.delete("worker", "_id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return deletedRows > 0;
    }

    @Override
    public List<Profession> findAllProfessions() {
        return findProfessionsByCondition(null, null);
    }

    private List<Profession> findProfessionsByCondition(String condition, String[] args) {
        List<Profession> professionList = new ArrayList<>();
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = findByCondition(database, "profession", new String[]{"_id", "name", "code"}, condition, args, "_id")
        ) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int specialtyCode = cursor.getInt(cursor.getColumnIndex("code"));
                    professionList.add(
                            new Profession(id, name, specialtyCode)
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return professionList;
    }

    private List<Worker> findWorkersByCondition(String condition, String[] args) {
        List<Worker> workerList = new ArrayList<>();
        try (SQLiteDatabase database = dbHelper.getReadableDatabase(); Cursor cursor = findByCondition(
                database,
                "profession as p left join worker as w on p._id = w.prof_id",
                new String[]{"w._id", "w.first_name", "w.last_name", "w.date_of_birth", "w.link", "w.prof_id", "p.name", "p.code"},
                condition,
                args,
                "w.last_name"
        )) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("prof_id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int specialtyCode = cursor.getInt(cursor.getColumnIndex("code"));
                    Profession profession = new Profession(id, name, specialtyCode);
                    int wId = cursor.getInt(cursor.getColumnIndex("_id"));
                    String wFName = cursor.getString(cursor.getColumnIndex("first_name"));
                    String wLName = cursor.getString(cursor.getColumnIndex("last_name"));
                    long wDate = cursor.getLong(cursor.getColumnIndex("date_of_birth"));
                    String wLink = cursor.getString(cursor.getColumnIndex("link"));
                    workerList.add(
                            new Worker(wId, wFName, wLName, wDate, wLink, profession)
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return workerList;
    }

    private Cursor findByCondition(SQLiteDatabase database, String table, String[] columns, String condition, String[] args, String orderBy) {
        return database.query(table, columns, condition, args, null, null, orderBy);
    }

    @Override
    public List<Worker> findAllWorkers() {
        return findWorkersByCondition(null, null);
    }

    @Override
    public Profession findProfessionById(int id) {
        List<Profession> professionList = findProfessionsByCondition("_id = ?", new String[]{String.valueOf(id)});
        return professionList.size() > 0 ? professionList.get(0) : null;
    }

    @Override
    public Worker findWorkerById(int id) {
        List<Worker> workerList = findWorkersByCondition("w._id = ?", new String[]{String.valueOf(id)});
        return workerList.size() > 0 ? workerList.get(0) : null;
    }

    @Override
    public List<Profession> findProfessionsByName(String name) {
        return findProfessionsByCondition("name = ?", new String[]{name});
    }

    @Override
    public List<Worker> findWorkersByName(String name) {
        return findWorkersByCondition("w.first_name = ?", new String[]{name});
    }

    @Override
    public List<Worker> findWorkersByProfession(Profession profession) {
        return findWorkersByCondition("w.prof_id = ?", new String[]{String.valueOf(profession.getId())});
    }
}
