package sandy.android.assistant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class NotificationEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Notification notification = new Notification();
    DatabaseManagement db;

    String date, time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_editor);
        Button buttonMainActivity = findViewById(R.id.returnMain);
        Button datePickerButton = (Button) findViewById(R.id.datePickerButton);
        Button timePickerButton = (Button) findViewById(R.id.timePickerButton);
        Button saveNotificationButton = (Button) findViewById(R.id.saveNotificationButton);
        db = new DatabaseManagement(this);



        saveNotificationButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {                               //save button
                //String dates = date+"-"+time;
                //String[] date = dates.split("-");
                /*Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,Integer.parseInt(date[0]));
                calendar.set(Calendar.MONTH,Integer.parseInt(date[1]));
                calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[2]));
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(date[3]));
                calendar.set(Calendar.MINUTE,Integer.parseInt(date[4]));
                calendar.set(Calendar.SECOND,0);*/
                String currentDateString = date + "T" + time + ":00" + "Z";     //I use this format because others did not work


                try {
                    notification.setDate(currentDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                System.out.println(notification.getDate());
                db.insertNotification(notification);
                System.out.println(db.getLastAddedNotificationId());

                NotificationEditorActivity.this.onBackPressed();


                //notification.setDate(""+a);
                //notification.setDate(String.valueOf(calendar.getTime()));

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

        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //cancel button

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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

        TextView datePickertextView = (TextView) findViewById(R.id.datePickertextView);
        datePickertextView.setText(currentDateString);
        date = year + "-" + month + "-" + dayOfMonth;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // choosing time
        TextView textView = (TextView) findViewById(R.id.timePickertextView);

        textView.setText("Hour: " + hourOfDay + " Minute: " + minute);
        time = hourOfDay + ":" + minute;


    }
}
