package travlendardesktopclient.network.deleteactivityresponse;

/**
 * Modified by mattiadifatta on 19/11/2017.
 */
public enum ResponseDeleteActivityType {
    OK("ok"),
    DELETE_ACTIVITY_LOGIN_ERROR("The login data are invalid."),
    DELETE_ACTIVITY_WRONG_INPUT("The given input data are invalid."),
    DELETE_ACTIVITY_CONN_ERROR("We had a connection problem, please retry soon.");

    private final String message;

    ResponseDeleteActivityType(String s){
        this.message = s;
    }

    public String getMessage(){
        return this.message;
    }
}
