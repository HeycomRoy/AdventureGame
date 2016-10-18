package model.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.character.MovingCharacter;
import model.character.Player;
import model.character.MovingCharacter.Direction;
import model.game.Location.Type;
import model.items.Chest;
import model.items.Item;
import model.items.Maps;
import network.Helper;
import network.OverLord;

/**
 * The Game class, which holds most of game logic.
 *
 * @author yaoyucui
 */
public class Game {

	// map of players, search player by user name.
	public Map<String, Player> players;

	// List of levels, store multiple level worlds.
	public List<Level> worlds;

	// current player level
	//private Level world;

	// used in timing of movement with different character speeds
	private int count;

	private Set<Integer> ids = null;

	// enum of game status
	private GameStatus state = GameStatus.RUNNING;

	public enum GameStatus {
		RUNNING, ENDS, GAMEWON;
	}

	// constructor for making game from save file
	public Game(GameStatus st, List<Level> lv, Map<String, Player> pl, Set<Integer> ids) {
		players = pl;
		worlds = lv;
		state = st;
		this.ids = ids;
	}

	// makes an empty game
	public Game() {
		players = null;
		worlds = null;
		state = null;
	}

	/**
	 * Network constructor make a new game from a byte array
	 */
	public Game(byte[] gameStart) {
		ByteArrayInputStream bais = new ByteArrayInputStream(gameStart);
		DataInputStream dis = new DataInputStream(bais);
		try {
			ids = new HashSet<>();
			// get all data from levels
			int numLevels = dis.readInt();
			worlds = new LinkedList<>();
			for (int i = 0; i < numLevels; ++i) {
				// get all locations - includes items on locations
				int levelSize = dis.readInt();
				Location[][] newGrid = new Location[levelSize][levelSize];
				for (int j = 0; j < levelSize; ++j) {
					for (int k = 0; k < levelSize; ++k) {
						Location location = Helper.locationFromNetwork(dis);

						assert location != null;
						newGrid[j][k] = location;
						if (location.getItem() != null)
							ids.add(location.getItem().getID());
					}
				}

				// get all moving chars
				int charsSize = dis.readInt();
				Map<Integer, MovingCharacter> mcs = new HashMap<>();
				for (int l = 0; l < charsSize; ++l) {
					MovingCharacter mc = Helper.movingCharFromNetwork(dis);
					mcs.put(mc.getID(), mc);
					ids.add(mc.getID());
				}
				Level newLevel = new Level(newGrid, mcs);
				worlds.add(newLevel);
			}

			// get all players
			int numPlayers = dis.readInt();
			players = new HashMap<>();
			for (int m = 0; m < numPlayers; ++m) {
				Player player = Helper.playerFromNetwork(dis);
				if (player != null) {
					players.put(player.getPlayerName(), player);
					ids.add(player.getID());
					for (Item item : player.getInventory()) {
						if (item != null)
							ids.add(item.getID());
					}
				}
			}
			
		} catch (IOException ioe) {
			System.err.println("Failed building game from network");
		}
	}

	/**
	 * This method checks player's lives, and set game status to ends if player
	 * has no remaining lives.
	 *
	 * @param player
	 */
	public void gameEndsForThePlayer(Player player) {
		if (player == null) {
			throw new GameError(
					"gameEndsForThePlayer: set game status to ENDS, but could not able to find the target player...");
		}
		if (!player.isAlive()) {
			setState(GameStatus.ENDS);
		}
	}

	/**
	 * Add new player to the game The player should start from the river level,
	 * therefore to get index 0 world from list of levels.
	 *
	 * @param player
	 */
	public synchronized int addPlayer(String name) {
		if (name == null) {
			throw new GameError("add player: the name should not be null...");
		}

		int rand = (int) (Math.random() * Integer.MAX_VALUE);
		int count = 0;
		while (ids.contains(rand)) {
			System.out.println(count ++);
			rand = (int) Math.random() * Integer.MAX_VALUE;
		}

		ids.add(rand);

		Player newPlayer = new Player(rand, Player.MAX_LIVES, name);
		newPlayer.setLocation(worlds.get(0).getLoc(18, 9));
		newPlayer.setCurrentLevel(worlds.get(0));

		players.put(name, newPlayer);
		return rand;
	}

