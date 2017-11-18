package travlendarplus.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import travlendarplus.apimanager.APIManager;
import travlendarplus.calendar.Calendar;
import travlendarplus.calendar.activities.*;
import travlendarplus.exceptions.*;
import travlendarplus.travel.Position;
import travlendarplus.user.*;
import travlendarplus.user.preferences.*;



public class DataLayer {
	/**DBMS username**/
	private static final String USERNAME="admin";
	/**DBMS password**/
	private static final String PASSWORD="giorgio";
	/**DBMS url**/
	private static final String DB_URL="jdbc:mysql://localhost/travlendar?serverTimezone=GMT";
	
	/**Queries the DB to see if a given username exists
	 * @param username is the username for which existance has to be checked
	 * @return true if it exists in the DB, false otherwise
	 * @throws InvalidInputException if the given username is not a string containing only letters
	 * @throws SQLException  if a database access error occurs
	 */
	public static boolean usernameExists(String username) throws InvalidInputException, SQLException{
		if(!username.matches("([a-z]|[A-Z])+"))
			throw new InvalidInputException("Invalid username.");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * "
        		+ "FROM user "
        		+ "WHERE user.username='" + username+"'";
                ResultSet rs = stmt.executeQuery(query);
                boolean ris=false;
                if(rs.next())
                        ris=true;
                rs.close();
                con.close();
                return ris;
	}
	
	/**Queries the DB to see if a given pair username-password is valid
	 * @param username 
	 * @param password
	 * @return true if the pair username-password is valid, false otherwise
	 * @throws InvalidInputException if the given username/password are not a string containing only letters
	 * @throws SQLException  if a database access error occurs
	 */
	public static boolean validLogin(String username, String password)  throws InvalidInputException, SQLException{
		if(!username.matches("([a-z]|[A-Z])+"))
			throw new InvalidInputException("Invalid username.");
		if(!password.matches("([a-z]|[A-Z])+"))
			throw new InvalidInputException("Invalid password.");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * "
        		+ "FROM user "
        		+ "WHERE user.username='" + username+"'"
        		+" AND user.password='" + password+"'";
                ResultSet rs = stmt.executeQuery(query);
                boolean ris=false;
                if(rs.next())
                        ris=true;
                rs.close();
                con.close();
                return ris;
	}
	
	/**Registers a user in the DB
	 * @param username 
	 * @param password
	 * @param rst is the registration status
	 * @throws InvalidInputException if the given username/password are not a string containing only letters or if rst is null
	 * @throws SQLException  if a database access error occurs
	 * @throws AlreadyExistingUserException  if the DB already contains the given username
	 */
	public static void registerUser(String username, String password, RegistrationStatus rst)  throws InvalidInputException, SQLException, AlreadyExistingUserException{
		if(!username.matches("([a-z]|[A-Z])+"))
			throw new InvalidInputException("Invalid username.");
		if(!password.matches("([a-z]|[A-Z])+"))
			throw new InvalidInputException("Invalid password.");
		if(rst==null)
			throw new InvalidInputException("Invalid registration status.");
		if(DataLayer.usernameExists(username))
			throw new AlreadyExistingUserException("This User already exists.");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="INSERT INTO USER values (NULL,'"+username+"','"+password+"'," + rst.getValue()+")";
                stmt.execute(query);
                int id=DataLayer.getUserKeyID(username);
                query="INSERT INTO BOOLEAN_PREFERENCES values "
                                + "(NULL,"+id+",false,true,false,true,true,true,"+Modality.STANDARD.getValue()+")";
                stmt.execute(query);
                con.close();
	}
	
	/**Given a username, that is assumed to be existing in the DB, returns its tuple primary key identifier
	 * 
	 * @param username
	 * @return an integer that is the primary key identifier of the tuple containing the given username
	 * @throws SQLException if a database access error occurs
	 */
	private static int getUserKeyID(String username) throws SQLException{
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT ID FROM USER WHERE username='"+username+"'";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                return rs.getInt("ID");
	}
	
