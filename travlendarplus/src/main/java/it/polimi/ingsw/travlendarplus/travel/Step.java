package it.polimi.ingsw.travlendarplus.travel;

public class Step {
	private String distanceText;
	private int distanceValue;
	private int timeValue;
	private String timeText;
	private String instruction;
	public Step(String dt, int dv, String tt, int tv,String i){
		this.distanceText=dt;
		this.distanceValue=dv;
		this.timeText=tt;
		this.timeValue=tv;
		this.instruction=i;
	}
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return instruction;
	}
}
