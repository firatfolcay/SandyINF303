package sandy.android.assistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import java.util.Vector;

public class DatabaseTest extends SQLiteOpenHelper {        //this is a test class to test database operations

    public static final String TEST_DATABASE_NAME = "test.db";      //defining of note attributes that will be used while fetching and storing note data to database.
    public static final String TEST_TABLE_NAME = "test";
    public static final String TEST_COLUMN_TITLE = "note_title";
    public static final String TEST_COLUMN_CONTENT = "note_content";
    public static final String TEST_COLUMN_IMAGES = "note_images";

    EditText noteeditor_title_text;

    public DatabaseTest(Context context) {        //DatabaseManagement constructor method
        super(context, TEST_DATABASE_NAME, null, 1);
        //getReadableDatabase();
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {       //SQLiteOpenHelper class-dependent method to create database tables

        noteeditor_title_text = (EditText) noteeditor_title_text.findViewById(R.id.noteeditor_title_text);
        db.execSQL("CREATE TABLE test " +
                "(note_title text PRIMARY KEY, note_content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS test");
        onCreate(db);
    }

    public void createTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE test " +
                "(note_title text PRIMARY KEY, note_content text)");
    }

    public void insertNote (String note_title, String note_content) {    //method to insert a new note to test database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_title", note_title);
        contentValues.put("note_content", note_content);
        db.insert("test", null, contentValues);

        //return true;
    }

    public String fetchContent () {     //method to fetch note data from test database
        String str = null;

        //String title = noteeditor_title_text.toString();
        String arman = "Denemexyz";
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor res = db.rawQuery("select note_content from test where " + "note_title" + "=?", new String[]{arman});
        res.moveToFirst();
        if (res.isAfterLast() == false) {
            str = res.getString(res.getColumnIndex(TEST_COLUMN_CONTENT));
        }

        return str;
    }
}
