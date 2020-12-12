package sandy.android.assistant;

import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotebookActivity extends AppCompatActivity {

    DatabaseManagement db;
    LinearLayoutManager linearLayoutManager;
    RecyclerView listOfNotebooks;
    ArrayList<Notebook> notebookArrayList;
    MainActivityNavigationDrawerAdapter mainActivityNavigationDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_recycler_view);

        db = new DatabaseManagement(this);

        listOfNotebooks = findViewById(R.id.listOfNotebooks);

        linearLayoutManager = new LinearLayoutManager(this);        //defines LinearLayoutManager for vertical Recycleview orientation
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOfNotebooks.setLayoutManager(linearLayoutManager);

    }

    /*@Override
    protected void onResume() {
        super.onResume();

        notebookArrayList = db.getAllNotebooks();

        NotebookAdapter = new NotificationAdapter(this, notebookArrayList, db);
        listOfNotifications.setAdapter(notificationAdapter);
    }*/
}
