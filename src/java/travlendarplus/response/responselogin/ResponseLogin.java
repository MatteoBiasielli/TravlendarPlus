/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responselogin;

import travlendarplus.response.Response;
import travlendarplus.user.*;

/**
 *
 * @author matteo
 */
public class ResponseLogin extends Response{
    private final ResponseLoginType rlt;
    private final User u;
    ResponseLogin(ResponseLoginType r, User u){
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
