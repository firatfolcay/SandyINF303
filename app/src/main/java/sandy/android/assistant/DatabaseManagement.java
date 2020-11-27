package sandy.android.assistant;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseManagement extends SQLiteOpenHelper {      //DatabaseManagement class that inherits SQLiteOpenHelper Functions

    public static final String DATABASE_NAME = "san.db";      //defining of note attributes that will be used while fetching and storing note data to database.
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_ID = "id";
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_CONTENT = "content";
    public static final String NOTES_COLUMN_SAVEDATE = "savedate";
    public static final String NOTES_COLUMN_NOTIFICATION_ID = "notification_id";

    public static final String NOTIFICATIONS_TABLE_NAME = "notifications"; //defining of notification attributes that will be used while fetching and storing notification data to database.
    public static final String NOTIFICATIONS_COLUMN_ID = "id";
    public static final String NOTIFICATIONS_COLUMN_DATE = "date";
    public static final String NOTIFICATIONS_COLUMN_NOTE_ID = "notification_note_id";

    public DatabaseManagement(Context context) {        //DatabaseManagement constructor method
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {       //SQLiteOpenHelper class-dependent method to create database tables
        String fk = "PRAGMA foreign_keys = 1";
        db.execSQL(fk);

        String notifications_sql = "create table " + NOTIFICATIONS_TABLE_NAME  +
                " (" + NOTIFICATIONS_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                NOTIFICATIONS_COLUMN_DATE + "text " + ")";
        db.execSQL(notifications_sql);

        String notes_sql = "create table " + NOTES_TABLE_NAME +
                " (" + NOTES_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                NOTES_COLUMN_TITLE + " text, " +
                NOTES_COLUMN_CONTENT + " text, " +
                NOTES_COLUMN_SAVEDATE + " text, " +
                NOTES_COLUMN_NOTIFICATION_ID + " integer, " +
                "FOREIGN KEY" + "(" + NOTES_COLUMN_NOTIFICATION_ID + ") " +
                "REFERENCES " + NOTIFICATIONS_TABLE_NAME + "(" + NOTIFICATIONS_COLUMN_ID + ")" + ")";
        db.execSQL(notes_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {      //SQLiteOpenHelper class-dependent method to refresh database tables
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS_TABLE_NAME);
        onCreate(db);
    }

    /**************************************************************************************************/

    /*methods below are for manipulating note data in database*/

    public boolean insertNote (Note n) {    //method to insert a new note to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, n.getTitle());
        contentValues.put(NOTES_COLUMN_CONTENT, n.getContent());
        contentValues.put(NOTES_COLUMN_SAVEDATE, n.getSaveDate());

        if(n.getNotification() != null)
            contentValues.put(NOTES_COLUMN_NOTIFICATION_ID, n.getNotification().getId());

        db.insert(NOTES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateNote (Note n, Note key) {    //method to update a note from database //key is the note to be updated #serdar
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_COLUMN_TITLE, n.getTitle());
        contentValues.put(NOTES_COLUMN_CONTENT, n.getContent());
        contentValues.put(NOTES_COLUMN_SAVEDATE, n.getSaveDate());

        db.update(NOTES_TABLE_NAME,
                contentValues,
                "id = ? ",
                new String[] { Integer.toString(n.getId()) } );
        return true;
    }

    public Integer deleteNote (Note n) {        //method to delete a note from database
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTES_TABLE_NAME,
                NOTES_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(n.getId()) });
    }

    public Cursor getDataFromNoteID(int note_id) {       //method to fetch note data from given note id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NOTES_TABLE_NAME + " where " + NOTES_COLUMN_ID +"= " + note_id,
                null );
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
        Cursor res =  db.rawQuery( "select * from " + NOTES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }

    /**************************************************************************************************/

    /*methods below are for manipulating notification data in database*/

    public boolean insertNotification (Notification n) {    //method to insert a new notification to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATIONS_COLUMN_DATE, n.getDate());

        db.insert(NOTIFICATIONS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateNotification (Notification n, Notification key) {    //method to update a notification from database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATIONS_COLUMN_DATE, n.getDate());

        db.update(NOTIFICATIONS_TABLE_NAME,
                contentValues,
                NOTIFICATIONS_COLUMN_ID + "= ? ",
                new String[] { Integer.toString(n.getId()) } );
        return true;
    }

    public Integer deleteNotification (Notification n) {        //method to delete a notification from database
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTIFICATIONS_TABLE_NAME,
                NOTIFICATIONS_COLUMN_ID + "= ? ",
                new String[] { Integer.toString(n.getId()) });
    }

    public Cursor getDataFromNotificationID(int notification_id) {       //method to fetch notification data from given notification id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NOTIFICATIONS_TABLE_NAME + " where " + NOTIFICATIONS_COLUMN_ID + "=" + notification_id,
                null );
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

