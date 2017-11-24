package travlendarplus.notification;

/**
 * Modified by mattiadifatta on 23/11/2017.
 */
public class Notification {
    private int user_id, activity_id;
    private long timestamp;
    private String text;

    /*CONSTRUCTOR*/
    public Notification(int usr, int activity, String text, long timestamp){
        this.activity_id = activity;
        this.user_id = usr;
        this.text = text;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{USER: "+user_id+"; ACTIVITY_ID: "+activity_id+"; TEXT: "+text+"; TIMESTAMP: "+timestamp+"}";
    }
}
