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
public class ModalityTest {
    @Test
    public void testGetValue(){
        assertEquals(1,Modality.STANDARD.getValue());
        assertEquals(2,Modality.MINIMIZE_FOOTPRINT.getValue());
        assertEquals(3,Modality.MINIMIZE_COST.getValue());
        assertEquals(4,Modality.MINIMIZE_TIME.getValue());
    }
    @Test(expected=travlendarplus.exceptions.UnconsistentValueException.class)
    public void testGetForValue() throws UnconsistentValueException{
        assertEquals(Modality.getForValue(1),Modality.STANDARD);
        assertEquals(Modality.getForValue(2),Modality.MINIMIZE_FOOTPRINT);
        assertEquals(Modality.getForValue(3),Modality.MINIMIZE_COST);
        assertEquals(Modality.getForValue(4),Modality.MINIMIZE_TIME);
        assertEquals(Modality.getForValue(5),Modality.MINIMIZE_TIME);
   }
}
