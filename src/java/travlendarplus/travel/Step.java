package travlendarplus.travel;

public class Step {
	private final String distanceText;
	private final int distanceValue;
        
        /**seconds*/
	private final int timeValue;
	private final String timeText;
	private String instruction;
        private TravelMode mode;
	public Step(String dt, int dv, String tt, int tv,String i){
		this.distanceText=dt;
		this.distanceValue=dv;
		this.timeText=tt;
		this.timeValue=tv;
		this.instruction=i;
	}
	
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		return instruction;
	}
        
        public void setMode(TravelMode m){
            this.mode=m;
        }
        
        /**
        * @param m is the modality
        * @return the total travel time with the specified modality if it 
        * corresponds to the caller object's travel modality. return 0 
        * otherwise*/
        int getTimeByModality(TravelMode m) {
            if(mode==m)
                return timeValue;
            return 0;
        }
}
