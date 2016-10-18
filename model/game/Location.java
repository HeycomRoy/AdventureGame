package model.game;

import model.items.Item;

/**
 * Class representing a square tile logically in game Each location has a type
 * and optionally a tile may hold an item
 *
 * @author yaoyucui
 */
public class Location extends Point{
	
	//item on the location
	private Item item;
	
	//Type of this tile
	private Type type;
	
	private String tileStr;
	
	/**
	 * ========================================================
	 * this enum responsable for different type of tiles
	 * ========================================================
	 * GRASS --- grass tile
	 * TREE --- tree tile 
	 * WATER --- water tile 
	 * BRIDGE --- bridge 
	 * ROAD --- black road tile 
	 * ROADSTRIPE --- black with stripe 
	 * STONE --- stone tile 
	 * STEP --- step tile
	 */
	public enum Type {
		GRASS /* FINE */, TREE /* NOGO */, MOUNTAIN /* NOGO */, WATER /* DEAD */, 
		BRIDGE /* EL */, ROAD /* FINE */, ROADSTRIPE /* FINE */, STONE /* FINE */, STEP/* STEP */
	}

	public Location(int x, int y, Type t) {
		super(x, y);
		type = t;
	}

	public Location(int x, int y) {
		super(x, y);
	}

	public Location(Type locType, Item i) {
		super(0, 0);//TODO should not pass as zero
		type = locType;
		item = i;
	}

	public Location(int x, int y, String tileStr) {
		super(x, y);
		this.tileStr = tileStr;

	}

	/*
	 * ================================ Getter & Setter ================================
	 */
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item i) {
		item = i;
	}

	public String getTileStr() {
		return tileStr;
	}

	// just checks if same location.
	public boolean equals(Object o) { 
		if (o instanceof Location && ((Location) o).type == this.type) {
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		String coordinates = "x: "+x+" y: "+y+"\n";
		String itemName = "item is : "+item.getDescription()+"\n";
		return coordinates+itemName;
	}
}
