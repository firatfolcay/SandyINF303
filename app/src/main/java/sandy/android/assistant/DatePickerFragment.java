package sandy.android.assistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private Integer year;
    private Integer month;
    private Integer day;

    public DatePickerFragment(){

    }

    public DatePickerFragment(Integer year, Integer month, Integer day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(year == null || month == null || day == null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(), year,month,day);
    }
}