	/**
	 * This method removes player from the level And place items in the
	 * inventory on the board tiles around the player dead location.
	 *
	 * @param player
	 */
	public synchronized void removePlayer(Player player) {

		if (!players.containsValue(player) && player == null) {
			throw new GameError("removePlayer: No such player found in players list/player could not be null");
		}

		// Place all items from this player's inventory on the map
		// place items around on player dead location
		List<Location> itemPlaceLocation = findAvailableLocation(player);

		int index = 0;
		for(Item i : player.getInventory()){
			itemPlaceLocation.get(index).setItem(i);
			player.removeFromInventory(i);
			index++;
		}

		// remove player from players map
		for (Entry<String, Player> ps : players.entrySet()) {
			// Find match player in the map
			if (ps.getValue().equals(player)) {
				// then remove the player from the map
				players.remove(ps.getKey());
			}
		}
	}

	private List<Location> findAvailableLocation(Player player) {
		Level world = player.getCurrentLevel();
		int playerLocX = player.getLocation().getX();
		int playerLocY = player.getLocation().getY();
		List<Location> availableLocs = new ArrayList<Location>();

		for(int x = playerLocX-1; x < 18 && x>=0;){
			for(int y = playerLocY-2; y< 18 && y>=0; ){
				if(availableLocs.size()<=player.getInventory().size()){
					if(world.getLoc(x, y).getItem() == null && world.getLoc(x, y).getType() == Location.Type.GRASS){
						availableLocs.add(world.getLoc(x,y));
					}
					y++;
				}
				else{
					return availableLocs;
				}
			}
			x++;
		}
		return null;
	}

	///////////////////////////////////////////////////////////////
	// private void setUpNPCs(){
	// npcs = new ArrayList<MovingCharacter>();
	// type filter text FlyStrategy flying = new FlyStrategy(5);
	// LogStrategy logging = new LogStrategy(5);
	//
	// Bee bee = new Bee(flying, new Location(1,2));
	// Log log1 = new Log(logging, new Location(0,7));
	// Log log2 = new Log(logging, new Location(2,8));
	// Log log3 = new Log(logging, new Location(8,8));
	// Log log4 = new Log(logging, new Location(14,9));
	// Log log5 = new Log(logging, new Location(0,10));
	// Log log6 = new Log(logging, new Location(11,10));
	// Alligator alligator = new Alligator(logging, new Location(0,9));
	//
	// npcs.add(bee);
	// npcs.add(log1);
	// npcs.add(log2);
	// npcs.add(log3);
	// npcs.add(log4);
	// npcs.add(log5);
	// npcs.add(log6);
	// npcs.add(alligator);
	// }
	/////////////////////////////////////////////////////////////////

	/**
	 * This method is called when move player by direction to destination
	 * location
	 *
	 * @param playerID
	 * @param dir
	 */
	public void movePlayer(int playerID, Direction dir) {
		// player object gets id form the player
		if (dir == null) {
			throw new GameError("movePlayer: the dirction should not be null.");
		}
		Player p = getPlayer(playerID);
		p.move(dir);
		if (p.getCurrentLevel().checkValidMove(p.getNextLocation())) {
			if(p.isAlive() && p.getLives()>1 && p.getNextLocation().getType() == Type.WATER){
				p.playerLossLife();
				p.setLocation(p.getCurrentLevel().getLoc(9, 18));
			}
			else if(p.isAlive() && p.getLives()>1){
				p.setLocation(p.getNextLocation());
			}
		}
		if(!p.isAlive() || (p.getLives()==1 && p.getNextLocation().getType() == Type.WATER)){
			setState(GameStatus.ENDS);
		}
	}

	/**
	 * This method is called, when the player on the item location. ensures the
	 * inventory still has place ensures the item is able picked up according to
	 * behaviour of each item
	 *
	 * @param player
	 */
	public void pickup(Player player) {
		if (player == null) {
			throw new GameError("pickup: player should not be null.");
		}
		Location playerLocation = player.getLocation();
		if(playerLocation.getType()==Location.Type.GRASS){
			Item item = playerLocation.getItem();
			Level world = player.getCurrentLevel();
			if (item != null && world.hasTheItemOnThisLevel(item)) {
				if (item instanceof Chest) {
					if (((Chest) item).isOpened()) {
						player.pickUpItem(((Chest) item).getContainedItem());
						((Chest) item).setContainedItem(null);
					}
				}
				else{
					player.pickUpItem(item);
					world.removeItemFromThisLevel(item);
				}
			}
		}
		else{
			throw new GameError("No vaild location for pick up item.");
		}
	}

