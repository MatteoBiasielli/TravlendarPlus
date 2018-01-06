/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author matteo
 */
public class BooleanPreferencesSetTest {
    BooleanPreferencesSet bps= new BooleanPreferencesSet(false,false,false,false,false,false,Modality.STANDARD);
    @Test
    public void testGetters(){
        assertEquals(false,bps.bikeSharing());
        assertEquals(false,bps.carSharing());
        assertEquals(false,bps.isRanged());
        assertEquals(false,bps.personalBike());
        assertEquals(false,bps.personalCar());
        assertEquals(false,bps.uberTaxi());
        assertEquals(false,bps.publicTrasport());
        assertEquals(Modality.STANDARD,bps.mode());
        
    }
    @Test
    public void testIsRespectedBy(){
        assertTrue(bps.isRespectedBy(null));
    }
}
