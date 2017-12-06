/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user.preferences;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import travlendarplus.exceptions.UnconsistentValueException;

/**
 *
 * @author matteo
 */
public class RangedPreferenceTypeTest {
    @Test
    public void testGetValue(){
        assertEquals(1,RangedPreferenceType.WALKING_TIME_LIMIT.getValue());
        assertEquals(2,RangedPreferenceType.COST_LIMIT.getValue());
        assertEquals(3,RangedPreferenceType.BIKING_TIME_LIMIT.getValue());
        assertEquals(4,RangedPreferenceType.CAR_TIME_LIMIT.getValue());
        assertEquals(5,RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT.getValue());
    }
    @Test(expected=travlendarplus.exceptions.UnconsistentValueException.class)
    public void testGetForValue() throws UnconsistentValueException{
        assertEquals(RangedPreferenceType.getForValue(1),RangedPreferenceType.WALKING_TIME_LIMIT);
        assertEquals(RangedPreferenceType.getForValue(2),RangedPreferenceType.COST_LIMIT);
        assertEquals(RangedPreferenceType.getForValue(3),RangedPreferenceType.BIKING_TIME_LIMIT);
        assertEquals(RangedPreferenceType.getForValue(4),RangedPreferenceType.CAR_TIME_LIMIT);
        assertEquals(RangedPreferenceType.getForValue(5),RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT);
        assertEquals(RangedPreferenceType.getForValue(6),RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT);
   }
}
