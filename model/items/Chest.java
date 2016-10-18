package model.items;

import model.game.Location;

/**
 * This is a chest class holds all fields of chest
 * ensures is opened already
 * ensures contain a item object
 * @author yaoyucui
 *
 */
public class Chest extends Item{

	private boolean isOpened;
	private Item containedItem;
	
	/**
	 * constructor
	 * @param id
	 * @param description
	 * @param loc
	 * @param item
	 */
	public Chest(int id, String description, Location loc, Item item){
		super(id, description, loc, true);
		this.setContainedItem(item);
	}
	
	public void setOpen(boolean setStatus){
		this.isOpened=setStatus;
	}
	
	public boolean isOpened(){
		return isOpened;
	}

	public Item getContainedItem() {
		return containedItem;
	}

	public void setContainedItem(Item containedItem) {
		this.containedItem = containedItem;
	}
	
	
}
