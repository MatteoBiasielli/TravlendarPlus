package biasiellicapodifatta.travlendar.response.responsedeletetag;

/**
 *
 * @author matteo
 */
public enum ResponseDeleteTagType {
    OK("ok"),
    DELETE_TAG_LOGIN_ERROR("The login data are invalid."),
    DELETE_TAG_WRONG_INPUT("The given input data are invalid."),
    DELETE_TAG_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseDeleteTagType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
