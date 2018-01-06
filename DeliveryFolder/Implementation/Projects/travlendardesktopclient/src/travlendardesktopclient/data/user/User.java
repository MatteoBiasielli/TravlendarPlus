package travlendardesktopclient.data.user;


import java.util.ArrayList;
import travlendardesktopclient.data.activities.Calendar;
import travlendardesktopclient.data.notification.Notification;
import travlendardesktopclient.data.user.preferences.BooleanPreferencesSet;
import travlendardesktopclient.data.user.preferences.RangedPreference;


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
		this.rangedPreferences=pSet;
                this.boolPreferences=b;
	}
	public void setFavPositions(ArrayList<FavouritePosition> fp){
                this.favPos=fp;
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
        public void setNotifications(ArrayList<Notification> notif){
		this.notifications = notif;
	}
}
