package model.character;

import java.util.ArrayList;
import java.util.List;

import model.game.Level;
import model.game.Location;
import model.game.Point;
import model.items.Item;
import model.items.Key;
import model.items.Maps;

/**
 *This is player class holds all fields for player
 * @author yaoyucui
 *
 */

public class Player extends MovingCharacter{

	private static final int MAX_ITEMS = 16;
	private static final int STEP = 1;
	public static final int MAX_LIVES = 3;
	
	//remanning lives
	private int lives;
	
	//is the player still alive
	private boolean isAlive;
	
	private int playerID;
	private String playerName;
	
	//inventory list stores all items in player's inventory
	private List<Item> inventory;
	
	//private Location playerLocation;
	private Location nextLocation;
	
	//player current location 
	private Level currentLevel;
	
	public boolean newPlayer = true;

	/**
	 * Player constructor
	 * @param playerID
	 * @param lives
	 * @param playerName
	 */
	public Player(int playerID, int lives, String playerName){

		this.playerID = playerID;
		this.playerName = playerName;
		this.lives = lives;
		this.nextLocation = null;

		this.isAlive = true;
		inventory = new ArrayList<Item>();

	}

	//return true if player still alive
	public boolean isAlive(){
		return isAlive;
	}
	
	/**
	 * if player loss life updates remaining lives field
	 */
	public void playerLossLife(){
		if(lives > 0){
			lives--;
		}
		else if(lives == 0){
			isAlive = false;
		}
	}
	
	public void setIsAlive(boolean boo){
		this.isAlive = boo;
	}
	
	/**
	 * Check if there is space in the inventory
	 */
	public boolean inventoryHasSpace(){
		return inventory.size()<MAX_ITEMS;

	}


	//Move four directions by calling each method
	public void moveUp(){
		
		int nextStep = getLocation().getY()+STEP;
		this.setLocation(new Location(getLocation().getX(), nextStep));
	}
	public void moveDown(){
		int nextStep = getLocation().getY()-STEP;
		this.setLocation(new Location(getLocation().getX(), nextStep));
	}
	public void moveLeft(){
		int nextStep = getLocation().getX()-STEP;
		this.setLocation(new Location(nextStep, getLocation().getY()));
	}
	public void moveRight(){
		int nextStep = getLocation().getX()+STEP;
		this.setLocation(new Location(nextStep, getLocation().getY()));
	}
	//Move player passing direction as parameter
	public void move(Direction d) {
		
        switch (d) {
        case NORTH :
        	int stepUp = getLocation().getY()-STEP;        	
        	setNextLocation(this.currentLevel.getLoc(getLocation().getX(), stepUp));
        	break;
        case SOUTH :
        	int stepDown = getLocation().getY()+STEP;
        	setNextLocation(this.currentLevel.getLoc(getLocation().getX(), stepDown));
        	break;
        case WEST :
        	int stepLeft = getLocation().getX()-STEP;
        	setNextLocation(this.currentLevel.getLoc(getLocation().getX(), stepLeft));
        	break;
        case EAST :
        	int stepRight = getLocation().getX()+STEP;
        	setNextLocation(this.currentLevel.getLoc(getLocation().getX(), stepRight));
        	break;
        default:
        	break;
        }
	}



	/**
	 * check inventory is full, if not add item to inventory
	 * @param item
	 * @return
	 */
	public boolean pickUpItem(Item item){
		if(item instanceof Item && isAlive){
			if(inventory.size() <= MAX_ITEMS){
				return inventory.add(item);
			}
			else{
				return false;
			}
		}
		return false;
	}

	/**
	 * drop item removes item for inventory
	 * @param item
	 * @return
	 */
	public boolean dropItem(Item item){
		
		if(!(item instanceof Item) || !isAlive){
			return false;
		}
		if(this.getLocation().getType() == Location.Type.GRASS && isAlive){	
			return inventory.remove(item);
		}
		return false;
	}
	
	/**
	 * has map in player's inventory
	 * @return
	 */
	public boolean hasMap() {
		for(Item item : inventory){
			if(item instanceof Maps){
				return true;
			}
		}
		return false;
	}

	/**
	 * has key in player's inventory
	 * @return
	 */
	public boolean hasKey(){
		for(Item item : inventory){
			if(item instanceof Key){
				return true;
			}
		}
		return false;
	}

	/**
	 * Printing out description and ID for each of item in inventory
	 * @return
	 */
	public String printInventory() {
		String inv="";
		for(Item i : inventory){
			inv = inv+i.getDescription()+i.getID()+",";
		}
		return "";
	}
	
	/**
	 * add/remove item in player's inventory
	 * @param item
	 */
	public void addItemToInventory(Item item){
		inventory.add(item);
	}
	
	public void removeFromInventory(Item item) {
		for(Item i : inventory){
			inventory.remove(i);
		}
	}
	
	/*
	 * ================================ Getter & Setter ================================
	 */
	
	public Location getNextLocation(){
		return this.nextLocation;
	}
	
	public void setNextLocation(Location nextLoc){
		this.nextLocation = nextLoc;
	}
	
	public int getID() {
        return this.playerID;
	}

	public int getLives(){
		return this.lives;
	}

	public void setLives(int value) {
		this.lives = value;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public Level getCurrentLevel(){
		return currentLevel;
	}

	@Override
	public void move() {}

	public void setLevel(Level lev){
		this.currentLevel = lev;
	}

	public String getPlayerName(){
		return playerName;
	}

	/*
	 * Sets the current level of the player
	 */
	public void setCurrentLevel(Level level) {
		currentLevel = level;		
	}

	

}
