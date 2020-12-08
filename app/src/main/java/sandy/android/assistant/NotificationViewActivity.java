package sandy.android.assistant;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationViewActivity extends AppCompatActivity {

    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotifications;
    ArrayList<Note> notes;
    ArrayList<Note> notesWithNotification;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_recycler_view);

        db = new DatabaseManagement(this);

        listOfNotifications = findViewById(R.id.listOfNotifications);

        notes = db.getAllNotes();
        notesWithNotification = new ArrayList<Note>();
        int index = 0;
        for (index = 0; index < notes.size(); index++) {
            if (notes.get(index).getNotification() != null) {
                notesWithNotification.add(notes.get(index));
            }
        }

        notificationAdapter = new NotificationAdapter(this, notesWithNotification, db);
        listOfNotifications.setAdapter(notificationAdapter);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotifications.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        notes = db.getAllNotes();

        int index = 0;
        for (index = 0; index < notes.size(); index++) {
            if (notes.get(index).getNotification() != null) {
                notesWithNotification.add(notes.get(index));
            }
        }

        notificationAdapter = new NotificationAdapter(this, notesWithNotification, db);
        listOfNotifications.setAdapter(notificationAdapter);
    }
}
