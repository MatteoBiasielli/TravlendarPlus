package it.polimi.ingsw.travlendarplus.calendar.activities;

import it.polimi.ingsw.travlendarplus.exceptions.UnconsistentValueException;
import it.polimi.ingsw.travlendarplus.user.preferences.Modality;

public enum ActivityStatus {
	NOT_STARTED(1),TERMINATED(3), ON_GOING(2);
	private int i;
	ActivityStatus(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
	public static ActivityStatus getForValue(int val) throws UnconsistentValueException{
		for(ActivityStatus m: ActivityStatus.values())
			if(m.getValue()==val)
				return m;
		throw new UnconsistentValueException("The given value is inconsistent.");
	}
}
