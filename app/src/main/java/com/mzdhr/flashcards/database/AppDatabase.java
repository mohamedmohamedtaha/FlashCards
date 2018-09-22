package com.mzdhr.flashcards.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mzdhr.flashcards.database.converter.DateConverter;
import com.mzdhr.flashcards.database.dao.CardDao;
import com.mzdhr.flashcards.database.dao.SubjectDao;
import com.mzdhr.flashcards.database.entity.CardEntity;
import com.mzdhr.flashcards.database.entity.SubjectEntity;

import java.util.Date;

@Database(entities = {SubjectEntity.class, CardEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "flashcardsdb";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating a new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .addCallback(sRoomDatabaseCallback) // to populate database
                        .build();
            }
        }
        Log.d(TAG, "getInstance: Getting the database instance, no need to recreated it.");
        return sInstance;
    }

    public abstract SubjectDao subjectDao();
    public abstract CardDao cardDao();


    /**
     * Populate Database Section
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(sInstance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SubjectDao mSubjectDao;
        private final CardDao mCardDao;

        PopulateDbAsync(AppDatabase db) {
            mSubjectDao = db.subjectDao();
            mCardDao = db.cardDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            SubjectEntity subjectEntity01 = new SubjectEntity("Math", new Date(), 1);
            SubjectEntity subjectEntity02 = new SubjectEntity("Computer", new Date(), 1);
            mSubjectDao.insertSubject(subjectEntity01);
            mSubjectDao.insertSubject(subjectEntity02);
            return null;
        }
    }
}
