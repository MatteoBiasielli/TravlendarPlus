package travlendarplus.travel;

import java.util.ArrayList;

public class Leg {
	private final String start;
	private final String end;
	private final String duration;
        
        /**seconds*/
	private final int durationInt;
	private final ArrayList<Step> steps;
	public Leg(String s, String e, String d, int i, ArrayList<Step> st){
		start=s;
		end=e;
		duration=d;
		durationInt=i;
		steps=st;
	}
	
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		String ris=start+" -> "+end+" - "+duration;
		for(Step s:steps)
			ris+="\n      " + s.toString();
		return ris+"\n";
	}
        /**@return the total duration of the leg*/
        public int getDuration(){
            return this.durationInt;
        }
        
        /**
        * @param m is the modality
        * @return the total travel time with the specified modality*/
        int getTimeByModality(TravelMode m) {
            int acc=0;
            for(Step s:steps)
                acc=acc+s.getTimeByModality(m);
            return (int)(acc/60);
        }
}
