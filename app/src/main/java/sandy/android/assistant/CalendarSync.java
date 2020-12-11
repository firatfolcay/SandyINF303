package sandy.android.assistant;

public class CalendarSync {
    String eventTitle = "";
    String eventDate = "";
    String eventDescription = "";

    public CalendarSync (String eventTitle, String eventDate, String eventDescription) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String Date) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
