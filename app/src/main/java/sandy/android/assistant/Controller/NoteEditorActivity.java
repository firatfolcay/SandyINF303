package sandy.android.assistant.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.github.irshulx.models.Node;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;

import sandy.android.assistant.Adapter.NoteAdapter;
import sandy.android.assistant.Model.CalendarSync;
import sandy.android.assistant.Model.DatabaseManagement;
import sandy.android.assistant.Model.Note;
import sandy.android.assistant.Model.Notification;
import sandy.android.assistant.R;
import sandy.android.assistant.Receiver.NotificationPublisher;
import top.defaults.colorpicker.ColorPickerPopup;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class NoteEditorActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 0;
    private static final int REQUEST_NOTIFICATION = 1;

    DatabaseManagement db;
    boolean isFABOpen = false;
    ArrayList <Note> notesFromDB = new ArrayList();

    CalendarSync calendarSync;


    Context context;

    Editor editor;
    EditText noteeditor_title_text;

    FloatingActionButton fab_noteeditor_options;
    FloatingActionButton fab_noteeditor_options_addimage;
    FloatingActionButton fab_noteeditor_options_timer;
    FloatingActionButton fab_noteeditor_options_calendar;

    ImageView imageView_back;
    ImageView imageView_save_note;

    RecyclerView listOfNotes;

    Uri targetUri;

    public Note editNote;
    Notification notification; //will be null until a notification from notification screen is added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //INITIALIZE VARIABLES BEFORE EVERYTHING ELSE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        calendarSync = new CalendarSync();

        context = getApplicationContext();
        editor = (Editor) findViewById(R.id.editor);

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

                    //content is not empty therefore move on.

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

                        if (notification != null) {       //if created note has a notification attached
                            int calendarReadPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
                            int calendarWritePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);

                            if (calendarReadPermission == PermissionChecker.PERMISSION_GRANTED && calendarWritePermission == PermissionChecker.PERMISSION_GRANTED) {

                                try {       //creates a new "sandy personal assistant" calendar if device doesn't have one.
                                    String calendarName = CalendarSync.getCalendarName(context);
                                    System.out.println("calendar name: " +calendarName);
                                    if (calendarName == null) {
                                        CalendarSync.createNewCalendar(context);
                                    }
                                } catch(Exception e) {
                                    System.out.println("calendar exception.");
                                }
                            }
                            else {
                                Toast.makeText(context, getApplicationContext().getText(R.string.calendar_no_permission_error), Toast.LENGTH_SHORT);
                            }
                            calendarReadPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
                            calendarWritePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);

                            if (calendarReadPermission == PERMISSION_GRANTED && calendarWritePermission == PERMISSION_GRANTED) {
                                notesFromDB = db.getAllNotes();
                                for (int i = 0; i < notesFromDB.size(); i++) {
                                    if (notesFromDB.get(i).getSaveDate().equals(n.getSaveDate())) {      //this is not a good solution, maybe we should fix it by modifying DatabaseManagement.java
                                        calendarSync = new CalendarSync(notesFromDB.get(i).getTitle(), notesFromDB.get(i).getNotification(), notesFromDB.get(i).getContent());      //instantiate new calendarSync object
                                        calendarSync.addCalendarEventInBackground(context, calendarSync.getEventTitle(), calendarSync.getEventDescription(), calendarSync.getEventNotification());
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.calendar_no_permission_error), Toast.LENGTH_LONG).show();
                            }
                        }

                        if (notification != null) {
                            notesFromDB = db.getAllNotes();
                            for (int i = 0; i < notesFromDB.size(); i++) {
                                if (notesFromDB.get(i).getSaveDate().equals(n.getSaveDate())) {      //this is not a good solution, maybe we should fix it by modifying DatabaseManagement.java
                                    Intent intent = new Intent(context, NoteEditorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, notesFromDB.get(i).getNotification().getId(), intent, 0);
                                    scheduleNotification(getNotification(notesFromDB.get(i), pendingIntent), notesFromDB.get(i).getNotification());

                                }
                            }

                        }
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
                            notesFromDB = db.getAllNotes();
                            for (int i = 0; i < notesFromDB.size(); i++) {
                                if (notesFromDB.get(i).getSaveDate().equals(newNote.getSaveDate())) {      //this is not a good solution, maybe we should fix it by modifying DatabaseManagement.java
                                    Intent intent = new Intent(context, NoteEditorActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, notesFromDB.get(i).getNotification().getId(), intent, 0);
                                    scheduleNotification(getNotification(notesFromDB.get(i), pendingIntent), notesFromDB.get(i).getNotification());
                                }
                            }

                        }

                        int calendarReadPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
                        int calendarWritePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);

                        if (calendarReadPermission == PERMISSION_GRANTED && calendarWritePermission == PERMISSION_GRANTED) {

                            try {       //creates a new "sandy personal assistant" calendar if device doesn't have one.
                                String calendarName = CalendarSync.getCalendarName(context);
                                System.out.println("calendar name: " +calendarName);
                                if (calendarName == null) {
                                    CalendarSync.createNewCalendar(context);
                                }
                            } catch(Exception e) {
                                System.out.println("calendar exception.");
                            }

                            if (notification != null) {         //if a new notification time is selected
                                    int numberOfRowsAffected = 0;
                                    if (editNote.getNotification() != null) {       //if edited Note has already a notification attached
                                        numberOfRowsAffected = calendarSync.updateCalendarEntry(context, db.getLastAddedNotification().getId(), newNote);       //update this calendar entry.
                                        if (numberOfRowsAffected > 0) {
                                            Toast.makeText(context, context.getResources().getString(R.string.calendar_event_updated), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {      //if edited Note has no notification attached before,
                                        notesFromDB = db.getAllNotes();
                                        for (int i = 0; i < notesFromDB.size(); i++) {
                                            if (notesFromDB.get(i).getSaveDate().equals(newNote.getSaveDate())) {      //this is not a good solution, maybe we should fix it by modifying DatabaseManagement.java
                                                calendarSync = new CalendarSync(notesFromDB.get(i).getTitle(), notesFromDB.get(i).getNotification(), notesFromDB.get(i).getContent());      //instantiate new calendarSync object
                                                calendarSync.addCalendarEventInBackground(context, calendarSync.getEventTitle(), calendarSync.getEventDescription(), calendarSync.getEventNotification());
                                                Toast.makeText(context, context.getResources().getString(R.string.calendar_event_insert_success), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.calendar_no_permission_error), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    }

                            }
                            else {      //if no new notification time selected
                                if (editNote.getNotification() != null) {       //if edited Note has already a notification attached
                                    int numberOfRowsAffected = 0;
                                    numberOfRowsAffected = calendarSync.updateCalendarEntry(context, editNote.getNotification().getId(), newNote);  //update calendar event values
                                    if (numberOfRowsAffected > 0) {
                                        Toast.makeText(context, context.getResources().getString(R.string.calendar_event_updated), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.calendar_no_permission_error), Toast.LENGTH_LONG).show();
                        }
                    }

                    notesFromDB = db.getAllNotes();
                    listOfNotes = findViewById(R.id.listOfNotes);
                    break;

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
                                editor.updateTextColor(colorHex(color));
                            }
                        });


            }
        });


        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {      //onClick listener for add image from toolbox
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);       //initialization of new intent that launches External Storage browser
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        findViewById(R.id.fab_noteeditor_options_addimage).setOnClickListener(new View.OnClickListener() {      //onClick listener for add image from action button
            @Override
            public void onClick(View v) {
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
                    int calendarReadPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
                    int calendarWritePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);

                    if (calendarReadPermission == PERMISSION_GRANTED && calendarWritePermission == PERMISSION_GRANTED) {
                        calendarSync = new CalendarSync(editNote.getTitle(), editNote.getNotification(), editNote.getContent());      //instantiate new calendarSync object
                        calendarSync.addCalendarEvent(context, calendarSync.getEventTitle(), calendarSync.getEventDescription(), calendarSync.getEventNotification());     //call method that sends Note info to Calendar API
                        closeFABMenu();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.calendar_no_permission_error), Toast.LENGTH_LONG).show();
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
                            editor.insertImage(bitmap);
                            String html = editor.getContentAsHTML();
                            System.out.println("html : " + html);
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
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
    }

    private void closeFABMenu(){        //method that makes sub-FAB menus invisible
        System.out.println("closeFABMenu");
        isFABOpen=false;
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

    public String htmlifyPlainText(String textIn) {
        SpannableString spannable = SpannableString.valueOf(textIn);
        Linkify.addLinks(spannable, Linkify.WEB_URLS);
        return Html.toHtml(spannable);
    }

    private void scheduleNotification(android.app.Notification notification, Notification n) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, n.getId());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, n.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = parseFormattedDateString(n);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        System.out.println("alarm will go off at : " + cal.getTimeInMillis());
    }

    public android.app.Notification getNotification(Note n, PendingIntent pendingIntent) {
        NotificationCompat.BigTextStyle nc;
        android.app.Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new android.app.Notification.Builder(this, "notification_channel_" + Integer.toString(n.getId()));
        }
        else {
            builder = new android.app.Notification.Builder(this);
        }
        builder.setContentTitle(n.getTitle());
        builder.setContentText(n.getTitle());
        builder.setSmallIcon(R.drawable.circle_clock);
        builder.setStyle(new android.app.Notification.BigTextStyle().bigText(Html.fromHtml(n.getContent())));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        return builder.build();
    }

    public Calendar parseFormattedDateString(Notification notification) {       //function to format date and time string to clean one and return it as calendar object
        String date_time = notification.getDate();

        String eventDate = date_time.substring(0,date_time.indexOf('T'));
        String eventTime = date_time.substring(date_time.indexOf('T') +1, date_time.indexOf('Z'));

        Integer eventYear = Integer.parseInt(eventDate.split("-")[0]);
        Integer eventMonth = Integer.parseInt(eventDate.split("-")[1]);
        Integer eventDay = Integer.parseInt(eventDate.split("-")[2]);

        Integer eventHour = Integer.parseInt(eventTime.split(":")[0]);
        Integer eventMinute = Integer.parseInt(eventTime.split(":")[1]);

        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.MONTH,eventMonth-1);
        cal.set(Calendar.YEAR,eventYear);
        cal.set(Calendar.DAY_OF_MONTH,eventDay);
        cal.set(Calendar.HOUR_OF_DAY,eventHour);
        cal.set(Calendar.MINUTE,eventMinute);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal;
    }

    public Note getEditedNote() {
        return editNote;
    }

    public void setEditedNote(Note note) {
        editNote = note;
    }





}
