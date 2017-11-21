package travlendarplus.calendar.activities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import travlendarplus.apimanager.APIManager;
import travlendarplus.apimanager.Language;

import travlendarplus.calendar.Calendar;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.NoPathFoundException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.response.responseaddactivity.ResponseAddActivityNotification;
import travlendarplus.travel.Route;
import travlendarplus.travel.TravelMode;
import travlendarplus.user.FavouritePosition;
import travlendarplus.user.User;
import travlendarplus.user.preferences.BooleanPreferencesSet;

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
		this.label=fa.label==null?null:new String(fa.label);
		this.notes=fa.notes==null?null:new String(fa.notes);
		this.locationAddress=fa.locationAddress==null?null:new String(fa.locationAddress);
		this.startPlaceAddress=fa.startPlaceAddress==null?null:new String(fa.startPlaceAddress);
		this.actStatus=fa.actStatus;
		this.key=fa.key;
		this.keySet=fa.keySet;
                this.estimatedTravelTime=fa.estimatedTravelTime;
	}

	/**
	 *
	 */
	public FixedActivity(){
		key = 0;
		keySet = false;
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
		for(Break br:b)
			if(Activity.fixedBreakOverlap(this, br))
				breaks.add(br);
		for(FixedActivity fAct: fa)
			for(Break br:breaks)
				if(Activity.fixedBreakOverlap(fAct, br))
					fixApp.add(fAct);
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
        
        
        //TOFIX
        /**Calculates the esitmated travel time for the activity.
         * This is used for activities that have to be added to the calendar.
         * @param tagStart is the starting location tag
         * @param tagLoc is the activity location tag
         * @param u is the user for which the calulus has to be performed
         * @return the estimated travel time
         */
        @Override
        public int calculateEstimatedTravelTime(String tagStart, String tagLoc, User u) throws IOException, InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException, InvalidPositionException{
            String startAddress=null;
            String endAddress=null;
            int carTime=0;
            int transportTime=0;
            int bikeTime=0;
            int count=0;
            u.getPreferencesFromDB();
            u.getfavPositionsFromDB();
            if(tagStart==null)
                startAddress=this.startPlaceAddress;
            else
                for(FavouritePosition pos:u.getFavPositions())
                    if(tagStart.equals(pos.getTag()))
                        startAddress=pos.getAddress();
            if(tagLoc==null)
                endAddress=this.locationAddress;
            else
                for(FavouritePosition pos:u.getFavPositions())
                    if(tagLoc.equals(pos.getTag()))
                        endAddress=pos.getAddress();
            if(startAddress==null || endAddress==null)
                throw new InvalidInputException();
            BooleanPreferencesSet userPrefs=u.getBoolPreferences();
            if(userPrefs.personalCar() || userPrefs.carSharing() || userPrefs.uberTaxi()){
                try{
                    Route r=APIManager.googleDirectionsRequest(startAddress, endAddress, TravelMode.DRIVING, false, Language.EN);
                    count++;
                    carTime=r.getDuration();
                }catch(NoPathFoundException e){}
            }
            //FOR THE MOMENT, BIKE IS HALF OF WALKING
            /*if(userPrefs.personalBike() || userPrefs.bikeSharing()){
                Route r=APIManager.googleDirectionsRequest(startAddress, endAddress, TravelMode.WALKING, false, Language.EN);
                count++;
                bikeTime=r.getDuration()/2;
            }*/
            if(userPrefs.publicTrasport()){
                try{
                    Route r=APIManager.googleDirectionsRequest(startAddress, endAddress, TravelMode.TRANSIT, false, Language.EN);
                    count++;
                    transportTime=r.getDuration();
                }catch(NoPathFoundException e){}
            }
            if(count>0)
                this.estimatedTravelTime=(int)((carTime+transportTime+bikeTime)/count);
            else
                this.estimatedTravelTime=0;
            return this.estimatedTravelTime;
        }
        
        //TOFIX
        /**
         * Computes the notification that would be generated when this activity
         * is added to the calendar given as parameter.
         * @param c the caledar mentioned above.
         * @return the notification that would be generated when this activity 
         * is added to the calendar given as parameter.
         */
        @Override
        public ResponseAddActivityNotification generateRequiredNotification(Calendar c) {
            Calendar mod= Calendar.modifyCalendarWithEstimatedTravelTimes(c);
            FixedActivity thisMod= new FixedActivity(new Date(this.startDate.getTime()-this.estimatedTravelTime*60*1000),this.endDate,null,null,null,null,null);
            ArrayList<FixedActivity> calendarActivities= mod.getFixedActivities();
            for(FixedActivity fa:calendarActivities)
                    if(!thisMod.isBefore(fa) && !thisMod.isAfter(fa)){
                        if(fa.endDate.after(thisMod.endDate))
                            return ResponseAddActivityNotification.CANNOT_BE_ON_TIME_NEXT;
                        else
                            return ResponseAddActivityNotification.CANNOT_BE_ON_TIME;
                    }     
            ArrayList<FixedActivity> fixApp=new ArrayList<>();
            ArrayList<Break> breaks=new ArrayList<>();
            boundSubCalendar(mod,fixApp, breaks);
            fixApp.add(thisMod);
            if(!c.canBeACalendar(fixApp,breaks))
                return ResponseAddActivityNotification.OTHER;
            return ResponseAddActivityNotification.NO;
        }
        
        
        
        public ArrayList<Route> computeTravels(User u) throws NoPathFoundException, IOException{
            ArrayList<Route> ris=new ArrayList<>();
            BooleanPreferencesSet userPref=u.getBoolPreferences();
            //DRIVING TRAVEL MEAN
            if(userPref.personalCar()){
                try{
                        Route r=APIManager.googleDirectionsRequest(startPlaceAddress, locationAddress, TravelMode.DRIVING, false, Language.EN);
                        if(r.respects(u.getPreferences()))
                            ris.add(r);
                }catch(NoPathFoundException e){}
            }
            //PUBLIC TRANSPORT TRAVEL MEAN
            if(userPref.publicTrasport()){
                try{
                        Route r=APIManager.googleDirectionsRequest(startPlaceAddress, locationAddress, TravelMode.TRANSIT, false, Language.EN);
                        if(r.respects(u.getPreferences()))
                            ris.add(r);
                }catch(NoPathFoundException e){}
            }
            //WALKING TRAVEL MEAN
            try{
                    Route r=APIManager.googleDirectionsRequest(startPlaceAddress, locationAddress, TravelMode.TRANSIT, false, Language.EN);
                    if(r.respects(u.getPreferences()))
                        ris.add(r);
            }catch(NoPathFoundException e){}
            
            if(ris.isEmpty())
                throw new NoPathFoundException("No routes were found.");
            return ris;
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
