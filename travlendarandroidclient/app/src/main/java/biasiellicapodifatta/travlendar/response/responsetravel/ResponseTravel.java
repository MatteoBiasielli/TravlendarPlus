package biasiellicapodifatta.travlendar.response.responsetravel;

/**
 *
 * @author matteo
 */
public class ResponseTravel {
    private final ArrayList<Route> routes;
    private final ResponseTravelType type;
    
    public ResponseTravel(ArrayList<Route> routes,ResponseTravelType t){
        this.routes=routes;
        this.type=t;
    }
    public ArrayList<Route> getRoutes(){
        return routes;
    } 
    public ResponseTravelType getType(){
        return type;
    }
}
