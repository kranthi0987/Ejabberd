/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sanjay.ejabberd.database.EjabberdDatabaseHelper;

public class OelpchatApplication extends Application {
    private static OelpchatApplication instance;

    public OelpchatApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // init
        EjabberdDatabaseHelper mDbHelper = new EjabberdDatabaseHelper(this);
        SQLiteDatabase localdb = mDbHelper.getWritableDatabase();

    }
}
