package model.items;

import model.game.Location;

/**
 * 
 * @author yaoyucui
 *
 */
public class Fruit extends Item{

	public static final int HEAL = 4;
	

	public Fruit(int id, String description, Location loc){
		super(id, description, loc, false);
	}
	
}
