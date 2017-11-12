package it.polimi.ingsw.travlendarplus.travel;

import java.util.ArrayList;



public class Route {
	private TravelMode mode;
	private ArrayList<Leg> legs;
	public Route(){
		legs=new ArrayList<>();
	}
	public ArrayList<Leg> getLegs(){
		return legs;
	}
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		String out=mode.toString()+"\n";
		for(Leg l:legs){
			out=out+l.toString()+"\n";
		}
		return out;	
	}
	public void setMode(TravelMode m){
		mode=m;
	}
}
