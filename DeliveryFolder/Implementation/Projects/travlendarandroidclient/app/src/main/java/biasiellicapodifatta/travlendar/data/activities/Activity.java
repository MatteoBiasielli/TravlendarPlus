package biasiellicapodifatta.travlendar.data.activities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public abstract class Activity implements Comparable{
	protected Date startDate;
	protected Date endDate;
	protected String label;
	protected String notes;
	protected String locationAddress;
	protected String startPlaceAddress;
	protected ActivityStatus actStatus;
	protected int key;
	protected boolean keySet;
	
	/* *CONSTRUCTORS**/
	protected Activity(Date s, Date e,String l, String n, String lA, String sP, ActivityStatus actSt){
		startDate=s;
		endDate=e;
		this.label=l;
		this.notes=n;
		this.locationAddress=lA;
		this.startPlaceAddress=sP;
		this.actStatus=actSt;
		if(this.label==null)
			this.label=new String();
		if(this.notes==null)
			this.notes=new String();
		key=0;
		keySet=false;
	}
	protected Activity(){
		key=0;
		keySet=false;
	}
	/* **************************************************** */
	
	/**Given a calndar, verifies if the activity can be added to the calendar or not
	 *@param c is the calendar
	 *@return true if the activity doesn't make the calendar become inconsistent, false otherwise 
	 */
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		return "START="+startDate+", END="+endDate+","+label+","+notes+","+locationAddress+","+startPlaceAddress+","+actStatus.name();
	}
        
	/* ******GETTERS**********/
	public int getKey(){
		return this.key;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	public Date getEndDate(){
		return endDate;
	}
	public String getLabel(){
		return this.label;
	}
	public String getNotes(){
		return this.notes;
	}
	public String getLocationAddress(){
		return this.locationAddress;
	}
	public String getStartPlaceAddress(){
		return this.startPlaceAddress;
	}
	public ActivityStatus getStatus(){
		return this.actStatus;
	}
	public abstract boolean isFlexible();
	public abstract long getDuration();
        public abstract int getEstimatedTravelTime();

    @Override
    public int compareTo(Object o) {
        return this.startDate.compareTo(((Activity)o).startDate);
    }

    public static ArrayList<Activity> getForDate(ArrayList<Activity> allAct, int i, int i1, int i2){
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<Activity> result = new ArrayList<>();
		int currY, currM, currD;
    	String parsDate;
    	String[] parsFields;

    	for(Activity a : allAct){
    		parsDate = fm.format(a.getStartDate());
    		parsFields = parsDate.split("-", 3);
    		currY = Integer.parseInt(parsFields[0]);
    		currM = Integer.parseInt(parsFields[1]);
    		currD = Integer.parseInt(parsFields[2]);

    		if(currY == i && currM == i1 + 1 && currD == i2){
    			result.add(a);
			}
		}

		return result;
	}
}
