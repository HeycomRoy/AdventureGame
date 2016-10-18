package model.items;

import model.game.Location;

/**
 * This Treasure class represents chest on game board..
 * 
 * @author yaoyucui
 */
public class Treasure extends Item{

	public Treasure(int id, String description, Location loc){
		super(id, description, loc, false);
	}
	
}
