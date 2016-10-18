package model.character;

import model.game.Location;

/**
 * This is Bat class that represents a bat object
 * @author yaoyucui
 *
 */
public class Bat extends MovingCharacter{
	Strategy strategy;
	public Bat(Strategy strategy, Location location) {
		super(strategy, location);
		this.strategy = strategy;
	}

	@Override
	public void move() {
		strategy.move(this);
	}
	
	
}
 
