package travlendarplus.user;

import java.sql.SQLException;
import java.util.ArrayList;

import travlendarplus.calendar.Calendar;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.user.preferences.Preference;

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
        @Override
	public String toString(){
		String ris= "["+username+","+password+"] -> ";
		if(preferences!=null)
			for(Preference p:preferences)
				ris+="("+p.toString()+") ";
		return ris;
	}
	
        public boolean isValidLogin() throws InvalidInputException, SQLException{
            return DataLayer.validLogin(username, password);
        }
        public boolean usernameExists() throws InvalidInputException, SQLException{
            return DataLayer.usernameExists(username);
        }
        public void getCalendarFromDB() throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
            DataLayer.getCalendar(this);
        }
        public void getPreferencesFromDB() throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
            DataLayer.getPreferences(this);
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
