package travlendarplus.calendar.activities;

import java.util.*;

import travlendarplus.calendar.Calendar;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;

public class FixedActivity extends Activity{
    
	/**MINUTES*/
	private int estimatedTravelTime;
	/* ****************CONSTRUCTORS**********************/
	/**
	 * @param s is the start date
	 * @param e is the ending date
	 */
	public FixedActivity(Date s, Date e,String l, String n, String lA, String sP, ActivityStatus actSt){
		super(s,e,l,n,lA,sP,actSt);
                this.estimatedTravelTime=0;
	}
	/**
	 * @param fa is the FixedActivity to copy
	 */
	public FixedActivity(FixedActivity fa){
		this.startDate=new Date(fa.startDate.getTime());
		this.endDate=new Date(fa.endDate.getTime());
		this.label=new String(fa.label);
		this.notes=new String(fa.notes);
		this.locationAddress=new String(fa.locationAddress);
		this.startPlaceAddress=new String(fa.startPlaceAddress);
		this.actStatus=fa.actStatus;
		this.key=fa.key;
		this.keySet=fa.keySet;
                this.estimatedTravelTime=fa.estimatedTravelTime;
	}
	
	/*************************************************/
	
	/**
	 * @param a is a fixed activity to compare with the caller object
	 * @return true if the callers's ending time comes before or is equal to
	 * the parameters's starting time. false otherwise
	 */
	public boolean isBefore(FixedActivity a){
		return endDate.before(a.startDate) || endDate.equals(a.startDate);
	}
	
	/**
	 * @param a is a fixed activity to compare with the caller object
	 * @return true if the callers's starting time comes after or is equal to
	 * the parameters's ending time. false otherwise
	 */
	public boolean isAfter(FixedActivity a){
		return this.startDate.after(a.endDate) || startDate.equals(a.endDate);
	}
	
	/**
	 * This method is used to verify that breaks can have a placement in the calendar.
	 * @param b is the break to parse to FixedActivity.
	 * @param s is the starting time of the new FixedActivity.
	 * @param e is the ending time of the new FixedActivity.
	 * @return the new FixedActivity produced from break b and the given times.
	 * @throws InvalidInputException when s comes before b's starting time or
	 * 				e comes after b's ending time. Basically
	 * 				the exception is thrown when s and e are not coherent with the break
	 * 				that is going to be parsed.
	 */
	public static FixedActivity parseFixedActivity(Break b, Date s, Date e) throws InvalidInputException{
		if(s.before(b.getStartDate()) || e.after(b.getEndDate()))
			throw new InvalidInputException();
		return new FixedActivity(s,e,b.label,b.notes,b.locationAddress,b.startPlaceAddress,b.actStatus);
	}	
	/**
	 * @param c is the calendar for which to verify whether the FixedActivity can
	 * be added into it or not.
	 * @return true if the calendar containing the FixedActivity to be added is
	 * still coherent, false otherwise.
	 */
	@Override
	public boolean canBeAddedTo(Calendar c) {
		ArrayList<FixedActivity> calendarActivities= c.getFixedActivities();
		for(FixedActivity fa:calendarActivities)
			if(!this.isBefore(fa) && !this.isAfter(fa))
				return false;
		ArrayList<FixedActivity> fixApp=new ArrayList<>();
		ArrayList<Break> breaks=new ArrayList<>();
		boundSubCalendar(c,fixApp, breaks);
                fixApp=copyList(fixApp);
		fixApp.add(this);
		return c.canBeACalendar(fixApp,breaks);
	}
	
	
	/**
	 * Supports canBeAddedTo method. This method reduces the complexity of the process
	 * followed to verify whether a FixedActivity can be added to a calendar or not
	 * by finding a sub-calendar of the original calendar on which to verify the conditions.
	 * Since the sub-calendar will probably contain less activities than the original calendar,
	 * operations that will be executed are significantly less.
	 * @param c is the original calendar
	 * @param fixApp is the ArrayList that, at the end of the process, will contain
	 * 			all the FixedActvities of the sub-calendar.
	 * @param breaksis the ArrayList that, at the end of the process, will contain
	 * 			all the Breaks of the sub-calendar.
	 */
	private void boundSubCalendar(Calendar c,ArrayList<FixedActivity> fixApp, ArrayList<Break> breaks) {
		ArrayList<FixedActivity> fa=c.getFixedActivities();
		ArrayList<Break> b=c.getBreaks();
		for(Break br:b){
			if((br.getStartDate().after(this.startDate) || br.getStartDate().equals(this.startDate))
					&& br.getStartDate().before(this.endDate) ||
					br.getEndDate().after(this.startDate) && 
				(br.getEndDate().before(this.endDate) || br.getEndDate().equals(this.endDate))){
				breaks.add(br);
			}
		}
		for(FixedActivity fAct: fa){
			for(Break br:breaks){
				if((br.getStartDate().after(fAct.startDate) || br.getStartDate().equals(fAct.startDate))
                                    && br.getStartDate().before(fAct.endDate) ||
                                    br.getEndDate().after(fAct.startDate) && 
                                    (br.getEndDate().before(fAct.endDate) || br.getEndDate().equals(fAct.endDate)) 
                                        || (br.getStartDate().before(fAct.startDate) ||  br.getStartDate().equals(fAct.startDate))
                                            && (br.getEndDate().after(fAct.endDate) ||  br.getEndDate().equals(fAct.endDate))){
					fixApp.add(fAct);
					break;
				}
			}
		}
	}
	
	
	/**
	 * @param fa is the array list of FixedActivity to copy
	 * @return a new ArrayList object containing a copy of all
	 * the FixedActivities in the ArrayList given as parameter
	 */
	public static ArrayList<FixedActivity> copyList(ArrayList<FixedActivity> fa){
		ArrayList<FixedActivity> ret=new ArrayList<>();
		for(FixedActivity act: fa)
			ret.add(new FixedActivity(act));
		return ret;
	}
        
        /**Calculates the esitmated travel time for the activity.
         * This is used for activities that have to be added to the calendar.
         * @return the estimated travel time
         */
        @Override
        public int calculateEstimatedTravelTime(){
            //TODO
            this.estimatedTravelTime=5;
            return 5;
        }
        
        @Override
        public ResponseAddActivityNotification generateRequiredNotification(Calendar c) {
            return ResponseAddActivityNotification.NO;
        }
        
	/* SETTERS */
        public void setEstimatedTravelTime(int ett){
            this.estimatedTravelTime=ett;
        }
	/* GETTERS */
        @Override
	public boolean isFlexible(){
		return false;
	}
        @Override
	public long getDuration(){
		return 0;
	}
        @Override
        public int getEstimatedTravelTime(){
            return this.estimatedTravelTime;
        }

        
}
