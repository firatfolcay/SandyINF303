package sandy.android.assistant;

import android.annotation.SuppressLint;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.io.File;
import java.util.ArrayList;

public class Note {
    private int id = 0;
    private String title = "";
    private String content = "";
    private Notification notification;
    private String saveDate = null;
    private ArrayList<Note> notesArrayList = new ArrayList<Note>();

    public Note(){
    }

    public Note(String title, String content, Notification notification, String date){
        setTitle(title);
        this.content = content;
        this.notification = notification;
        this.saveDate = date;
    }

    public Note(int id, String title, String content, Notification notification, String date){
        this.id = id;
        setTitle(title);
        this.content = content;
        this.notification = notification;
        this.saveDate = date;
    }

    public ArrayList<Note> getNotesArrayList() {
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<Note> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null){
            title = "";
        }
        this.title = title;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}