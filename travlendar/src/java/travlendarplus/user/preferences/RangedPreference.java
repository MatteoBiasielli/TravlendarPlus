
package travlendarplus.user.preferences;

import travlendarplus.travel.Route;
import travlendarplus.travel.TravelMode;

public class RangedPreference extends Preference{
	private RangedPreferenceType type;
	private int value;
	public RangedPreference(RangedPreferenceType t, int v){
		this.type=t;
		this.value=v;
	}
	
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return type.name()+","+value;
	}
	
	/* GETTERS */
	public RangedPreferenceType getType(){
		return type;
	}
	public int getValue(){
		return value;
	}

        @Override
        public boolean isRanged() {
            return true;
        }

        @Override
        public boolean isRespectedBy(Route r) {
            if(r.getTimeByModality(TravelMode.WALKING)<=value && type==RangedPreferenceType.WALKING_TIME_LIMIT)
                return true;
            if(r.getTimeByModality(TravelMode.DRIVING)<=value && type==RangedPreferenceType.CAR_TIME_LIMIT)
                return true;
            if(r.getTimeByModality(TravelMode.TRANSIT)<=value && type==RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT)
                return true;
            return false;
        }
}
