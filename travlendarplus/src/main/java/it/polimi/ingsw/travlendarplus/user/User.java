package it.polimi.ingsw.travlendarplus.user;

import java.util.ArrayList;

import it.polimi.ingsw.travlendarplus.calendar.Calendar;
import it.polimi.ingsw.travlendarplus.user.preferences.Preference;

public class User {
	private String username;
	private String password;
	private RegistrationStatus status;
	private ArrayList<Preference> preferences;
	private Calendar calendar;
	public User(String u, String p){
		this.username=u;
		this.password=p;
	}
	
	
	/**@return a string human-readable version of the Object**/
	public String toString(){
		String ris= "["+username+","+password+"] -> ";
		if(preferences!=null)
			for(Preference p:preferences)
				ris+="("+p.toString()+") ";
		return ris;
	}
	
	/* SETTERS */
	public void setCalendar(Calendar c){
		this.calendar=c;
	}
	public void setPreferences(ArrayList<Preference> pSet){
		this.preferences=pSet;
	}
	
	/* GETTERS **/
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	
	public ArrayList<Preference> getPreferences() {
		return this.preferences;
	}
	
	public Calendar getCalendar(){
		return this.calendar;
	}
}
