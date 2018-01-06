/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network.registerresponse;

/**
 *
 * @author matteo
 */
public class ResponseRegister {
    private final ResponseRegisterType rrt;
    public ResponseRegister(ResponseRegisterType r){
        this.rrt=r;
    }
    public ResponseRegisterType getType(){
        return rrt;
    }
}
