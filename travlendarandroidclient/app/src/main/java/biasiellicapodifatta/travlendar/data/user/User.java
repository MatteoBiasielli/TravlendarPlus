package biasiellicapodifatta.travlendar.data.user;


import java.util.ArrayList;

import biasiellicapodifatta.travlendar.data.activities.Calendar;
import biasiellicapodifatta.travlendar.data.notification.Notification;
import biasiellicapodifatta.travlendar.data.user.preferences.BooleanPreferencesSet;
import biasiellicapodifatta.travlendar.data.user.preferences.RangedPreference;

public class User {
	private String username;
	private String password;
	private RegistrationStatus status;

	private BooleanPreferencesSet boolPreferences;
	private ArrayList<RangedPreference> rangedPreferences;
	private ArrayList<FavouritePosition> favPos;
	private Calendar calendar;
	private ArrayList<Notification> notifications;

	public User(String u, String p){
		this.username=u;
		this.password=p;
		this.boolPreferences = new BooleanPreferencesSet();
		this.rangedPreferences = new ArrayList<>();
		this.favPos = new ArrayList<>();
		this.notifications = new ArrayList<>();
	}
	
	
	/**@return a string human-readable version of the Object**/
        @Override
	public String toString(){
		String ris= "["+username+","+password+"] -> ";
		if(boolPreferences!=null)
			ris=ris+boolPreferences.toString();
		return ris;
	}

	/* SETTERS */
	public void setCalendar(Calendar c){
		this.calendar=c;
	}

	public void setPreferences(ArrayList<RangedPreference> pSet, BooleanPreferencesSet b){
		setRangedPreferences(pSet);
		setBooleanPreferences(b);
	}

	public void setRangedPreferences(ArrayList<RangedPreference> pSet){
		this.rangedPreferences = pSet;
	}

	public void setBooleanPreferences(BooleanPreferencesSet b){
		this.boolPreferences = b;
	}

	public void setFavPositions(ArrayList<FavouritePosition> fp){
                this.favPos=fp;
        }

	public void deleteRangedPreferences(ArrayList<RangedPreference> pSet){
		for(RangedPreference myrp : rangedPreferences){
			for(RangedPreference rp : pSet){
				if(myrp.equalTo(rp)){
					rangedPreferences.remove(myrp);
				}
			}
		}
	}
	/* GETTERS **/
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public Calendar getCalendar(){
		return this.calendar;
	}
	public ArrayList<FavouritePosition> getFavPositions(){
                return this.favPos;
        }
	public BooleanPreferencesSet getBoolPreferences(){
                return this.boolPreferences;
        }
	public ArrayList<RangedPreference> getRangedPreferences(){
            return this.rangedPreferences;
        }
	public ArrayList<Notification> getNotifications() {
		return this.notifications;
	}
}
