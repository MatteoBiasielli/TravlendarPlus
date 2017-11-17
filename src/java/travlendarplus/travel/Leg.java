package travlendarplus.travel;

import java.util.ArrayList;

public class Leg {
	private String start;
	private String end;
	private String duration;
	private int durationInt;
	private ArrayList<Step> steps;
	public Leg(String s, String e, String d, int i, ArrayList<Step> st){
		start=s;
		end=e;
		duration=d;
		durationInt=i;
		steps=st;
	}
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		String ris=start+" -> "+end+" - "+duration;
		for(Step s:steps)
			ris+="\n      " + s.toString();
		return ris+"\n";
	}
}
