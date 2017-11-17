package travlendarplus.user.preferences;

import travlendarplus.exceptions.UnconsistentValueException;

public enum Modality {
	STANDARD(1),MINIMIZE_FOOTPRINT(2), MINIMIZE_COST(3),MINIMIZE_TIME(4);
	private int i;
	Modality(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
	public static Modality getForValue(int val) throws UnconsistentValueException{
		for(Modality m: Modality.values())
			if(m.getValue()==val)
				return m;
		throw new UnconsistentValueException("The given value is inconsistent.");
	}
}
