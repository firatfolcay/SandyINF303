package sandy.android.assistant;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManagement extends SQLiteOpenHelper {      //DatabaseManagement class that inherits SQLiteOpenHelper Functions

    public static final String NOTES_DATABASE_NAME = "notes.db";      //defining of note attributes that will be used while fetching and storing note data to database.
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_ID = "note_id";
    public static final String NOTES_COLUMN_TITLE = "note_title";
    public static final String NOTES_COLUMN_TEXT = "note_text";
    public static final String NOTES_COLUMN_SAVEDATE = "note_savedate";
    public static final String NOTES_COLUMN_NOTIFICATION_ID = "note_notification_id";

    public static final String NOTIFICATIONS_DATABASE_NAME = "notifications.db";        //defining of notification attributes that will be used while fetching and storing notification data to database.
    public static final String NOTIFICATIONS_TABLE_NAME = "notifications";
    public static final String NOTIFICATIONS_COLUMN_ID = "notification_id";
    public static final String NOTIFICATIONS_COLUMN_DATE = "notification_date";
    public static final String NOTIFICATIONS_COLUMN_NOTE_ID = "notification_note_id";

    public DatabaseManagement(Context context) {        //DatabaseManagement constructor method
        super(context, NOTES_DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {       //SQLiteOpenHelper class-dependent method to create database tables
        db.execSQL("create table notes " +
                "(note_id integer primary key AUTOINCREMENT, note_title text, note_text text, note_savedate text, FOREIGN KEY(note_notification_id) REFERENCES notification(notification_id))");

        db.execSQL("create table notifications " +
                "(notification_id integer primary key AUTOINCREMENT, notification_date text, FOREIGN KEY(notification_note_id) REFERENCES notes(note_id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {      //SQLiteOpenHelper class-dependent method to refresh database tables
        db.execSQL("DROP TABLE IF EXISTS notes");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        onCreate(db);
    }

    /**************************************************************************************************/

    /*methods below are for manipulating note data in database*/

    public boolean insertNote (String note_title, String note_text, String note_savedate) {    //method to insert a new note to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_title", note_title);
        contentValues.put("note_text", note_text);
        contentValues.put("note_savedate", note_savedate);
        db.insert("notes", null, contentValues);
        return true;
    }

    public boolean updateNote (Integer note_id, String note_title, String note_text, String note_savedate) {    //method to update a note from database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_title", note_title);
        contentValues.put("note_text", note_text);
        contentValues.put("note_savedate", note_savedate);

        db.update("notes", contentValues, "id = ? ", new String[] { Integer.toString(note_id) } );
        return true;
    }

    public Integer deleteNote (Integer note_id) {        //method to delete a note from database
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notes",
                "note_id = ? ",
                new String[] { Integer.toString(note_id) });
    }

    public Cursor getDataFromNoteID(int note_id) {       //method to fetch note data from given note id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes where note_id="+note_id+"", null );
        return res;
    }

    public int getNoteCount(){      //method to fetch number of notes that exist in database
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllNotes() {        //method to fetch data of all notes from database
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }

    /**************************************************************************************************/

    /*methods below are for manipulating notification data in database*/

    public boolean insertNotification (String notification_date) {    //method to insert a new notification to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notification_title", notification_date);

        db.insert("notifications", null, contentValues);
        return true;
    }

    public boolean updateNotification (Integer notification_id, String notification_date) {    //method to update a notification from database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", notification_date);

        db.update("notifications", contentValues, "notification_id = ? ", new String[] { Integer.toString(notification_id) } );
        return true;
    }

    public Integer deleteNotification (Integer notification_id) {        //method to delete a notification from database
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notifications",
                "notification_id = ? ",
                new String[] { Integer.toString(notification_id) });
    }

    public Cursor getDataFromNotificationID(int notification_id) {       //method to fetch notification data from given notification id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notifications where notification_id="+notification_id+"", null );
        return res;
    }

    public int getNotificationCount(){      //method to fetch number of notifications that exist in database
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTIFICATIONS_TABLE_NAME);
        return numRows;
    }

    /*public ArrayList<String> getAllNotifications() {        //method to fetch data of all notifications from database
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notifications", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTIFICATIONS_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }*/


}

