/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.response.responsetravel;

import java.util.ArrayList;
import travlendarplus.travel.Route;

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
