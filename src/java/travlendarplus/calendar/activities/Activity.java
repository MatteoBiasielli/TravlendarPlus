package travlendarplus.calendar.activities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import travlendarplus.calendar.Calendar;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.NoPathFoundException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.exceptions.UnsopportedOperationException;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;
import travlendarplus.user.User;

public abstract class Activity {
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
	public abstract boolean canBeAddedTo(Calendar c);
	public abstract ResponseAddActivityNotification generateRequiredNotification(Calendar c);
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		return "START="+startDate+", END="+endDate+","+label+","+notes+","+locationAddress+","+startPlaceAddress+","+actStatus.name();
	}
	
	/**Allows to set the parameter key. Parameter key is meant to contain the key_id value of the tuple containing this activity in the database.
	 * After setting the key, param keySet is automatically set to true and it's impossible to set the key again (= it becomes read only). 
	 * This is generally used by the data layer.
	 * @param nkey is the key to insert
	 */
	public void setKey(int nkey){
		if(keySet)
			throw new UnsopportedOperationException("Cannot modify the key.");
		this.key=nkey;
		this.keySet=true;
	}
	
        /**
         * Verifies if a fixed activity and a break overlap
         * @param fAct is the fixed activity
         * @param br is the break
         * @return true if the two activities overlap, false otherwise
         */
        public static boolean fixedBreakOverlap(FixedActivity fAct, Break br){
            return (br.getStartDate().after(fAct.startDate) || br.getStartDate().equals(fAct.startDate))
                                    && br.getStartDate().before(fAct.endDate) ||
                                    br.getEndDate().after(fAct.startDate) && 
                                    (br.getEndDate().before(fAct.endDate) || br.getEndDate().equals(fAct.endDate)) 
                                        || (br.getStartDate().before(fAct.startDate) ||  br.getStartDate().equals(fAct.startDate))
                                            && (br.getEndDate().after(fAct.endDate) ||  br.getEndDate().equals(fAct.endDate));
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
        public abstract int calculateEstimatedTravelTime(String tagStart, String tagLoc, User u) throws IOException, InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException, InvalidPositionException;
}
