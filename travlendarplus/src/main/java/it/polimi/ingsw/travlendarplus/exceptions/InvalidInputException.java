package it.polimi.ingsw.travlendarplus.exceptions;

public class InvalidInputException extends Exception {
	public InvalidInputException(String s){
		super(s);
	}

	public InvalidInputException() {
		super();
	}
}
