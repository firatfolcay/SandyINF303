package sandy.android.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {

    final int callbackId = 22;
    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager notebooksLinearLayoutManager;
    ConstraintLayout mainActivityConstraintLayout;
    RecyclerView listOfNotes;
    RecyclerView listOfNotebooks;
    NavigationView notebookNavigationView;
    ImageView mainActivityNavigationViewImageView;
    FloatingActionButton fab_create_new_note;
    ImageView buttonShowNotification;

    View popupView;     //notebook popup attributes
    EditText notebookPopupEditText;
    Button notebookPopupCreateButton;
    Button notebookPopupCancelButton;
    LayoutInflater inflater;
    PopupWindow popupWindow;

    Button buttonAddNotebook;
    ArrayList<Note> notes;
    ArrayList<Notebook> notebooks;
    NoteAdapter noteAdapter;
    MainActivityNavigationDrawerAdapter mainActivityNavigationDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent calendarNotificationListenerService = new Intent(this, CalendarNotificationListenerService.class);
        this.startService(calendarNotificationListenerService);*/

        //checkNotificationListenerPermission();
        checkNotificationListenerServicePermissions();

        Intent screenOffBroadcastService = new Intent(this, BroadcastReceiverScreenOffService.class);
        this.startService(screenOffBroadcastService);

        Intent screenOnBroadcastService = new Intent(this, BroadcastReceiverScreenOnService.class);
        this.startService(screenOnBroadcastService);

        Intent timeTickBroadcastService = new Intent(this, BroadcastReceiverTimeTickService.class);
        this.startService(timeTickBroadcastService);

        Intent calendarNotificationListenerService = new Intent(this, CalendarNotificationListenerService.class);
        this.startService(calendarNotificationListenerService);

        createNotificationChannel();
        //calls function that grants permissions for device interactions.
        checkPermission(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);     //calendar permission check

        db = new DatabaseManagement(this);

        fab_create_new_note = (FloatingActionButton) findViewById(R.id.fab_create_new_note);

        mainActivityConstraintLayout = (ConstraintLayout) findViewById(R.id.mainActivityConstraintLayout);
        mainActivityNavigationViewImageView = (ImageView) findViewById(R.id.buttonMainActivityNavigationDrawer);
        notebookNavigationView = (NavigationView) findViewById(R.id.mainActivityNavigationView);

        buttonShowNotification = findViewById(R.id.showNotification);
        buttonAddNotebook = findViewById(R.id.buttonAddNotebook);

        listOfNotes = findViewById(R.id.listOfNotes);
        listOfNotebooks = findViewById(R.id.mainActivityListOfNotebooks);


        notes = db.getAllNotes();
        noteAdapter = new NoteAdapter(this, notes, db, this);       //create new Adapter to fetch Notes from DB and to show them in Cardview inside Recycleview
        listOfNotes.setAdapter(noteAdapter);

        notebooks = db.getAllNotebooks();
        mainActivityNavigationDrawerAdapter = new MainActivityNavigationDrawerAdapter(this, notebooks, db);
        listOfNotebooks.setAdapter(mainActivityNavigationDrawerAdapter);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Notes Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotes.setLayoutManager(linearLayoutManager);

        notebooksLinearLayoutManager = new LinearLayoutManager(this);
        notebooksLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotebooks.setLayoutManager(notebooksLinearLayoutManager);

        // Unused variables ??
        ConstraintLayout mainActivityConstraintLayout = findViewById(R.id.mainActivityConstraintLayout);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        buttonAddNotebook.setOnClickListener(new View.OnClickListener() {           //if add notebook button is clicked
            @Override
            public void onClick(View view) {
                // inflate the layout of the popup window
                inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                popupView = inflater.inflate(R.layout.notebook_popup_view, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                notebookPopupEditText = (EditText) popupView.findViewById(R.id.notebookPopupEditText);              //initialize popup windows views
                notebookPopupCreateButton = (Button) popupView.findViewById(R.id.notebookPopupCreateButton);
                notebookPopupCancelButton = (Button) popupView.findViewById(R.id.notebookPopupCancelButton);

                notebookPopupCreateButton.setOnClickListener(new View.OnClickListener() {       //onClick listener for notebook popup create button
                    @Override
                    public void onClick(View v) {
                        if (!notebookPopupEditText.getText().toString().equals("")) {           //if notebook title input isn't empty
                            if (notebookPopupEditText.getText().toString().length() <= 10) {
                                Notebook newNotebook = new Notebook(notebookPopupEditText.getText().toString());
                                db.insertNotebook(newNotebook);         //insert a new notebook into database with input title
                                notebookNavigationView.setVisibility(View.INVISIBLE);       //close navigation view
                                refreshNotebookNavigationView();        //refresh navigation view
                                popupWindow.dismiss();                  //dispose popup
                            }
                            else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notebook_title_char_limit_error), Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

                notebookPopupCancelButton.setOnClickListener(new View.OnClickListener(){            //if notebook popup cancel button is clicked
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();          //dispose popup
                    }
                });
            }
        });

        buttonShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationViewActivity.class);
                startActivity(intent);
            }
        });

        fab_create_new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorActivity.java
                startActivity(intent);
            }
        });

        listOfNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notebookNavigationView.getVisibility() == View.VISIBLE) {
                    notebookNavigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        listOfNotes.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeRight() {
                if (notebookNavigationView.getVisibility() == View.INVISIBLE) {
                    notebookNavigationView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSwipeLeft() {
                if (notebookNavigationView.getVisibility() == View.VISIBLE) {
                    notebookNavigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mainActivityConstraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeRight() {
                if (notebookNavigationView.getVisibility() == View.INVISIBLE) {
                    notebookNavigationView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSwipeLeft() {
                if (notebookNavigationView.getVisibility() == View.VISIBLE) {
                    notebookNavigationView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mainActivityNavigationViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notebookNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        notes = db.getAllNotes();
        noteAdapter = new NoteAdapter(this, notes, db, this);
        listOfNotes.setAdapter(noteAdapter);

        refreshNotebookNavigationView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_layout_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.linearViewVertical:
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                listOfNotes.setLayoutManager(linearLayoutManager);
                break;

            case R.id.gridView:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                listOfNotes.setLayoutManager(gridLayoutManager);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                listOfNotes.setLayoutManager(staggeredGridLayoutManager);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {       //overrides back button behavior
        if (notebookNavigationView.getVisibility() == View.VISIBLE) {       //if navigation drawer is opened
            notebookNavigationView.setVisibility(View.INVISIBLE);           //simply close it
        } else {
            super.onBackPressed();      //if navigation drawer is already closed, apply primary activity behavior
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("application closed.");

    }



    public void startNoteEditorActivity () {
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);        //creates new intent that opens up note_editor.xml screen and runs NoteEditorActivity.java
        startActivity(intent);
    }

    public void refreshNotebookNavigationView () {
        notebooks = db.getAllNotebooks();
        mainActivityNavigationDrawerAdapter = new MainActivityNavigationDrawerAdapter(this, notebooks, db);
        listOfNotebooks.setAdapter(mainActivityNavigationDrawerAdapter);

    }

    private void checkPermission(int callbackId, String... permissionsId) {     //checks if selected permissions exist, requests if missed.
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.BLUE);
            //channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            System.out.println("notification channel name: " + notificationManager.getNotificationChannel(channel.getId()).getId());
        }
    }

    private void checkNotificationListenerServicePermissions() {
        if(isPermissionRequired()){
            requestNotificationPermission();
        }
    }

    public boolean isPermissionRequired() {
        ComponentName cn = new ComponentName(this, CalendarNotificationListenerService.class);
        String flat = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return !enabled;
    }

    private void requestNotificationPermission() {
        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivityForResult(intent, 101);
    }

    public void checkNotificationListenerPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
                != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("permission denied");
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        else {
            System.out.println("permission granted");
        }
    }
}
