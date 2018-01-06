/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user.preferences;

import java.util.ArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import travlendarplus.travel.Leg;
import travlendarplus.travel.Route;
import travlendarplus.travel.Step;
import travlendarplus.travel.TravelMode;

/**
 *
 * @author matteo
 */
public class RangedPreferenceTest {
    private RangedPreference rpCar=new RangedPreference(RangedPreferenceType.CAR_TIME_LIMIT,5);
    private RangedPreference rpWalk=new RangedPreference(RangedPreferenceType.WALKING_TIME_LIMIT,5);
    private RangedPreference rpTransp=new RangedPreference(RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT,5);
    
    
    @Test
    public void testGetType(){
        assertTrue(RangedPreferenceType.CAR_TIME_LIMIT==rpCar.getType());
        assertTrue(RangedPreferenceType.WALKING_TIME_LIMIT==rpWalk.getType());
        assertTrue(RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT==rpTransp.getType());
    }
    @Test
    public void testGetValue(){
        assertTrue(5==rpCar.getValue());
    }
    @Test
    public void testIsRanged(){
        assertTrue(rpCar.isRanged());
    }
    @Test
    public void testIsRespectedBy(){
        Route driveRoute=new Route();
        Step st=new Step(null,0,null,300,null);
        ArrayList<Step> stepList=new ArrayList<>();
        stepList.add(st);
        st.setMode(TravelMode.DRIVING);
        driveRoute.getLegs().add(new Leg(null,null,null,0,stepList));
        assertTrue(this.rpCar.isRespectedBy(driveRoute));
        Step st2=new Step(null,0,null,300,null);
        st2.setMode(TravelMode.DRIVING);
        stepList.add(st2);
        assertFalse(this.rpCar.isRespectedBy(driveRoute));
        Step st3=new Step(null,0,null,300,null);
        st3.setMode(TravelMode.WALKING);
        stepList.add(st3);
        assertTrue(this.rpWalk.isRespectedBy(driveRoute));
        Step st4=new Step(null,0,null,300,null);
        st4.setMode(TravelMode.TRANSIT);
        stepList.add(st4);
        assertTrue(this.rpTransp.isRespectedBy(driveRoute));
    }
}
