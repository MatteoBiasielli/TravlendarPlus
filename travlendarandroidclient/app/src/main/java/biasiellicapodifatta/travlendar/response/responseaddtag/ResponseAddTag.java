package biasiellicapodifatta.travlendar.response.responseaddtag;


import java.util.ArrayList;

import biasiellicapodifatta.travlendar.user.FavouritePosition;

/**
 *
 * @author matteo
 */
public class ResponseAddTag {
    ResponseAddTagType ratt;
    ArrayList<FavouritePosition> favPos;
    public ResponseAddTag(ResponseAddTagType respType,ArrayList<FavouritePosition> favPos){
        this.ratt=respType;
        this.favPos=favPos;
    }
    public ResponseAddTagType getType(){
        return this.ratt;
    }
    public ArrayList<FavouritePosition> getPositions(){
        return this.favPos;
    }
}
