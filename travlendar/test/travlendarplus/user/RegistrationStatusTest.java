/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author matteo
 */
public class RegistrationStatusTest {
    @Test
    public void testGetValue(){
        assertEquals(3,RegistrationStatus.EMAIL_NOT_CONFIRMED.getValue());
        assertEquals(1,RegistrationStatus.REGULAR.getValue());
        assertEquals(2,RegistrationStatus.SUSPENDED.getValue());
    }
}
