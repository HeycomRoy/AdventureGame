package model.items;

import model.game.Location;

/**
 * abstract Item class
 * @author yaoyucui
 */
public abstract class Item {

	private String description;
	private Location loc;
	private int id;
	private boolean container;
	
	/**
	 * Constructor
	 * @param id
	 * @param des
	 * @param loc
	 * @param container
	 */
	public Item(int id, String des, Location loc, boolean container){
		this.description = des;
		this.id = id;
		this.container = container;
	}
	
	public void setDescription(String des){
		this.description=des;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setLocation(Location loc){
		this.loc = loc;
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public int getID(){
		return id;
	}
	
	public boolean isContainer(){
		return container;
	}
}
