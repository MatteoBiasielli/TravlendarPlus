/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.loginresponse;

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
