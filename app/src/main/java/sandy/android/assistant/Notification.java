package sandy.android.assistant;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Notification {
    private int id; //this one better not have any default value; if it must, define it -1 or something negative
    private Date date;

    public Notification(){
        date = new Date();
    }

    public Integer getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDate() {
        return date.toString();
    }

    @SuppressLint("NewApi")
    //min. API Level_26 required for parse function
    public void setDate(String date){

        //date = "2020-12-3T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //dont ask

        //if the exception is handled here then there is no need for "throws" statement
        try {
            this.date = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
