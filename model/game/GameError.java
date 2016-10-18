package model.game;

/**
 * This class handles runtime exception in the game.
 * @author yaoyucui
 */
public class GameError extends RuntimeException{
	//throw new exception from other class
	public GameError(String errormsg){
		super(errormsg);
	}
	
}
