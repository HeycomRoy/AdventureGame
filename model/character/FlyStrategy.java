package model.character;

import model.game.Location;
import model.game.Point;

/**
 * This Fly Strategy class that changes all fly action
 * @author yaoyucui
 *
 */
		
public class FlyStrategy implements Strategy{

	private int speed;
	
	public FlyStrategy(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	//Move any fly character across the screen
	@Override
	public void move(MovingCharacter character) {
		int dir = (int) (Math.random() * 3);
		
		int across = (int) character.getPoint().getX();
		int upDown = (int) character.getPoint().getY();
		
		int acrossStep = 4;
		int upDownStep = 2;
		
		if (across + acrossStep >= 760)
			across = -60;
		else
			across = across + acrossStep;
		
		// switch the direction randomly to get bee movement
		
		switch (dir) {
		case 1:
			if (upDown + upDownStep >= 114) {
				upDown = 100;
			} else
				upDown = upDown + upDownStep;
			character.setPoint(new Point(across, upDown));
			break;
		case 2:
			
			if (upDown - upDownStep <= 0) {
				upDown = 20;
			} else
				upDown = upDown - upDownStep;
			character.setPoint(new Point(across, upDown));
			break;
		default:
			break;
		}
		
	}

	@Override
	public int getValue() {
		return speed;
	}
	
	

	
}
