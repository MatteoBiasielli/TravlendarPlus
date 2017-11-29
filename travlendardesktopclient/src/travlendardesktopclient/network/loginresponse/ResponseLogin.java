/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.loginresponse;

import travlendardesktopclient.data.user.User;




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
