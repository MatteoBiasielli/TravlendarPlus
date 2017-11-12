package it.polimi.ingsw.travlendarplus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import it.polimi.ingsw.travlendarplus.calendar.activities.ActivityStatus;
import it.polimi.ingsw.travlendarplus.calendar.activities.FixedActivity;
import it.polimi.ingsw.travlendarplus.data.DataLayer;
import it.polimi.ingsw.travlendarplus.exceptions.InvalidInputException;
import it.polimi.ingsw.travlendarplus.exceptions.InvalidLoginException;
import it.polimi.ingsw.travlendarplus.exceptions.UnconsistentValueException;
import it.polimi.ingsw.travlendarplus.user.User;
import it.polimi.ingsw.travlendarplus.user.preferences.BooleanPreferencesSet;
import it.polimi.ingsw.travlendarplus.user.preferences.Modality;
import it.polimi.ingsw.travlendarplus.user.preferences.RangedPreference;
import it.polimi.ingsw.travlendarplus.user.preferences.RangedPreferenceType;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InvalidLoginException, InvalidInputException, SQLException, UnconsistentValueException, IOException
    {
        User u=new User("cane","cane");
        /*RangedPreference p=new RangedPreference(RangedPreferenceType.WALKING_TIME_LIMIT,6);
        BooleanPreferencesSet p2=new BooleanPreferencesSet(true,true,true,true,true,true,Modality.MINIMIZE_TIME);
        DataLayer.updatePreference(u, p2);*/
       /* System.out.println(u);
        DataLayer.getPreferences(u);
        System.out.println(u);*/
        //DataLayer.getCalendar(u);
        //System.out.println(new Date().getTime());
        //System.out.println(u.getCalendar());
        //DataLayer.addActivity(u, new FixedActivity(new Date(), new Date(new Date().getTime()+60*60*3), "prova", "note di prova", "via stefanardo da vimercate 8 milano", "via caduti di marcinelle 2 milano" , ActivityStatus.ON_GOING),"d",null);
        FixedActivity a=new FixedActivity(new Date(), new Date(new Date().getTime()+60*60*3), "prova", "note di prova111", "via stefanardo da vimercate 95 milano", "via caduti di marcinelle 2 milano" , ActivityStatus.ON_GOING);
        a.setKey(5);
        //DataLayer.updateActivity(u,a ,null,null);
        DataLayer.addActivity(u, a,null,null);
        
    }
}
