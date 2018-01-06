package travlendarplus.user;

public enum RegistrationStatus {
	REGULAR(1),EMAIL_NOT_CONFIRMED(3), SUSPENDED(2);
	private int i;
	RegistrationStatus(int i){
		this.i=i;
	}
	public int getValue(){
		return i;
	}
}
