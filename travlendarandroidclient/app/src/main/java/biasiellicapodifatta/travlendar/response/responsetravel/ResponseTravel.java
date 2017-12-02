package biasiellicapodifatta.travlendar.response.responsetravel;

import java.util.ArrayList;

import biasiellicapodifatta.travlendar.data.travel.Route;

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
