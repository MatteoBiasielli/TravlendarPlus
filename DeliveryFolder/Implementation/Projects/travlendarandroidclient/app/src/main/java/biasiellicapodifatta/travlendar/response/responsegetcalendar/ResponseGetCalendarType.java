package biasiellicapodifatta.travlendar.response.responsegetcalendar;

/**
 * Modified by mattiadifatta on 20/11/2017.
 */
public enum ResponseGetCalendarType {
    OK("ok"),
    GET_CALENDAR_LOGIN_ERROR("The login data are invalid."),
    GET_CALENDAR_WRONG_INPUT("The given input data are invalid."),
    GET_CALENDAR_CONN_ERROR("We had a connection problem, please retry soon");

    private final String message;

    ResponseGetCalendarType(String s){
        this.message = s;
    }

    public String getMessage(){
        return this.message;
    }
}
