package biasiellicapodifatta.travlendar.response.responseregister;

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
