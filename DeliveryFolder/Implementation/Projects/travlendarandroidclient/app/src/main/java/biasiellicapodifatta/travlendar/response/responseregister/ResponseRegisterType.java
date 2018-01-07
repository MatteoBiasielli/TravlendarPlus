package biasiellicapodifatta.travlendar.response.responseregister;

/**
 *
 * @author matteo
 */
public enum ResponseRegisterType {
    OK("ok"),
    REGISTER_USERNAME_ERROR("The given username already exists."),
    REGISTER_WRONG_INPUT("The given input data are invalid."),
    REGISTER_CONNECTION_ERROR("We had a connection problem, please retry soon.");
    private final String message;
    
    ResponseRegisterType(String s){
        this.message=s;
    }
    public String getMessage(){
        return message;
    }
}
