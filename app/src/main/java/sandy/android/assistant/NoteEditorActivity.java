package sandy.android.assistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.models.Node;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.google.gson.Gson;

import top.defaults.colorpicker.ColorPickerPopup;

public class NoteEditorActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 0;
    private static final int REQUEST_NOTIFICATION = 1;

    Toolbar toolbar;
    RecyclerView recyclerView;
    View view;
    DatabaseManagement db;
    boolean isFABOpen = false;
    ArrayList notesFromDB = new ArrayList();

    CalendarSync calendarSync;


    Context context;

    Editor editor;
    EditText noteeditor_title_text;

    Button button_db;
    FloatingActionButton fab_noteeditor_options;
    FloatingActionButton fab_noteeditor_options_addimage;
    FloatingActionButton fab_noteeditor_options_timer;
    FloatingActionButton fab_noteeditor_options_calendar;

    ImageView imageView_back;
    ImageView imageView_save_note;

    RecyclerView listOfNotes;

    Uri targetUri;

    Note editNote;
    Notification notification; //will be null until a notification from notification screen is added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //INITIALIZE VARIABLES BEFORE EVERYTHING ELSE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        calendarSync = new CalendarSync();

        context = getApplicationContext();

        editor = (Editor) findViewById(R.id.editor);


        //DatabaseTest dbt = new DatabaseTest(this);
        db = new DatabaseManagement(this);


        notesFromDB = db.getAllNotes();
        NoteAdapter noteAdapter = new NoteAdapter(this, notesFromDB, db);
        notification = null;

        isFABOpen = false;      //initialization of attributes that will be used during run of onCreate method

        noteeditor_title_text = (EditText) findViewById(R.id.noteeditor_title_text);

        fab_noteeditor_options = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options);      //initialization of attributes that are referenced into note_editor.xml
        fab_noteeditor_options_addimage = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_addimage);
        fab_noteeditor_options_timer = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_timer);
        fab_noteeditor_options_calendar = (FloatingActionButton) findViewById(R.id.fab_noteeditor_options_calendar);

        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_save_note = (ImageView) findViewById(R.id.imageView_save_note);

        // GET ID FOR NOTE EDITING
        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.get("NOTE_ID") != null){
                editNote = db.getNoteFromNoteId(b.getInt("NOTE_ID"));
                //System.out.println("NOTE ID: " + editNote.getId() + "\n");
                updateEditor(editNote);
            }
        }

        // FUNCTIONS

        fab_noteeditor_options.setOnClickListener(new View.OnClickListener() {      //onClick listener for NoteEditor options floating action button
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {      //onClick listener for NoteEditor back button
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView_save_note.setOnClickListener(new View.OnClickListener() {     //onClick listener for save note button in noteeditor.
            @Override
            public void onClick(View v) {
                for(Node item: editor.getContent().nodes){
                    if(item.content.size() > 0){
                        if(item.content.get(0).toString().isEmpty()){
                            continue;   //if the current one is empty move to next item
                        }
                    }
                    else{       //content is not empty therefore move on.

                        if(notification == null){
                            //System.out.println("NOTIFICATION IS NULL");
                        }
                        else{
                            //System.out.println("NOTIFICATION_DATE: " + notification.getDate());
                        }

                        if (editNote == null) {     //if new Note will be created
                            String content = editor.getContentAsHTML();
                            String title = noteeditor_title_text.getText().toString();
                            Date currentTime = Calendar.getInstance().getTime();
                            String date = currentTime.toString();

                            Note n = new Note(title,
                                    content,
                                    notification,
                                    date);

                            db.insertNote(n);
                        }
                        else{        //if selected Note will be edited
                            String content = editor.getContentAsHTML();
                            String title = noteeditor_title_text.getText().toString();
                            Date currentTime = Calendar.getInstance().getTime();
                            String date = currentTime.toString();

                            Note newNote = new Note(title,
                                    content,
                                    notification,
                                    date);

                            db.updateNote(newNote, editNote);
                            if (notification != null) {
                                int numberOfRowsAffected = 0;
                                numberOfRowsAffected = calendarSync.updateCalendarEntry(context, db.getLastAddedNotification().getId(), newNote);
                                if (numberOfRowsAffected > 0) {
                                    Toast.makeText(context, "calendar event of edited note is also updated.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                if (editNote.getNotification() != null) {
                                    int numberOfRowsAffected = 0;
                                    numberOfRowsAffected = calendarSync.updateCalendarEntry(context, editNote.getNotification().getId(), newNote);
                                    if (numberOfRowsAffected > 0) {
                                        Toast.makeText(context, "calendar event of edited note is also updated.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        notesFromDB = db.getAllNotes();
                        listOfNotes = findViewById(R.id.listOfNotes);
                        break;
                    }
                }
                finish();

                return;
            }
        });

        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {        //onClick listener for text size options
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {      //onClick listener for bold text font
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {        //onClick listener for italic text font
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {        //onClick listener for indent
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {       //onClick listener for outdent
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {      //onClick listener for bulleted
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {         //onClick listener for color selection
            //FIXME this function makes the font red and you can't change it back to black
            @Override
            public void onClick(View v) {
                editor.updateTextColor("#FF3333");
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {        //onClick listener for ordering text
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {        //onClick listener for inserting line divider
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });

        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {         //onClick listener for color picker
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(context)
                        .initialColor(Color.RED) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                Toast.makeText(context, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                                editor.updateTextColor(colorHex(color));
                            }
                        });


            }
        });


        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {      //onClick listener for add image from toolbox
            @Override
            public void onClick(View v) {
                //editor.openImagePicker();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       //initialization of new intent that launches External Storage browser
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        findViewById(R.id.fab_noteeditor_options_addimage).setOnClickListener(new View.OnClickListener() {      //onClick listener for add image from action button
            @Override
            public void onClick(View v) {
                //editor.openImagePicker();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       //initialization of new intent that launches External Storage browser
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {       //onClick listener for link inserting
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {     //onClick listener for erase all contents from editor option
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

        editor.setEditorListener(new EditorListener() {     //editor activity listener
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                editor.onImageUploadComplete(targetUri.toString(), uuid);
                // editor.onImageUploadFailed(uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                View v = null;
                return v;
            }

        });

        fab_noteeditor_options_addimage.setOnClickListener(new View.OnClickListener() {     //onClick listener for add image function
            @Override
            public void onClick(View v) {
                //editor.openImagePicker();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       //initialization of new intent that launches External Storage browser
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        fab_noteeditor_options_timer.setOnClickListener(new View.OnClickListener() {        //onClick Listener for notification editor
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationEditorActivity.class);

                if(editNote != null) {    //if the note is getting edited, it should send it's current Notification to NotificationEditorActivity. Otherwise it sends an empty intent which is handled already.
                    if (editNote.getNotification() != null) {
                        //do not send the object, send the id. It is handled that way in NotificationEditorActivity.
                        intent.putExtra("NOTIFICATION_ID", editNote.getNotification().getId());
                    }
                }

                startActivityForResult(intent, REQUEST_NOTIFICATION);
            }
        });

        fab_noteeditor_options_calendar.setOnClickListener(new View.OnClickListener() {         //onClick Listener for calendar synchronization
            @Override
            public void onClick(View v) {
                if (editNote.getNotification() != null) {       //if note that is edited has a notification attached
                    calendarSync = new CalendarSync(editNote.getTitle(), editNote.getNotification(), editNote.getContent());      //instantiate new calendarSync object
                    //addCalendarEvent(calendarSync.getEventTitle(), calendarSync.getEventDescription(), calendarSync.getEventDate());     //call method that sends Note info to Calendar API
                    calendarSync.addCalendarEventInBackground(context, calendarSync.getEventTitle(), calendarSync.getEventDescription(), calendarSync.getEventNotification());
                    closeFABMenu();
                }
                else {          //if there's no notifications attached to note,
                    Toast.makeText(getApplicationContext(), "There are no notifications attached to Note.", Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //activity result handler.
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQUEST_IMAGE:     //if image from external storage is selected
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (Build.VERSION.SDK_INT < 19) {
                            targetUri = data.getData();
                        } else {
                            targetUri = data.getData();
                            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            try {
                                getContentResolver().takePersistableUriPermission(targetUri, takeFlags);
                            } catch (SecurityException se) {
                                se.printStackTrace();
                            }
                        }
                        try {       //fetches selected image from media storage and insert into editor
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                            // Log.d(TAG, String.valueOf(bitmap));
                            editor.insertImage(bitmap);
                            String html = editor.getContentAsHTML();
                            System.out.println("html : " + html);
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case REQUEST_NOTIFICATION:      //if a notification is added
                switch(resultCode){
                    case Activity.RESULT_OK:
                        //gets back data from NotificationEditorActivity
                        String date = data.getStringExtra("NOTIFICATION_DATE");

                        if(date.contains("null"))
                            break;

                        notification = new Notification(date);
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.closeFABMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notification = null;
    }

    private void showFABMenu(){         //method that makes sub-FAB menus visible
        System.out.println("showFABMenu");
        isFABOpen=true;
        fab_noteeditor_options_addimage.setVisibility(View.VISIBLE);
        System.out.println("fab visibility:" + fab_noteeditor_options_addimage.getVisibility());
        fab_noteeditor_options_timer.setVisibility(View.VISIBLE);
        if (editNote != null) {
            if (editNote.getNotification() != null) {
                fab_noteeditor_options_calendar.setVisibility(View.VISIBLE);
            }
        }
        /*fab_noteeditor_options_addimage.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_noteeditor_options_timer.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab_noteeditor_options_calendar.animate().translationY(-getResources().getDimension(R.dimen.standard_155));*/
    }

    private void closeFABMenu(){        //method that makes sub-FAB menus invisible
        System.out.println("closeFABMenu");
        isFABOpen=false;
        /*fab_noteeditor_options_addimage.animate().translationY(0);
        fab_noteeditor_options_timer.animate().translationY(0);
        fab_noteeditor_options_calendar.animate().translationY(0);*/
        fab_noteeditor_options_addimage.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_timer.setVisibility(View.INVISIBLE);
        fab_noteeditor_options_calendar.setVisibility(View.INVISIBLE);
    }

    public void updateEditor(Note n) {      //function that updates Editor context
        noteeditor_title_text.setText(n.getTitle());
        editor.render(n.getContent());
    }

    private String colorHex(int color) {        //returns selected color with its Hex value
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }





}
