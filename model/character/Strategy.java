package model.character;

/**
 * Strategy interface
 * @author yaoyucui
 */
public interface Strategy{

	public void move(MovingCharacter movingCharacter);

	public int getValue();
	
}
