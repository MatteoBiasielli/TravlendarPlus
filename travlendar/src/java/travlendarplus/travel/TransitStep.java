package travlendarplus.travel;

public class TransitStep extends Step{
	private int nStops;
	private String arrivalStation;
	public TransitStep(String dt, int dv, String tt, int tv, String i, int nStops, String arrivalStation) {
		super(dt, dv, tt, tv, i);
		this.nStops=nStops;
		this.arrivalStation=arrivalStation;
	}
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		return super.toString()+" -> "+arrivalStation+ " ("+nStops+" stops) ";
	}
}
