package biasiellicapodifatta.travlendar.response.responselogin;

/**
 *
 * @author matteo
 */
public enum ResponseLoginType {  
    OK("ok"),
    LOGIN_USERNAME_ERROR("The given username does not exist."),
    LOGIN_PASSWORD_ERROR("Wrong password"),
    LOGIN_WRONG_INPUT("The given input data are invalid."),
    LOGIN_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseLoginType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
