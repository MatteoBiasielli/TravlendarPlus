package biasiellicapodifatta.travlendar.response.responseretrievenotifications;

/**
 * Modified by mattiadifatta on 24/11/2017.
 */
public enum ResponseRetrieveNotificationsType {
    OK("ok"),
    RETRIEVE_NOTIFICATIONS_LOGIN_ERROR("The login data are invalid."),
    RETRIEVE_NOTIFICATIONS_WRONG_INPUT("The given input data are invalid."),
    RETRIEVE_NOTIFICATIONS_CONN_ERROR("We had a connection problem, please retry soon.");

    private final String message;

    ResponseRetrieveNotificationsType(String s){
        this.message = s;
    }

    public String getMessage(){
        return message;
    }
}
