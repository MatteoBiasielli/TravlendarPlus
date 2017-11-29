package travlendarplus.user.preferences;

import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.travel.Route;

public enum RangedPreferenceType {
	WALKING_TIME_LIMIT(1),COST_LIMIT(2), BIKING_TIME_LIMIT(3),CAR_TIME_LIMIT(4),
	PUBLIC_TRANSPORT_TIME_LIMIT(5);
	private final int i;
	RangedPreferenceType(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
	public static RangedPreferenceType getForValue(int val) throws UnconsistentValueException{
		for(RangedPreferenceType m: RangedPreferenceType.values())
			if(m.getValue()==val)
				return m;
		throw new UnconsistentValueException("The given value is inconsistent.");
	}

        
}