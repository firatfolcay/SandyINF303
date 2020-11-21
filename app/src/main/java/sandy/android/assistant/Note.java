package main.java.sandy.android.assistant;

import java.time.LocalDateTime;
import java.io.File;

public class Note {
    private String title = "";
    private Notification notification;
    private int id = 0;

    //FIXME should these be in a wrapper class for easier management? (something like: private contents List<Element<String>>)
    private String text = "";
    private File image;             // an Image class doesn't exist, java.io.File is probably what we need

    // Datetime of when the note was saved
    private LocalDateTime saveDate = null;  //FIXME LocalDateTime doesn't have a time zone attached, should probably be changed

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }
}