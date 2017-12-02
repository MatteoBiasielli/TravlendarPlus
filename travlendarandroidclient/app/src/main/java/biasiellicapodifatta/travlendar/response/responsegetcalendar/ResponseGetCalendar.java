package biasiellicapodifatta.travlendar.response.responsegetcalendar;



/**
 * Modified by mattiadifatta on 20/11/2017.
 */
public class ResponseGetCalendar {
    private ResponseGetCalendarType rgct;
    private Calendar cal;

    public ResponseGetCalendar(ResponseGetCalendarType type, Calendar cal){
        this.rgct = type;
        this.cal = cal;
    }

    public ResponseGetCalendarType getRgct() {
        return rgct;
    }

    public Calendar getCal() {
        return cal;
    }
}
