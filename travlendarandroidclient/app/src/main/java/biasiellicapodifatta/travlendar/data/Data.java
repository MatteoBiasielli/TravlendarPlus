package biasiellicapodifatta.travlendar.data;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

import biasiellicapodifatta.travlendar.data.user.FavouritePosition;
import biasiellicapodifatta.travlendar.data.user.User;

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