	/**Updates a user's ranged preference in the DB. This method can also be used to add a preference.
	 * @param u is an object containing username and password of the user that needs to update/add its preferences
	 * @param p is the preference to update
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void updatePreference(User u, RangedPreference p) throws InvalidLoginException, InvalidInputException, SQLException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="DELETE FROM RANGED_PREFERENCES "
                                + "WHERE USER_ID="+userKey
                                + " AND PREF_TYPE="+p.getType().getValue();
                stmt.execute(query);
                query="INSERT INTO RANGED_PREFERENCES values(NULL,"+userKey+","
                                +p.getType().getValue()+","+p.getValue()+")";
                stmt.execute(query);
                con.close();
	}
	
	/**Deletes a user's ranged preference in the DB.
	 * @param u is an object containing username and password of the user that needs to delete its preference
	 * @param p is the preference to delete
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters or if rst is null
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void deletePreference(User u, RangedPreference p) throws InvalidInputException, SQLException, InvalidLoginException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="DELETE FROM RANGED_PREFERENCES "
                                + "WHERE USER_ID="+userKey
                                + " AND PREF_TYPE="+p.getType().getValue();
                stmt.execute(query);
                con.close();
	}
	
	/**Updates a user's boolean preference in the DB.
	 * @param u is an object containing username and password of the user that needs to update its preferences
	 * @param p is the preference to update
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void updatePreference(User u, BooleanPreferencesSet p) throws InvalidLoginException, InvalidInputException, SQLException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="UPDATE BOOLEAN_PREFERENCES SET "
                                + "PERSONAL_CAR="+p.personalCar()
                                +" , CAR_SHARING="+p.carSharing()
                                +" , PERSONAL_BIKE="+p.personalBike()
                                +" , BIKE_SHARING="+p.bikeSharing()
                                +" , PUBLIC_TRANSPORT="+p.publicTrasport()
                                +" , UBER_TAXI="+p.uberTaxi()
                                +" , MODALITY="+p.mode().getValue()
                                +" WHERE USER_ID="+userKey;
                stmt.execute(query);
                con.close();
	}
	
	/**Retrieves a user's preferences in the DB. At the end of the method, the result is put in the input object
	 * @param u is an object containing username and password of the user for which to retrieve preferences
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 * @throws UnconsistentValueException is there's a mismatch between values in the database and RangedPreferencedType's values
	 */
	public static void getPreferences(User u) throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		ArrayList<Preference> pref= new ArrayList<>();
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * FROM USER,BOOLEAN_PREFERENCES WHERE"
        		+ " USER.ID=BOOLEAN_PREFERENCES.USER_ID"
        		+ " AND USER.USERNAME='"+u.getUsername()+"'";
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                        Preference p= new BooleanPreferencesSet(rs.getBoolean("PERSONAL_CAR"),rs.getBoolean("CAR_SHARING"),rs.getBoolean("PERSONAL_BIKE"),rs.getBoolean("BIKE_SHARING"),rs.getBoolean("PUBLIC_TRANSPORT"),rs.getBoolean("UBER_TAXI"),Modality.getForValue(rs.getInt("MODALITY")));
                        pref.add(p);
                }
                rs.close();
                query="SELECT * FROM USER,RANGED_PREFERENCES WHERE"
                                + " USER.ID=RANGED_PREFERENCES.USER_ID"
                                + " AND USER.USERNAME='"+u.getUsername()+"'";
                rs = stmt.executeQuery(query);
                while(rs.next()){
                        Preference p= new RangedPreference(RangedPreferenceType.getForValue(rs.getInt("PREF_TYPE")),rs.getInt("VALUE"));
                        pref.add(p);
                }
                rs.close();
                con.close();
                u.setPreferences(pref);
	}
	
	/**Retrieves a user's calendar in the DB. At the end of the method, the result is put in the input object
	 * @param u is an object containing username and password of the user for which to retrieve the calendar
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 * @throws UnconsistentValueException is there's an inconsistency among favorite position's values in the database
	 */
	public static void getCalendar(User u) throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * FROM USER,ACTIVITY WHERE "
        		+ "USER.ID=ACTIVITY.USER "
        		+ "AND USER.username='"+u.getUsername()+"'";
                ResultSet rs = stmt.executeQuery(query);
                ArrayList<FixedActivity> fix=new ArrayList<>();
                ArrayList<Break> breaks= new ArrayList<>();
                while(rs.next()){
                        Date s=new Date(rs.getLong("START_DAY_TIME"));
                        Date e=new Date(rs.getLong("END_DAY_TIME"));
                        String l=rs.getString("LABEL");
                        String n=rs.getString("NOTES");
                        Integer tagIdSP=rs.getInt("START_PLACE_TAG_ID");
                        String sP="";
                        if(tagIdSP!=0){
                                try {
                                        sP=APIManager.googleReverseGeocodingRequest(DataLayer.getPositionFromID(tagIdSP));
                                } catch (InvalidPositionException e1) {
                                        throw new UnconsistentValueException("Database contains unconsistencies in positions values.");
                                }
                        }
                        else
                                sP=rs.getString("START_PLACE_TEXT");
                        Integer tagIdLA=rs.getInt("LOCATION_TAG_ID");
                        String lA="";
                        if(tagIdLA!=0){
                            try {
                                lA=APIManager.googleReverseGeocodingRequest(DataLayer.getPositionFromID(tagIdLA));
                            } catch (InvalidPositionException e1) {
                                throw new UnconsistentValueException("Database contains unconsistencies in positions values.");
                            }
                        }
                        else
                                lA=rs.getString("LOCATION_TEXT");
                        ActivityStatus stat=ActivityStatus.getForValue(rs.getInt("STATUS"));
                        boolean flex=rs.getBoolean("FLEXIBLE");
                        if(flex){
                                int d=rs.getInt("DURATION");
                                Break b= new Break(s,e,l,n,lA,sP,stat,d);
                                b.setKey(rs.getInt("KEY_ID"));
                                breaks.add(b);
                        }
                        else{
                                FixedActivity fa=new FixedActivity(s,e,l,n,lA,sP,stat);
                                fa.setKey(rs.getInt("KEY_ID"));
                                fa.setEstimatedTravelTime(rs.getInt("ESTIMATED_TRAVEL_TIME"));
                                fix.add(fa);
                        }
                }
                Calendar c=new Calendar(fix,breaks);
                u.setCalendar(c);
                rs.close();
                con.close();
	}
	
	/**Retrieves a position in the DB, given its tuple key id.
	 * @param id
	 * @return an objetct Position containing latitude and longitude found in the tuple corresponding to the given id
	 * @throws SQLException  if a database access error occurs
	 * @throws UnconsistentValueException if no feasible result can be found, given the input. Since 
	 * this method is private and is used by another single method, this can happen only if there's a mismatch in foreign/primary key associations
	 */
	private static Position getPositionFromID(Integer id) throws SQLException, UnconsistentValueException{
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * FROM FAVPOSITIONS WHERE"
                                + " FAVPOSITIONS.KEY_ID="+id;
                ResultSet rs = stmt.executeQuery(query);
                if(!rs.next())
                        throw new UnconsistentValueException("Database contains unconsistencies in positions ids.");
                Position res=new Position(rs.getDouble("LATITUDE"), rs.getDouble("LONGITUDE"));
                rs.close();
                con.close();
                return res;
        }
	
	/**Retrieves a user's favorite place's id in the DB.
	 * @param u is an object containing username and password of the user that owns the tag
	 * @return an integer representing the key id of the tuple of the favorite position associated to the tag and to the user
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * or if no result can be found
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static int getIDFromTag(User u, String tag) throws InvalidInputException, SQLException, InvalidLoginException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                String query="SELECT * FROM FAVPOSITIONS WHERE"
        		+ " FAVPOSITIONS.TAG='"+tag+"'"
        		+ " AND FAVPOSITIONS.USER_ID="+DataLayer.getUserKeyID(u.getUsername());
                ResultSet rs = stmt.executeQuery(query);
                if(!rs.next())
                        throw new InvalidInputException("There's no tuple associating the given tag to the given user");
                int res=rs.getInt("KEY_ID");
                rs.close();
                con.close();
                return res;
	}
	
	/**Adds an activity to a user's calendar in the DB.
	 * @param u is an object containing username and password of the user that needs to add the activity
	 * @param a is the activity to add
	 * @param tagLocation is a parameter used to indicate if the user has indicated one of his favorite places, that he has added to the system previously, as location of the activity. If this parameter is different from NULL, the attribute location contained in the activity object is not taken into account. If you want to use the attribute location in the activity object given as input leave this parameter as NULL.
	 * @param tagStart is a parameter used to eventually indicate if the user has indicated one of his favorite places, that he has added to the system previously, as place he will start from to reach the activity location. If this parameter is different from NULL, the attribute startPlaceAddress contained in the activity object is not taken into account. If you want to use the attribute startPlaceAddress in the activity object given as input leave this parameter as NULL.
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void addActivity(User u, Activity a, String tagLocation, String tagStart) throws InvalidInputException, SQLException, InvalidLoginException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="INSERT INTO ACTIVITY values (NULL,"+userKey+",'"
                                +a.getLabel()+"','"+a.getNotes()+"'";
                if(tagLocation!=null)
                        query+=",NULL,"+getIDFromTag(u,tagLocation);
                else
                        query+=",'"+a.getLocationAddress()+"',NULL";
                if(tagStart!=null)
                        query+=",NULL,"+getIDFromTag(u,tagStart);
                else
                        query+=",'"+a.getStartPlaceAddress()+"',NULL";
                query+=","+a.isFlexible()+","+a.getDuration()+","+a.getStartDate().getTime()+","+ a.getEndDate().getTime()+","+a.getStatus().getValue()+","+a.getEstimatedTravelTime()+")";
                stmt.execute(query);
                con.close();
	}
	
	/**Updates an activity from a user's calendar in the DB.
	 * @param u is an object containing username and password of the user that needs to update its activity
	 * @param a is the activity to update
	 * @param tagLocation is a parameter used to indicate if the user has indicated one of his favorite places, that he has added to the system previously, as location of the activity. If this parameter is different from NULL, the attribute location contained in the activity object is not taken into account. If you want to use the attribute location in the activity object given as input leave this parameter as NULL.
	 * @param tagStart is a parameter used to eventually indicate if the user has indicated one of his favorite places, that he has added to the system previously, as place he will start from to reach the activity location. If this parameter is different from NULL, the attribute startPlaceAddress contained in the activity object is not taken into account. If you want to use the attribute startPlaceAddress in the activity object given as input leave this parameter as NULL.
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void updateActivity(User u, Activity a, String tagLocation, String tagStart) throws InvalidInputException, SQLException, InvalidLoginException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="UPDATE ACTIVITY SET "
        		+ "LABEL='"+a.getLabel()+"', "
        		+ "NOTES='"+a.getNotes()+"', "
        		+ "LABEL='"+a.getLabel()+"', ";
                if(tagLocation!=null){
                        query+="LOCATION_TEXT=NULL, " +
                                        "LOCATION_TAG_ID=" + getIDFromTag(u,tagLocation)+", ";
                }
                else{
                        query+="LOCATION_TEXT='"+a.getLocationAddress()+ "', " +
                                        "LOCATION_TAG_ID=NULL, ";
                }
                if(tagStart!=null){
                        query+="START_PLACE_TEXT=NULL, " +
                                        "START_PLACE_TAG_ID=" + getIDFromTag(u,tagStart)+", ";
                }
                else{
                        query+="START_PLACE_TEXT='"+a.getStartPlaceAddress()+ "', " +
                                        "START_PLACE_TAG_ID=NULL, ";
                }
                query+="FLEXIBLE="+a.isFlexible()+", "
        		+"DURATION="+a.getDuration()+", "
        		+"START_DAY_TIME="+ a.getStartDate().getTime()+", "
        		+"END_DAY_TIME="+ a.getEndDate().getTime()+", "
        		+"STATUS=" +a.getStatus().getValue()+ ", "
                        +"ESTIMATED_TRAVEL_TIME=" + a.getEstimatedTravelTime()+
        		" WHERE ACTIVITY.KEY_ID="+a.getKey()+
        		" AND ACTIVITY.USER="+userKey;
                stmt.execute(query);
                con.close();
	}
	
	/**Deletes an activity from a user's calendar in the DB.
	 * @param u is an object containing username and password of the user that needs to delete its activity
	 * @param a is the activity to delete
	 * @throws InvalidInputException if the given username/password in the input object u are not strings containing only letters
	 * @throws SQLException  if a database access error occurs
	 * @throws InvalidLoginException  if the object u doesn't represent a valid login
	 */
	public static void deleteActivity(User u, Activity a) throws InvalidInputException, SQLException, InvalidLoginException{
		if(!validLogin(u.getUsername(),u.getPassword()))
			throw new InvalidLoginException("Invalid Username or Password");
		Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                Statement stmt = con.createStatement();
                int userKey=DataLayer.getUserKeyID(u.getUsername());
                String query="DELETE FROM ACTIVITY WHERE KEY_ID="+a.getKey()+" AND USER="+userKey;
                stmt.execute(query);
                con.close();
	}
}
