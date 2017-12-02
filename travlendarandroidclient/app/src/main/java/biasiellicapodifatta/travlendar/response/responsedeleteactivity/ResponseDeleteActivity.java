package biasiellicapodifatta.travlendar.response.responsedeleteactivity;

import biasiellicapodifatta.travlendar.data.activities.Calendar;

/**
 * Modified by mattiadifatta on 19/11/2017.
 */
public class ResponseDeleteActivity {
    private ResponseDeleteActivityType rdat;
    private Calendar cal;

    public ResponseDeleteActivity(ResponseDeleteActivityType type, Calendar c){
        this.rdat = type;
        this.cal = c;
    }

    public ResponseDeleteActivityType getRdat() {
        return rdat;
    }

    public Calendar getCal() {
        return cal;
    }
}
