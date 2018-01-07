package biasiellicapodifatta.travlendar.response.responsedeletetag;

import java.util.ArrayList;

import biasiellicapodifatta.travlendar.data.user.FavouritePosition;

/**
 *
 * @author matteo
 */
public class ResponseDeleteTag {
    ResponseDeleteTagType rdtt;
    ArrayList<FavouritePosition> favPos;
    public ResponseDeleteTag(ResponseDeleteTagType respType,ArrayList<FavouritePosition> favPos){
        this.rdtt=respType;
        this.favPos=favPos;
    }
    public ResponseDeleteTagType getType(){
        return this.rdtt;
    }
    public ArrayList<FavouritePosition> getPositions(){
        return this.favPos;
    }
}
