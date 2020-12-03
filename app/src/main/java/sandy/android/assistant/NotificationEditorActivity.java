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

public class NotificationEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    Notification notification = new Notification();


    String date,time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_editor);
        Button buttonMainActivity = findViewById(R.id.returnMain);
        Button datePickerButton = (Button) findViewById(R.id.datePickerButton);
        Button timePickerButton = (Button) findViewById(R.id.timePickerButton);
        Button saveNotificationButton = (Button) findViewById(R.id.saveNotificationButton);



        saveNotificationButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {//first get all date infos from other classes
                String[] dates = date.split("-");
                String[] times = time.split("-");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,Integer.parseInt(dates[0]));
                calendar.set(Calendar.MONTH,Integer.parseInt(dates[1]));
                calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dates[2]));
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(times[0]));
                calendar.set(Calendar.MINUTE,Integer.parseInt(times[1]));
                calendar.set(Calendar.SECOND,0);
                System.out.println(calendar.getTime());
                //LocalDateTime a = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
                //notification.setDate(""+a);
                //notification.setDate(String.valueOf(calendar.getTime()));

            }
        });
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView datePickertextView = (TextView)findViewById(R.id.datePickertextView);
        datePickertextView.setText(currentDateString);
        date = year+"-"+month+"-"+dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.timePickertextView);
        textView.setText("Hour: "+hourOfDay+" Minute: "+minute);
        time = hourOfDay+"-"+minute;


    }
}
