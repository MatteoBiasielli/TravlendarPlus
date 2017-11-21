package travlendarplus.travel;

import java.util.ArrayList;
import travlendarplus.user.preferences.Preference;



public class Route {
	private TravelMode mode;
	private final ArrayList<Leg> legs;
	public Route(){
		legs=new ArrayList<>();
	}
	public ArrayList<Leg> getLegs(){
		return legs;
	}
	
	/**@return a string human-readable version of the Object**/
        @Override
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
        
        /**
         * 
         * @return the total duration of the route
         */
        public int getDuration(){
            int acc=0;
            for(Leg l:legs)
                acc=acc+l.getDuration();
            return (int)(acc/60);
        }
        
        /**
        * @param m is the modality
        * @return the total travel time with the specified modality*/
        public int getTimeByModality(TravelMode m){
            int acc=0;
            for(Leg l:legs)
                acc=acc+l.getTimeByModality(m);
            return (int)(acc/60);
        }

    public boolean respects(ArrayList<Preference> preferences) {
        for(Preference p:preferences)
            if(!p.isRespectedBy(this))
                return false;
        return true;    
    }
}
