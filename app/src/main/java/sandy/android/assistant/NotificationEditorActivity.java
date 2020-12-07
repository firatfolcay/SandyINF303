package sandy.android.assistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class NotificationEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Notification notification = new Notification();
    DatabaseManagement db;

    String date, time;

    Button notificationCancelButton;
    Button datePickerButton;
    Button timePickerButton;
    Button saveNotificationButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_editor);

        notificationCancelButton = findViewById(R.id.notificationCancelButton);
        datePickerButton = (Button) findViewById(R.id.datePickerButton);
        timePickerButton = (Button) findViewById(R.id.timePickerButton);
        saveNotificationButton = (Button) findViewById(R.id.saveNotificationButton);

        db = new DatabaseManagement(this);


        //getting notification of the note if it has one and updates the screen
        Bundle b = new Bundle();
        if(b != null){
            if(b.get("NOTIFICATION_ID") != null){
                updateNotificationActivity(db.getNotificationFromNotificationID(b.getInt("NOTIFICATION_ID")));
            }
        }

        saveNotificationButton.setOnClickListener(new View.OnClickListener() {  //save button
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String currentDateString = date + "T" + time + ":00" + "Z";     //I use this format because others did not work

                //FIXME this currently passes back only the date not the object, makes the code a bit spaghetti. It is possible to pass the object itself but painful to do so.
                Intent data = new Intent(getApplicationContext(), NoteEditorActivity.class);
                data.putExtra("NOTIFICATION_DATE", currentDateString);
                setResult(Activity.RESULT_OK, data);
                finish();

                return;

            }

        });

        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        notificationCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //cancel button

                finish();  //finish notification selection screen if cancel button is pressed.
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) { //choosing date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView datePickerTextView = (TextView) findViewById(R.id.datePickertextView);
        datePickerTextView.setText(currentDateString);
        date = year + "-" + month + "-" + dayOfMonth;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // choosing time
        TextView textView = (TextView) findViewById(R.id.timePickertextView);

        textView.setText("Hour: " + hourOfDay + " Minute: " + minute);
        time = hourOfDay + ":" + minute;

    }

    public void updateNotificationActivity(Notification n){
        //TODO this function shall update the screen with the Notification object n passed from the NoteEditorActivity
    }
}
