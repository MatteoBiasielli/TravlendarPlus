package biasiellicapodifatta.travlendar.data.user.preferences;


public enum Modality {
	STANDARD(1),MINIMIZE_FOOTPRINT(2), MINIMIZE_COST(3),MINIMIZE_TIME(4);
	private int i;
	Modality(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
	public static Modality getModalityfor(int m){
		switch(m){
			case 1:
				return Modality.STANDARD;
			case 2:
				return Modality.MINIMIZE_FOOTPRINT;
			case 3:
				return Modality.MINIMIZE_COST;
			case 4:
				return Modality.MINIMIZE_TIME;
		}
	}
}
