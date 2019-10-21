/*
 * Copyright (c) 2019.
 * Project created and maintained by sanjay kranthi kumar
 * if need to contribute contact us on
 * kranthi0987@gmail.com
 */

package com.sanjay.ejabberd.database.dao;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sanjay.ejabberd.app.Constants;
import com.sanjay.ejabberd.exceptions.OelpException;

public class MessagesDao {


    public boolean insertMessages(String uuid, String message, String from_user, String to_user, String message_date, String status) throws OelpException {
        boolean isCreated = false;
        SQLiteDatabase db = Constants.ejabberdDatabaseHelper.getWriteDb();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        try {
            values.put("uuid", uuid);
            values.put("message", message);
            values.put("from_user", from_user);
            values.put("to_user", to_user);
            values.put("message_date", message_date);
            values.put("status", status);
            long createdRecordsCount = db.insertWithOnConflict("tbl_messages", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            if (createdRecordsCount != 0)
                isCreated = true;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            isCreated = false;
            throw new OelpException(e.getMessage(), e);
        } finally {
            db.endTransaction();

        }
        return isCreated;
    }
}