	/**
	 * This method ensures selected item drops on the ground
	 *
	 * @param player
	 * @param selectedItem
	 *            -- the item need to drop
	 */
	public void dropItem(Player player, Item selectedItem) {
		if (player == null || selectedItem == null) {
			throw new GameError("dropItem: This player/item not exit, cannot drop item...");
		}
		Location dropLocation = player.getLocation();
		dropLocation.setItem(selectedItem);
		// To update(remove) item from player inventory
		player.dropItem(selectedItem);
		// add item to this level
		player.getCurrentLevel().addItemToLevel(selectedItem);
		player.getLocation().setItem(selectedItem);
	}

	/**
	 * This method check player's inventory has map item
	 *
	 * @param player
	 * @param item
	 */
	public void playerUseItem(Player player, Item item) {
		if (player == null || item == null) {
			throw new GameError("playerUseMaps: player/ item should not be a null valuer.");
		}
		// check if maps pick up by player, if has maps in the player's
		// inventory then display the map

		if (item instanceof Maps) {
			Maps map = (Maps) item;
			boolean displayMap = player.hasMap();
			map.setEnableDisplay(displayMap);
		}
	}

	/**
	 * This method will check the player has autority to unlock teleporter.
	 *
	 * @param player
	 * @return -- true if player is able to use key -- false player cannot use
	 *         key
	 */
	public boolean hasAuthorityToUnlock(Player player) {
		if (player == null) {
			throw new GameError("hasAuthorityToUnlock: player should not be null.");
		}
		Location currentLoc = player.getLocation();
		// if current location is not door returns false
		if (currentLoc.getType() != Type.STEP) {
			return false;
		}
		// unlock entrance to other world
		return player.hasKey();
	}

	/**
	 * This method will allowed player to enter to other level.
	 *
	 * @param player
	 */
	public void enterToOtherWorld(Player player) {
		if (player == null) {
			throw new GameError("enterToOtherWorld: player should not be null.");
		}
		Location currentLoc = player.getLocation();
		if (currentLoc.getType() != Location.Type.STEP) {
			throw new GameError("This is not Teleporter tile...");
		} else if (currentLoc instanceof Teleporter && hasAuthorityToUnlock(player)) {
			Teleporter entrance = (Teleporter) currentLoc;
			Level newWorld = entrance.getOtherWorld();
			player.setLevel(newWorld);
		}
	}

	public synchronized void clockTick() {

		for (Level l : worlds) {
			for (MovingCharacter mc : l.getCharacters()) {
				mc.move();
			}
		}
		for (Map.Entry<String, Player> player : players.entrySet()) {
			player.getValue().move();
		}
		
		
		if (players.size() == 0) {
			setState(GameStatus.ENDS);
		}
		// TODO -- How to won the game
	}

