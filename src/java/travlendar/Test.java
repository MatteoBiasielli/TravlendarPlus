/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendar;

import java.sql.SQLException;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.exceptions.InvalidLoginException;
import travlendarplus.exceptions.InvalidPositionException;
import travlendarplus.exceptions.UnconsistentValueException;
import travlendarplus.user.User;

/**
 *
 * @author matteo
 */
public class Test {
    public static void main(String[] args) throws InvalidInputException, SQLException, InvalidLoginException, UnconsistentValueException, InvalidPositionException{
        User u=new User("cane","cane");
        String tag="CANION";
        DataLayer.deleteFavPosition(u, tag);
    }
}
