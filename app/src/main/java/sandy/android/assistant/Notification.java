package sandy.android.assistant;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Notification {
    private Integer id=0;
    private Date date=new Date(1999,9,7);

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
    public void setDate(String date) throws ParseException {

        //date = "2020-12-3T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //dont ask
        try {
            this.date = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
