/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EjabberdDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase database;
    private String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS tbl_messages (" +
            "uuid TEXT," +
            "message TEXT," +
            "from_user TEXT," +
            "to_user TEXT," +
            "message_date TEXT," +
            "status integer DEFAULT 2)";


    public EjabberdDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase getWriteDb() {
        if (database != null && database.isOpen())
            return database;
        else
            return getWritableDatabase();
    }

}
