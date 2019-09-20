package com.maclee.calorie.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.maclee.calorie.model.Steps;
import com.maclee.calorie.model.StepsDao;

@Database(entities = {Steps.class}, version = 1, exportSchema = false)
public abstract class StepsDB extends RoomDatabase {
    private static volatile StepsDB INSTANCE;
    private static final String DB_NAME = "steps_db";

    public static StepsDB getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (StepsDB.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    StepsDB.class, DB_NAME)
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract StepsDao stepsDao();
}