	/**
	 * Create a byte array representing the entire game to be sent once at the
	 * beginning of the connection data must be sufficient to create the whole
	 * game from scratch
	 *
	 * |..|..|item|player|number of players|..||..|moving character|number of
	 * chars|..||item|location|size of level||number of levels|
	 */
	public synchronized byte[] toNetworkStatic() {

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			// write number of levels
			int worldSize = worlds.size();
			dos.writeInt(worldSize);
			// for each level
			for (int a = 0; a < worldSize; ++a) {
				Level level = worlds.get(a);
				// write size of level => number of locations
				int levelSize = level.getLocations().length;
				dos.writeInt(levelSize);
				// for each location
				for (int r = 0; r < levelSize; ++r) {
					for (int c = 0; c < levelSize; ++c) {
						// write location
						dos.write(Helper.locationToNetwork(level.getLocations()[r][c]));
					}
				}
				// write size and all characters
				List<MovingCharacter> mcs = level.getCharacters();
				dos.writeInt(mcs.size());
				for (MovingCharacter mc : mcs) {
					dos.write(Helper.movingCharToNetworkInit(mc));

				}
			}
			// write size and all players
			dos.writeInt(players.size());

			for (Player player : getPlayers()) {
				dos.write(Helper.playerToNetwork(player));
			}

		} catch (Exception e) {
			System.err.println("Failed making game byte array");
		}
		return baos.toByteArray();
	}

	/**
	 * creates a byte array of the changing parts of the game ..|items|items
	 * size|..|moving chars|each level|..|player|players size|
	 *
	 */
	public synchronized byte[] toNetworkDynamic() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			// do players from game
			dos.writeInt(players.size());
			for (Map.Entry<String, Player> entry : players.entrySet()) {
				Player player = entry.getValue();
				dos.write(Helper.playerToNetwork(player));
			}
			for (Level level : worlds) {
				// do moving chars from level
				for (MovingCharacter mc : level.getCharacters()) {
					dos.write(Helper.movingCharToNetwork(mc));
				}
				// do items out on the map
				Map<Location, Item> items = level.getItemsMap();
				dos.writeInt(items.size());
				for (Map.Entry<Location, Item> entry : items.entrySet()) {
					dos.write(Helper.itemToNetworkDynamic(entry.getValue(), entry.getKey().getX(), entry.getKey().getY()));
				}
			}
		} catch (IOException ioe) {
			System.err.println("Failed writing game state");
		}
		return baos.toByteArray();
	}

	// recreate the state of the game from the byte array
	public synchronized void fromNetworkDynamic(byte[] state) {
		ByteArrayInputStream bais = new ByteArrayInputStream(state);
		DataInputStream dis = new DataInputStream(bais);
		try {
			int playersSize = dis.readInt();
			for (int a = 0; a < playersSize; ++ a) {
				Player newPlayer = Helper.playerFromNetwork(dis);
				players.put(newPlayer.getPlayerName(), newPlayer);
				ids.add(newPlayer.getID());
			}
			for (Level level : worlds) {
				Map<Integer, MovingCharacter> mcs = level.getMovingCharacters();
				for (int i = 0; i < mcs.size(); ++ i) {
					int[] charData = Helper.movingCharFromNetworkDynamic(dis);
					if (mcs.containsKey(charData[0])) {
						MovingCharacter mc = mcs.get(charData[0]);
						mc.setPoint(new Point(charData[1], charData[2]));
					}
				}
				int itemsSize = dis.readInt();
				level.clearItems();
				for (int j = 0; j < itemsSize; ++j) {
					Helper.itemFromNetworkLevelDynamic(dis, level);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ================================ Getter & Setter ================================
	 */

	/**
	 * This method returns current game status
	 *
	 * @return -- GameStatus
	 */
	public GameStatus getState() {
		return state;
	}

	/**
	 * This method sets current game status
	 *
	 * @param state
	 *            --GameStatus
	 */
	public void setState(GameStatus state) {
		this.state = state;
	}

	public List<Level> getLevels() {
		return this.worlds;
	}

	public List<Player> getPlayers() {
		List<Player> pl = new LinkedList<>();
		for (Map.Entry<String, Player> e : players.entrySet()) {
			pl.add(e.getValue());
		}
		return pl;
	}

	public Player getPlayer(int ID) {
		Player player = null;
		for(Player p : players.values()){
			if(p.getID() == ID){
				player = p;
			}
		}

		if (player != null) {
			return player;
		}
		throw new GameError("This player is not found...");
	}

	// gets player's inventory
	public List<Item> getPlayerItems(Player player) {
		return player.getInventory();
	}

	public int getPlayerRemainingLives(Player player) {
		return player.getLives();
	}

	/*
	 * Find if a given player exists
	 */
	public boolean hasUser(String username) {
		return players.containsKey(username);
	}

	public int getPlayerID(String username) {
		if (hasUser(username))
			return players.get(username).getID();
		else
			return -1;
	}

	public synchronized void printGame() {
		System.out.println("Number of players : " + players.size());
		for (Map.Entry<String, Player> entry : players.entrySet()) {
			System.out.println("Player : " + entry.getValue().getID() + "\nUsername : " + entry.getKey());
		}
		System.out.println("Number of levels : " + worlds.size());
		int index = 0;
		for (Level level : worlds) {
			System.out.println("Level : " + ++ index);
			System.out.println("Size : " + level.getLocations().length);
			System.out.println("Moving characters : " + level.getMovingCharacters().size());
			for (MovingCharacter mc : level.getCharacters()) {
				System.out.println("Class : " + mc.getClass().getName() + "\nID : " +mc.getID() + "\nX = " + mc.getPoint().x + " : Y = " + mc.getPoint().y);
			}
		}
	}
}