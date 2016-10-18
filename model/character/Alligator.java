package model.character;

import model.game.Location;
import model.game.Point;

/**
 * This is Alligator Class that represents alligator object
 *
 */
public class Alligator extends MovingCharacter {
	Strategy strategy;
	public Alligator(Strategy strategy, Location location) {
		super(strategy, location);
		this.strategy= strategy;
		mcID = "A";
	}

	@Override
	public void move() {
		this.strategy.move(this);
	}


}
