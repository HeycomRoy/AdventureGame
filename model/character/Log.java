package model.character;

import model.game.Location;
import model.game.Point;
/**
 * This is Log class that represents a log object
 * @author yaoyucui
 *
 */
public class Log extends MovingCharacter{

	Strategy strategy;
	public Log(Strategy strategy, Location location) {
		super(strategy, location);
		this.strategy = strategy;
		mcID = "L";

	}

	@Override
	public void move() {
		strategy.move(this);
	}

}
