package biasiellicapodifatta.travlendar.response.responseaddtag;

/**
 *
 * @author matteo
 */
public enum ResponseAddTagType {
    OK("ok"),
    ADD_TAG_LOGIN_ERROR("The login data are invalid."),
    ADD_TAG_ALREADY_EXISTING("The tag already exists."),
    ADD_TAG_WRONG_INPUT("The given input data are invalid."),
    ADD_TAG_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseAddTagType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
