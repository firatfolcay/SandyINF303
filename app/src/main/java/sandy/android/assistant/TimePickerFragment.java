package sandy.android.assistant;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.*;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private Integer hour;
    private Integer minute;

    public TimePickerFragment(){

    }

    public TimePickerFragment(Integer hour, Integer minute){
        this.hour = hour;
        this.minute = minute;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(hour == null || minute == null) {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener)getActivity(),hour,minute, DateFormat.is24HourFormat(getActivity()));

        return timePickerDialog;
    }

}
