package com.sanjay.ejabberd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class EjabberdDatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "history";
        public static final int DATABASE_VERSION = 48; // = Conversations DATABASE_VERSION + 4
        private static EjabberdDatabaseHelper instance = null;

//        private static String CREATE_CONTATCS_STATEMENT = "create table "



        private EjabberdDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onConfigure(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
            db.rawQuery("PRAGMA secure_delete=ON", null);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

//            db.execSQL("create table " + Message.TABLENAME + "( " + Message.UUID
//                    + " TEXT PRIMARY KEY, " + Message.CONVERSATION + " TEXT, "
//                    + Message.TIME_SENT + " NUMBER, " + Message.COUNTERPART
//                    + " TEXT, " + Message.TRUE_COUNTERPART + " TEXT,"
//                    + Message.BODY + " TEXT, " + Message.ENCRYPTION + " NUMBER, "
//                    + Message.STATUS + " NUMBER," + Message.TYPE + " NUMBER, "
//                    + Message.RELATIVE_FILE_PATH + " TEXT, "
//                    + Message.SERVER_MSG_ID + " TEXT, "
//                    + Message.FINGERPRINT + " TEXT, "
//                    + Message.CARBON + " INTEGER, "
//                    + Message.EDITED + " TEXT, "
//                    + Message.READ + " NUMBER DEFAULT 1, "
//                    + Message.DELETED + " NUMBER DEFAULT 0, "
//                    + Message.OOB + " INTEGER, "
//                    + Message.ERROR_MESSAGE + " TEXT,"
//                    + Message.READ_BY_MARKERS + " TEXT,"
//                    + Message.MARKABLE + " NUMBER DEFAULT 0,"
//                    + Message.FILE_DELETED + " NUMBER DEFAULT 0,"
//                    + Message.REMOTE_MSG_ID + " TEXT, FOREIGN KEY("
//                    + Message.CONVERSATION + ") REFERENCES "
//                    + Conversation.TABLENAME + "(" + Conversation.UUID
//                    + ") ON DELETE CASCADE);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


//        public void createConversation(Conversation conversation) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.insert(Conversation.TABLENAME, null, conversation.getContentValues());
//        }
//
//        public void createMessage(Message message) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.insert(Message.TABLENAME, null, message.getContentValues());
//        }
//
//        public void createAccount(Account account) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.insert(Account.TABLENAME, null, account.getContentValues());
//        }
//
//        public void insertDiscoveryResult(ServiceDiscoveryResult result) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.insert(ServiceDiscoveryResult.TABLENAME, null, result.getContentValues());
//        }
//
//        public ServiceDiscoveryResult findDiscoveryResult(final String hash, final String ver) {
//            SQLiteDatabase db = this.getReadableDatabase();
//            String[] selectionArgs = {hash, ver};
//            Cursor cursor = db.query(ServiceDiscoveryResult.TABLENAME, null,
//                    ServiceDiscoveryResult.HASH + "=? AND " + ServiceDiscoveryResult.VER + "=?",
//                    selectionArgs, null, null, null);
//            if (cursor.getCount() == 0) {
//                cursor.close();
//                return null;
//            }
//            cursor.moveToFirst();
//
//            ServiceDiscoveryResult result = null;
//            try {
//                result = new ServiceDiscoveryResult(cursor);
//            } catch (JSONException e) { /* result is still null */ }
//
//            cursor.close();
//            return result;
//        }
//
//        public void saveResolverResult(String domain, Resolver.Result result) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues contentValues = result.toContentValues();
//            contentValues.put(Resolver.Result.DOMAIN, domain);
//            db.insert(RESOLVER_RESULTS_TABLENAME, null, contentValues);
//        }
//
//
//
//        public ArrayList<Message> getMessages(Conversation conversations, int limit) {
//            return getMessages(conversations, limit, -1);
//        }
//
//        public ArrayList<Message> getMessages(Conversation conversation, int limit, long timestamp) {
//            ArrayList<Message> list = new ArrayList<>();
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor;
//            if (timestamp == -1) {
//                String[] selectionArgs = {conversation.getUuid(), "1"};
//                cursor = db.query(Message.TABLENAME, null, Message.CONVERSATION
//                        + "=? and " + Message.DELETED + "<?", selectionArgs, null, null, Message.TIME_SENT
//                        + " DESC", String.valueOf(limit));
//            } else {
//                String[] selectionArgs = {conversation.getUuid(), Long.toString(timestamp), "1"};
//                cursor = db.query(Message.TABLENAME, null, Message.CONVERSATION
//                                + "=? and " + Message.TIME_SENT + "<? and " + Message.DELETED + "<?", selectionArgs,
//                        null, null, Message.TIME_SENT + " DESC",
//                        String.valueOf(limit));
//            }
//            while (cursor.moveToNext()) {
//                try {
//                    final Message message = Message.fromCursor(cursor, conversation);
//                    if (message != null && !message.isMessageDeleted()) {
//                        list.add(0, message);
//                    }
//                } catch (Exception e) {
//                    Log.e(Config.LOGTAG, "unable to restore message");
//                }
//            }
//            cursor.close();
//            return list;
//        }
//
//        public Cursor getMessageSearchCursor(List<String> term) {
//            SQLiteDatabase db = this.getReadableDatabase();
//            String SQL = "SELECT " + Message.TABLENAME + ".*," + Conversation.TABLENAME + '.' + Conversation.CONTACTJID + ',' + Conversation.TABLENAME + '.' + Conversation.ACCOUNT + ',' + Conversation.TABLENAME + '.' + Conversation.MODE + " FROM " + Message.TABLENAME + " join " + Conversation.TABLENAME + " on " + Message.TABLENAME + '.' + Message.CONVERSATION + '=' + Conversation.TABLENAME + '.' + Conversation.UUID + " join messages_index ON messages_index.uuid=messages.uuid where " + Message.ENCRYPTION + " NOT IN(" + Message.ENCRYPTION_AXOLOTL_NOT_FOR_THIS_DEVICE + ',' + Message.ENCRYPTION_PGP + ',' + Message.ENCRYPTION_DECRYPTION_FAILED + ',' + Message.ENCRYPTION_AXOLOTL_FAILED + ") AND " + Message.TYPE + " IN(" + Message.TYPE_TEXT + ',' + Message.TYPE_PRIVATE + ") AND messages_index.body MATCH ? ORDER BY " + Message.TIME_SENT + " DESC limit " + Config.MAX_SEARCH_RESULTS;
//            Log.d(Config.LOGTAG, "search term: " + FtsUtils.toMatchString(term));
//            return db.rawQuery(SQL, new String[]{FtsUtils.toMatchString(term)});
//        }
//
//        public Iterable<Message> getMessagesIterable(final Conversation conversation) {
//            return () -> {
//                class MessageIterator implements Iterator<Message> {
//                    SQLiteDatabase db = getReadableDatabase();
//                    String[] selectionArgs = {conversation.getUuid(), "1"};
//                    Cursor cursor = db.query(Message.TABLENAME, null, Message.CONVERSATION
//                            + "=? and " + Message.DELETED + "<?", selectionArgs, null, null, Message.TIME_SENT
//                            + " ASC", null);
//
//                    public MessageIterator() {
//                        cursor.moveToFirst();
//                    }
//
//                    @Override
//                    public boolean hasNext() {
//                        return !cursor.isAfterLast();
//                    }
//
//                    @Override
//                    public Message next() {
//                        Message message = Message.fromCursor(cursor, conversation);
//                        cursor.moveToNext();
//                        return message;
//                    }
//
//                    @Override
//                    public void remove() {
//                        throw new UnsupportedOperationException();
//                    }
//                }
//                return new MessageIterator();
//            };
//        }
//
//
//
//        public Conversation findConversation(final Account account, final Jid contactJid) {
//            SQLiteDatabase db = this.getReadableDatabase();
//            String[] selectionArgs = {account.getUuid(),
//                    contactJid.asBareJid().toString() + "/%",
//                    contactJid.asBareJid().toString()
//            };
//            Cursor cursor = db.query(Conversation.TABLENAME, null,
//                    Conversation.ACCOUNT + "=? AND (" + Conversation.CONTACTJID
//                            + " like ? OR " + Conversation.CONTACTJID + "=?)", selectionArgs, null, null, null);
//            if (cursor.getCount() == 0) {
//                cursor.close();
//                return null;
//            }
//            cursor.moveToFirst();
//            Conversation conversation = Conversation.fromCursor(cursor);
//            cursor.close();
//            if (conversation.getJid() instanceof InvalidJid) {
//                return null;
//            }
//            return conversation;
//        }
//
//        public void updateConversation(final Conversation conversation) {
//            final SQLiteDatabase db = this.getWritableDatabase();
//            final String[] args = {conversation.getUuid()};
//            db.update(Conversation.TABLENAME, conversation.getContentValues(),
//                    Conversation.UUID + "=?", args);
//        }
//
//        public List<Account> getAccounts() {
//            SQLiteDatabase db = this.getReadableDatabase();
//            return getAccounts(db);
//        }
//
//        public List<Jid> getAccountJids(final boolean enabledOnly) {
//            SQLiteDatabase db = this.getReadableDatabase();
//            final List<Jid> jids = new ArrayList<>();
//            final String[] columns = new String[]{Account.USERNAME, Account.SERVER};
//            String where = enabledOnly ? "not options & (1 <<1)" : null;
//            Cursor cursor = db.query(Account.TABLENAME, columns, where, null, null, null, null);
//            try {
//                while (cursor.moveToNext()) {
//                    jids.add(Jid.of(cursor.getString(0), cursor.getString(1), null));
//                }
//                return jids;
//            } catch (Exception e) {
//                return jids;
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//        }
//
//
//        private List<Account> getAccounts(SQLiteDatabase db) {
//            List<Account> list = new ArrayList<>();
//            try (Cursor cursor = db.query(Account.TABLENAME, null, null, null, null,
//                    null, null)) {
//                while (cursor.moveToNext()) {
//                    list.add(Account.fromCursor(cursor));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return list;
//        }
//
//        public boolean updateAccount(Account account) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String[] args = {account.getUuid()};
//            final int rows = db.update(Account.TABLENAME, account.getContentValues(), Account.UUID + "=?", args);
//            return rows == 1;
//        }
//
//        public boolean deleteAccount(Account account) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String[] args = {account.getUuid()};
//            final int rows = db.delete(Account.TABLENAME, Account.UUID + "=?", args);
//            return rows == 1;
//        }
//
//        public boolean updateMessage(Message message, boolean includeBody) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String[] args = {message.getUuid()};
//            ContentValues contentValues = message.getContentValues();
//            contentValues.remove(Message.UUID);
//            if (!includeBody) {
//                contentValues.remove(Message.BODY);
//            }
//            return db.update(Message.TABLENAME, message.getContentValues(), Message.UUID + "=?", args) == 1;
//        }
//
//        public boolean updateMessage(Message message, String uuid) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String[] args = {uuid};
//            return db.update(Message.TABLENAME, message.getContentValues(), Message.UUID + "=?", args) == 1;
//        }
//
//        public void readRoster(Roster roster) {
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor;
//            String[] args = {roster.getAccount().getUuid()};
//            cursor = db.query(Contact.TABLENAME, null, Contact.ACCOUNT + "=?", args, null, null, null);
//            while (cursor.moveToNext()) {
//                roster.initContact(Contact.fromCursor(cursor));
//            }
//            cursor.close();
//        }
//
//        public void writeRoster(final Roster roster) {
//            long start = SystemClock.elapsedRealtime();
//            final Account account = roster.getAccount();
//            final SQLiteDatabase db = this.getWritableDatabase();
//            db.beginTransaction();
//            for (Contact contact : roster.getContacts()) {
//                if (contact.getOption(Contact.Options.IN_ROSTER) || contact.getAvatarFilename() != null || contact.getOption(Contact.Options.SYNCED_VIA_OTHER)) {
//                    db.insert(Contact.TABLENAME, null, contact.getContentValues());
//                } else {
//                    String where = Contact.ACCOUNT + "=? AND " + Contact.JID + "=?";
//                    String[] whereArgs = {account.getUuid(), contact.getJid().toString()};
//                    db.delete(Contact.TABLENAME, where, whereArgs);
//                }
//            }
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            account.setRosterVersion(roster.getVersion());
//            updateAccount(account);
//            long duration = SystemClock.elapsedRealtime() - start;
//            Log.d(Config.LOGTAG, account.getJid().asBareJid() + ": persisted roster in " + duration + "ms");
//        }
//
//        public void deleteMessageInConversation(Message message) {
//            long start = SystemClock.elapsedRealtime();
//            final SQLiteDatabase db = this.getWritableDatabase();
//            db.beginTransaction();
//            ContentValues values = new ContentValues();
//            values.put(Message.DELETED, "1");
//            String[] args = {message.getUuid()};
//            int rows = db.update("messages", values, "uuid =?", args);
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            Log.d(Config.LOGTAG, "deleted " + rows + " message in " + (SystemClock.elapsedRealtime() - start) + "ms");
//        }
//
//        public void deleteMessagesInConversation(Conversation conversation) {
//            long start = SystemClock.elapsedRealtime();
//            final SQLiteDatabase db = this.getWritableDatabase();
//            db.beginTransaction();
//            String[] args = {conversation.getUuid()};
//            db.delete("messages_index", "uuid in (select uuid from messages where conversationUuid=?)", args);
//            int num = db.delete(Message.TABLENAME, Message.CONVERSATION + "=?", args);
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            Log.d(Config.LOGTAG, "deleted " + num + " messages for " + conversation.getJid().asBareJid() + " in " + (SystemClock.elapsedRealtime() - start) + "ms");
//        }
//
//        public long countExpireOldMessages(long timestamp) {
//            long start = SystemClock.elapsedRealtime();
//            final String[] args = {String.valueOf(timestamp)};
//            SQLiteDatabase db = this.getReadableDatabase();
//            db.beginTransaction();
//            long num = DatabaseUtils.queryNumEntries(db, Message.TABLENAME, "timeSent<?", args);
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            Log.d(Config.LOGTAG, "found " + num + " expired messages in " + (SystemClock.elapsedRealtime() - start) + "ms");
//            return num;
//        }
//
//        public long getOldestMessages() {
//            Cursor cursor = null;
//            try {
//                SQLiteDatabase db = this.getReadableDatabase();
//                db.beginTransaction();
//                cursor = db.rawQuery("select timeSent from " + Message.TABLENAME + " ORDER BY " + Message.TIME_SENT + " ASC limit 1", null);
//                db.setTransactionSuccessful();
//                db.endTransaction();
//                if (cursor.getCount() == 0) {
//                    return 0;
//                } else {
//                    cursor.moveToFirst();
//                    return cursor.getLong(0);
//                }
//            } catch (Exception e) {
//                return 0;
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//        }
//
//        public int expireOldMessages(long timestamp) {
//            long start = SystemClock.elapsedRealtime();
//            int num = 0;
//            if (countExpireOldMessages(timestamp) >= 1) {
//                final String[] args = {String.valueOf(timestamp)};
//                SQLiteDatabase db = this.getReadableDatabase();
//                db.beginTransaction();
//                db.delete("messages_index", "uuid in (select uuid from messages where timeSent<?)", args);
//                num = db.delete(Message.TABLENAME, "timeSent<?", args);
//                db.setTransactionSuccessful();
//                db.endTransaction();
//            }
//            Log.d(Config.LOGTAG, "deleted " + num + " expired messages in " + (SystemClock.elapsedRealtime() - start) + "ms");
//            return num;
//        }
//


}
