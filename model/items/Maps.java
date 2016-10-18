package model.items;

import model.game.Location;

/**
 * Maps class that represents a map object
 * @author yaoyucui
 *
 */
public class Maps extends Item{
	
	boolean enableDisplay;
	
	/**
	 * constructor
	 * @param id
	 * @param description
	 * @param loc
	 */
	public Maps(int id, String description, Location loc){
		super(id, description, loc, false);
	}
	public boolean isEnableDisplay() {
		return enableDisplay;
	}

	public void setEnableDisplay(boolean enableDisplay) {
		this.enableDisplay = enableDisplay;
	}
	
	
}
