package model.character;

import model.game.Location;
/**
 * This is log strategy class that changes of all flowing action
 * @author yaoyucui
 *
 */
public class FloatStrategy implements Strategy {

	private int speed;
	
	public FloatStrategy(int speed) {
		this.speed = speed;
	}
	
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public void move(MovingCharacter character) {
		int newx = character.getLocation().getX() + speed;
		int oldy = character.getLocation().getY();
		character.setLocation(new Location(newx, oldy));
	}


	@Override
	public int getValue() {
		return speed;
	}

}
