package model.character;
import model.game.Location;
import model.game.Point;

/**
 * This is Bee class that represents a bee object
 * @author yaoyucui
 *
 */

public class Bee extends MovingCharacter{
	Strategy strategy;
	public Bee(Strategy strategy, Location location) {
		super(strategy, location);
		this.strategy = strategy;

	}
	
	@Override
	public void move() {
		strategy.move(this);
	}

}