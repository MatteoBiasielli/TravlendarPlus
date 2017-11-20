package travlendarplus.calendar.activities;

import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.user.preferences.Modality;

public enum ActivityStatus {
	NOT_STARTED(1),TERMINATED(3), ON_GOING(2);
	private int i;
	ActivityStatus(int i){
		this.i=i;
	}
        /**
         * 
         * @return the value associated to the object 
         */
	public int getValue(){
		return i;
	}
        /**
         * 
         * @param val the value for which the corresponding object is looked for
         * @return ActivityStatus object required
         * @throws UnconsistentValueException  if the value doesn't correspond to any ActivityStatus object
         */
	public static ActivityStatus getForValue(int val) throws UnconsistentValueException{
		for(ActivityStatus m: ActivityStatus.values())
			if(m.getValue()==val)
				return m;
		throw new UnconsistentValueException("The given value is inconsistent.");
	}
}
