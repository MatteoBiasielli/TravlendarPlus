package travlendarplus.user;

import java.sql.SQLException;
import java.util.ArrayList;

import travlendarplus.calendar.Calendar;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.AlreadyExistingUserException;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.user.preferences.BooleanPreferencesSet;
import travlendarplus.user.preferences.Preference;

public class User {
	private String username;
	private String password;
	private RegistrationStatus status;
	private ArrayList<Preference> preferences;
        private ArrayList<FavouritePosition> favPos;
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
	
        /**
         * 
         * @return true if the user represents a valid login
         * @throws InvalidInputException if the given username/password are not a string containing only letters
         * @throws SQLException is a database access error occurs
         */
        public boolean isValidLogin() throws InvalidInputException, SQLException{
            return DataLayer.validLogin(username, password);
        }
        
        /**
         * 
         * @return true if the username exists in the DB
         * @throws InvalidInputException if the given username is not a string containing only letters
         * @throws SQLException is a database access error occurs
         */
        public boolean usernameExists() throws InvalidInputException, SQLException{
            return DataLayer.usernameExists(username);
        }
        
        /**Retrieves a user's calendar in the DB. At the end of the method, the result is put in the caller object
	 * @throws InvalidInputException if the given username/password in this object are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the caller object doesn't represent a valid login
	 * @throws UnconsistentValueException is there's an inconsistency among favorite position's values in the database
	 */
        public void getCalendarFromDB() throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
            DataLayer.getCalendar(this);
        }
        
        /**Retrieves the caller object's preferences in the DB. At the end of the method, the result is put in the caller object
	 * @throws InvalidInputException if the given username/password in the caller object are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the caller object doesn't represent a valid login
	 * @throws UnconsistentValueException is there's a mismatch between values in the database and RangedPreferencedType's values
	 */
        public void getPreferencesFromDB() throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
            DataLayer.getPreferences(this);
        }
        
        /**Registers the user in the DB
	 * @throws InvalidInputException if the given username/password are not a string containing only letters or if rst is null
	 * @throws SQLException  if a database access error occurs
	 * @throws AlreadyExistingUserException  if the DB already contains the given username
	 */
        public void registerInDB() throws InvalidInputException, SQLException, AlreadyExistingUserException {
            DataLayer.registerUser(username, password, RegistrationStatus.REGULAR);
        }
        
        /**Retrieves the caller object's favourite positions in the DB. At the end of the method, the result is put in the caller object
	 * @throws InvalidInputException if the given username/password in the caller object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the caller object doesn't represent a valid login
         * @throws InvalidPositionException if the coordinates associated to some position don't correspond to any valid address on Earth
	 */
        public void getfavPositionsFromDB() throws InvalidInputException,InvalidPositionException, SQLException, InvalidLoginException{
            DataLayer.getFavPositions(this);
        }
	/* SETTERS */
	public void setCalendar(Calendar c){
		this.calendar=c;
	}
	public void setPreferences(ArrayList<Preference> pSet){
		this.preferences=pSet;
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
	
	public ArrayList<Preference> getPreferences() {
		return this.preferences;
	}
	
	public Calendar getCalendar(){
		return this.calendar;
	}
        public ArrayList<FavouritePosition> getFavPositions(){
                return this.favPos;
        }
        public BooleanPreferencesSet getBoolPreferences(){
                for(Preference p:this.preferences)
                    if(!p.isRanged())
                        return (BooleanPreferencesSet)p;
                return null;
        }
}
