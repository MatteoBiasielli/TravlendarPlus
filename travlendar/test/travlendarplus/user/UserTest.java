/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user;

import java.util.ArrayList;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import travlendarplus.calendar.Calendar;
import travlendarplus.notification.Notification;
import travlendarplus.user.preferences.BooleanPreferencesSet;
import travlendarplus.user.preferences.RangedPreference;

/**
 *
 * @author matteo
 */
public class UserTest {
    private User u=new User("cane","dog");
    @Test
    public void testGetUsername(){
        assertTrue(u.getUsername().equals("cane"));
    }
    @Test
    public void testGetPassword(){
        assertTrue(u.getPassword().equals("dog"));
    }
    @Test
    public void testToString(){
        assertTrue(u.toString().equals("[cane,dog]"));
    }
    @Test
    public void testGetSetCalendar(){
        Calendar c=new Calendar(null,null);
        u.setCalendar(c);
        assertTrue(c==u.getCalendar());
    }
    @Test
    public void testGetSetFavPositions(){
        ArrayList<FavouritePosition> fp= new ArrayList<>();
        u.setFavPositions(fp);
        assertTrue(fp==u.getFavPositions());
    }
    @Test
    public void testGetSetPreferences(){
        ArrayList<RangedPreference> rp= new ArrayList<>();
        BooleanPreferencesSet bps= new BooleanPreferencesSet(false,false,false,false,false,false,null);
        u.setPreferences(rp, bps);
        assertTrue(rp==u.getRangedPreferences());
        assertTrue(bps==u.getBoolPreferences());
    }
    @Test
    public void testGetSetNotification(){
        ArrayList<Notification> fp= new ArrayList<>();
        u.setNotifications(fp);
        assertTrue(fp==u.getNotifications());
    }
}
