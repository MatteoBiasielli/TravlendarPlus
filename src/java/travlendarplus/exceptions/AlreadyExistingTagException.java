/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.exceptions;

/**
 *
 * @author matteo
 */
public class AlreadyExistingTagException extends Exception{
    public AlreadyExistingTagException(String string) {
		super(string);
	}
}
