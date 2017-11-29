package travlendardesktopclient.data.user.preferences;


public enum Modality {
	STANDARD(1),MINIMIZE_FOOTPRINT(2), MINIMIZE_COST(3),MINIMIZE_TIME(4);
	private int i;
	Modality(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
}
