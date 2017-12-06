/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.user;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author matteo
 */
public class FavouritePositionTest {
    FavouritePosition fp= new FavouritePosition("addr","tag");
    @Test
    public void testGetAddress(){
        assertTrue(fp.getAddress().equals("addr"));
    }
    @Test
    public void testGetTag(){
        assertTrue(fp.getTag().equals("tag"));
    }
}
