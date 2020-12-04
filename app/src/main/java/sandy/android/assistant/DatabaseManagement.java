package sandy.android.assistant;

import java.text.ParseException;
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
    //public static final String NOTIFICATIONS_COLUMN_NOTE_ID = "notification_note_id";



    public DatabaseManagement(Context context) {        //DatabaseManagement constructor method
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {       //SQLiteOpenHelper class-dependent method to create database tables
        String fk = "PRAGMA foreign_keys = 1";
        db.execSQL(fk);


        String notifications_sql = "create table " + NOTIFICATIONS_TABLE_NAME  +
                " (" + NOTIFICATIONS_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                NOTIFICATIONS_COLUMN_DATE + " text" + ")";
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
        if(n.getNotification() != null)
            contentValues.put(NOTES_COLUMN_NOTIFICATION_ID, n.getNotification().getId());
        db.update(NOTES_TABLE_NAME,
                contentValues,
                "id = ? ",
                new String[] { Integer.toString(key.getId()) } );
        return true;
    }

    public Boolean deleteNote (Note n) {        //method to delete a note from database
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(NOTES_TABLE_NAME,
                    NOTES_COLUMN_ID + " = ? ",
                    new String[] { Integer.toString(n.getId()) });

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Note getNoteFromNoteId(int note_id) throws ParseException {       //method to fetch note data from given note id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Notification notification = new Notification();

        Cursor res =  db.rawQuery( "select * from " + NOTES_TABLE_NAME + " where " + NOTES_COLUMN_ID +"= " + note_id,
                null );
        res.moveToFirst();

        if (res.getString(res.getColumnIndex(NOTES_COLUMN_NOTIFICATION_ID)) != null) {
            Cursor resNotifications = db.rawQuery( "select * from " + NOTIFICATIONS_TABLE_NAME + " where " + NOTIFICATIONS_COLUMN_ID +"= " + res.getInt(res.getColumnIndex(NOTES_COLUMN_NOTIFICATION_ID)),
                    null );
            resNotifications.moveToFirst();
            if (!resNotifications.isNull(resNotifications.getInt(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_ID)))) {
                notification.setId(resNotifications.getInt(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_ID)));
                notification.setDate(resNotifications.getString(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_DATE)));
            }
        }

        return new Note(res.getInt(res.getColumnIndex(NOTES_COLUMN_ID)),
                res.getString(res.getColumnIndex(NOTES_COLUMN_TITLE)),
                res.getString(res.getColumnIndex(NOTES_COLUMN_CONTENT)),
                notification,
                res.getString(res.getColumnIndex(NOTES_COLUMN_SAVEDATE)));
    }

    public int getNoteCount(){      //method to fetch number of notes that exist in database
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME);
        return numRows;
    }

    public ArrayList<Note> getAllNotes() throws ParseException {        //method to fetch data of all notes from database
        Notification foundNotification = new Notification();
        //Note foundNote = null;
        ArrayList<Note> array_list = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resNotes =  db.rawQuery( "select * from " + NOTES_TABLE_NAME, null );
        resNotes.moveToFirst();

        while(resNotes.isAfterLast() == false){
            // gets the notification if the note has one
            if (resNotes.getString(resNotes.getColumnIndex(NOTES_COLUMN_NOTIFICATION_ID)) != null) {
                Cursor resNotifications = db.rawQuery( "select * from " + NOTIFICATIONS_TABLE_NAME + " where " + NOTIFICATIONS_COLUMN_ID +"= " + resNotes.getInt(resNotes.getColumnIndex(NOTES_COLUMN_NOTIFICATION_ID)),
                        null );
                if (!resNotifications.isNull(resNotifications.getInt(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_ID)))) {
                    foundNotification.setId(resNotifications.getInt(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_ID)));
                    foundNotification.setDate(resNotifications.getString(resNotifications.getColumnIndex(NOTIFICATIONS_COLUMN_DATE)));
                }
            }
            // creates and adds note to the array to be returned
            Note foundNote = new Note(resNotes.getInt(resNotes.getColumnIndex(NOTES_COLUMN_ID)),
                    resNotes.getString(resNotes.getColumnIndex(NOTES_COLUMN_TITLE)),
                    resNotes.getString(resNotes.getColumnIndex(NOTES_COLUMN_CONTENT)),
                    foundNotification,
                    resNotes.getString(resNotes.getColumnIndex(NOTES_COLUMN_SAVEDATE)));
            array_list.add(foundNote);
            resNotes.moveToNext();
        }
        return array_list;
    }

    /**************************************************************************************************/

    /*methods below are for manipulating notification data in database*/

    public boolean insertNotification (Notification n) {    //method to insert a new notification to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATIONS_COLUMN_DATE, n.getDate());

        db.insert(NOTIFICATIONS_TABLE_NAME,null, contentValues);
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

    public Notification getDataFromNotificationID(int notification_id) throws ParseException {       //method to fetch notification data from given notification id from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + NOTIFICATIONS_TABLE_NAME + " where " + NOTIFICATIONS_COLUMN_ID + "=" + notification_id,
                null );
        Notification n = new Notification();
        n.setId(res.getInt(res.getColumnIndex(NOTIFICATIONS_COLUMN_ID)));
        n.setDate(res.getString(res.getColumnIndex(NOTIFICATIONS_COLUMN_DATE)));
        return new Notification();
    }

    public int getNotificationCount(){      //method to fetch number of notifications that exist in database
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTIFICATIONS_TABLE_NAME);
        return numRows;
    }
    public int getLastAddedNotificationId (){
        String selectQuery= "SELECT * FROM " + NOTIFICATIONS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int id;

        cursor.moveToLast();
        id =cursor.getInt(cursor.getColumnIndex(NOTIFICATIONS_COLUMN_ID));

        return id;
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

