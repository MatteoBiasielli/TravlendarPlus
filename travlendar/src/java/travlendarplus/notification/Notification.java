package travlendarplus.notification;

/**
 * This class represents a notification object to be used in server as auxiliary object.
 */
public class Notification {
    private Integer user_id, activity_id;
    private long timestamp;
    private String text;

    /*CONSTRUCTORS*/
    public Notification(int usr, int activity, String text, long timestamp){
        this.activity_id = activity;
        this.user_id = usr;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Notification(String text, long timestamp){
        this.activity_id = null;
        this.user_id = null;
        this.text = text;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        if (this.user_id == 0)
            return "{GENERIC - TEXT: "+text+"; TIMESTAMP: "+timestamp+"}";
        else
            return "{USER: "+user_id+"; ACTIVITY_ID: "+activity_id+"; TEXT: "+text+"; TIMESTAMP: "+timestamp+"}";
    }

    /*GETTERS*/
    public Integer getUser_id() {
        return user_id;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }
}
