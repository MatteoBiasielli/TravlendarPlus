package biasiellicapodifatta.travlendar.response.responselogin;

import biasiellicapodifatta.travlendar.user.User;

/**
 *
 * @author matteo
 */
public class ResponseLogin{
    private final ResponseLoginType rlt;
    private final User u;
    public ResponseLogin(ResponseLoginType r, User u){
        this.rlt=r;
        this.u=u;
    }
    public ResponseLoginType getType(){
        return rlt;
    }
    public User getUser(){
        return u;
    }
}
