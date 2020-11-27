package sandy.android.assistant;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;

public class Notification {
    private Integer id = 0;
    private String date = "";

    public Integer getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


}
