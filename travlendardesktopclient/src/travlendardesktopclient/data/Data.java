/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.data;

import java.util.ArrayList;
import travlendardesktopclient.data.user.FavouritePosition;
import travlendardesktopclient.data.user.User;

/**
 *
 * @author matteo
 */
public class Data {
    private static User u;
    
    public static User getUser(){
        return u;
    }
    public static void setUser(User u){
        Data.u=u;
    }
    public static void setFavPlaces(ArrayList<FavouritePosition> fp){
        Data.u.setFavPositions(fp);
    }
}
