package model.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.character.MovingCharacter;
import model.character.Player;
import model.game.Location.Type;
import model.items.Fruit;
import model.items.Item;
import model.items.Key;
import model.items.Maps;
import model.items.Treasure;

/**
 * This is level class which represents different world. Holds all components
 * needed on each level.
 * 
 * @author yaoyucui & holderorio
 */
public class Level {

	// board width and height
	private static final int WIDTH = 18;
	private static final int HEIGHT = 18;
	
	//enums represents different level
	public enum LevelType {
		PASTURE, CAVE, MOUNTAIN
	}
	
	LevelType levelType;

	private String filename;

	// Board size of game map
	private Location[][] locations; // 18 x 18

	// List of items on the level.
	private List<Item> mapItems = new ArrayList<Item>();

	//stores characters on this level
	private Map<Integer, MovingCharacter> characters;

	/**
	 * Load in a level from a csv file in the format 'x, y, tile, *optional* NPC
	 * or Character' new line
	 * 
	 * @param filename
	 */
	public Level(String filename) {

		this.filename = filename;

		locations = new Location[18][18];
		mapItems = new ArrayList<Item>();

		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(filename));

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] locInfo = line.split(",");

				// First two values are the x and y
				int x = Integer.parseInt(locInfo[0]);
				int y = Integer.parseInt(locInfo[1]);

				// Third value is the base layer tile
				switch (locInfo[2]) {
				case "G":
					locations[x][y] = new Location(x, y, Location.Type.GRASS);
					break;
				case "R":
					locations[x][y] = new Location(x, y, Location.Type.WATER);
					break;
				case "M":
					locations[x][y] = new Location(x, y, Location.Type.MOUNTAIN);
					break;
				case "W":
					locations[x][y] = new Location(x, y, Location.Type.WATER);
					break;
				case "B":
					locations[x][y] = new Location(x, y, Location.Type.BRIDGE);
					break;
				case "T":
					locations[x][y] = new Location(x, y, Location.Type.TREE);
					break;
				case "E":
					locations[x][y] = new Location(x, y, Location.Type.GRASS);
					break;
				default:
					locations[x][y] = new Location(x, y, Location.Type.GRASS);

				}

				/*
				 * If the array's length is longer than 3 then that means there
				 * is an item or character on that square so they should be
				 * loaded/placed appropriately
				 */

			

				if (locInfo.length > 3) {

					switch (locInfo[3]) {
					case "Key":
						mapItems.add(new Key(1, "Key", locations[x][y]));
					case "Map":
						mapItems.add(new Maps(1, "Map", locations[x][y]));
					case "Fruit":
						mapItems.add(new Fruit(1, "Fruit", locations[x][y]));
					case "Treasure":
						mapItems.add(new Treasure(1, "Treasure", locations[x][y]));
					case "Bee":

						
					}

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public Level(Location[][] grid, Map<Integer, MovingCharacter> characters) {
		this.locations = grid;
		this.characters = characters;
	}
	
	/**
	 * gets location infront of player
	 * @param player
	 * @return
	 */
	public Location getFrontLoc(Player player) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY() + 1;
		return getLoc(x, y);
	}
	
	/**
	 * gets location behind of player
	 * @param player
	 * @return
	 */
	public Location getBackLoc(Player player) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY() - 1;
		return getLoc(x, y);
	}

	/**
	 * gets location left side of player
	 * @param player
	 * @return
	 */
	public Location getLeftLoc(Player player) {
		int x = player.getLocation().getX() - 1;
		int y = player.getLocation().getY();
		return getLoc(x, y);
	}

	/**
	 * gets location right side of player
	 * @param player
	 * @return
	 */
	public Location getRightLoc(Player player) {
		int x = player.getLocation().getX() + 1;
		int y = player.getLocation().getY();
		return getLoc(x, y);
	}

	/**
	 * checks valid move
	 * @param targetloc
	 * @return
	 */
	public boolean checkValidMove(Location targetloc) {
		if (targetloc.getType() == Location.Type.GRASS || targetloc.getType() == Location.Type.BRIDGE
				|| targetloc.getType() == Location.Type.STEP || targetloc.getType() == Location.Type.WATER)
			return true;
		else
			return false;
		
	}
	
	//For locate player location
	public Location setCharacterStartsLocation(){
		
		int ran = (int)(Math.random() * ((18 - 1) + 1)) + 1;
		Location loc = locations[ran][17];
		if(loc.getType() == Location.Type.GRASS ){
			if(loc.getItem() == null){
				return loc;
			}
			loc = locations[ran][17];
		}
		return loc;

	}

	/**
	 * add item on the location
	 * @param x
	 * @param y
	 * @param item
	 */
	public void addItemLoc(int x, int y, Item item){
		if(x>WIDTH||x<0 || y<0 ||y>HEIGHT){
			throw new GameError("addToLoc: not valid x and y coordinates.");
		}
		if(locations[x][y].getType() == Type.GRASS){
			addItemToLevel(item);
			locations[x][y].setItem(item);
		}
	}
	
	/**
	 * get location by coordinates x, y
	 * @param x
	 * @param y
	 * @return returns Location, return null if out of game world range.
	 */
	public Location getLoc(int x, int y) {
		if (x < 0 || x >= WIDTH)
			return null;

		if (y < 0 || y >= HEIGHT)
			return null;
		return locations[x][y];
	}

	/**
	 * checks has this item one this level
	 * @param item
	 * @return
	 */
	public boolean hasTheItemOnThisLevel(Item item) {

		for (Item i : mapItems) {
			if (i.equals(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * removes item from this level
	 * @param item
	 */
	public void removeItemFromThisLevel(Item item) {
		if (!mapItems.isEmpty()) {
			mapItems.remove(mapItems.indexOf(item));
		} else {
			throw new GameError("Player's inventory is empty, no item to remove...");
		}
	}

	/*
	 * Method to extract a list of characters from their map
	 */
	public List<MovingCharacter> getCharacters() {
		List<MovingCharacter> mcs = new LinkedList<>();
		for (Map.Entry<Integer, MovingCharacter> entry : characters.entrySet()) {
			mcs.add(entry.getValue());
		}
		return mcs;
	}

	public Map<Integer, MovingCharacter> getMovingCharacters() {
		return characters;
	}
	

	/**
	 * This method returns all Item on this level
	 * @return -- List of Item
	 */
	public List<Item> getItems() {
		List<Item> items = new LinkedList<>();
		for (int i = 0; i < locations.length; ++i) {
			for (int j = 0; j < locations.length; ++j) {
				Item item = locations[i][j].getItem();
				if (item != null)
					items.add(item);
			}
		}
		return items;
	}

	/**
	 * This method gets all Items coordinates on the level
	 * @return --Map<Point, Item>
	 */
	public Map<Location, Item> getItemsMap() {
		Map<Location, Item> items = new HashMap<>();
		for (int i = 0; i < locations.length; ++i) {
			for (int j = 0; j < locations.length; ++j) {
				Item item = locations[i][j].getItem();
				if (item != null) {
					items.put(new Location(i, j), item);
				}
			}
		}
		return items;
	}

	/**
	 * This method to set all Items Location to null.
	 */
	public void clearItems() {
		for (int i = 0; i < locations.length; ++i) {
			for (int j = 0; j < locations.length; ++j) {
				locations[i][j].setItem(null);
			}
		}
	}

	/*
	 * ================================ Getter & Setter ================================
	 */

	public void addItemToLevel(Item item) {
		mapItems.add(item);
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public LevelType getLevelType() {
		return levelType;
	}

	public void setLevelType(LevelType levelType) {
		this.levelType = levelType;
	}

	public String getFilename() {
		return filename;
	}

	public Location[][] getLocations() {
		return locations;
	}

	public void setLocations(Location[][] locations) {
		this.locations = locations;
	}

	@Override
	public String toString(){
		int Locleng = locations.length;
		return "locations array length: "+Locleng;
	}
	
}