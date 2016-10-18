package util.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import model.character.Bee;
import model.character.FloatStrategy;
import model.character.FlyStrategy;
import model.character.Log;
import model.character.MovingCharacter;
import model.character.Player;
import model.character.MovingCharacter.Direction;
import model.game.Game;
import model.game.Level;
import model.game.Location;
import model.game.Teleporter;
import model.game.Location.Type;
import model.items.Chest;
import model.items.Fruit;
import model.items.Item;
import model.items.Key;
import model.items.Maps;
import model.items.Treasure;

/**
 * This is test class
 * @author yaoyucui
 */
public class ModelTest {
	
	/**
	 * =============== Test Game ===============
	 */
	@Test
	public void testAddPlayer(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		levels.add(oneLevel);
		
		Game game = new Game(Game.GameStatus.RUNNING, levels, new HashMap<String, Player>(), new HashSet<Integer>());
		game.addPlayer("NP");
		//check successfully add this player in the game
		assertTrue(game.getPlayers().size() == 1);
		assertTrue(game.getPlayers().get(0).getPlayerName().equals("NP"));
	}
	
	@Test
	public void testRemovePlayer(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		levels.add(oneLevel);
		
		Fruit f = new Fruit(99, "a fruit", oneLevel.getLoc(5, 5));
		Key k = new Key(88, "a key", oneLevel.getLoc(9, 9));
		
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		p.addItemToInventory(f);
		p.addItemToInventory(k);
		players.put(p.getPlayerName(), p);
		
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.removePlayer(p);
		
		
		//System.out.println(p.getInventory().size()+" is inventory size");
		assertTrue(game.players.size() == 0);
//		assertTrue(game.players.isEmpty());
	}
	
	@Test
	public void TestEndTheGame(){
		// create game
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		levels.add(oneLevel);
		
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(9, 6));
		p.getLocation().setType(oneLevel.getLoc(9, 6).getType());
		p.setLives(2);
		players.put(p.getPlayerName(), p);
		
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.movePlayer(p.getID(), Direction.NORTH);

		game.gameEndsForThePlayer(p);
		assertTrue(game.getState()==Game.GameStatus.ENDS);
		
	}
	
	
	@Test
	public void TestMovePlayer(){
		Player p = new Player(0, 2, "gotyou");
		p.setLocation(new Location(1, 1, Location.Type.GRASS));
		p.moveUp();
		assertTrue(p.getLocation().getY() == 2);
		p.moveDown();
		assertTrue(p.getLocation().getY() == 1);
		p.moveLeft();
		assertTrue(p.getLocation().getX() == 0);
		p.moveRight();
		assertTrue(p.getLocation().getX() == 1);
	}
	
	@Test

	public void pickUpFruit(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Fruit f = new Fruit(99, "a fruit", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, f);;
		levels.add(oneLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.pickup(p);
		
		assertFalse(oneLevel.hasTheItemOnThisLevel(f));
		assertTrue(p.getInventory().contains(f));
	}
	
	@Test
	public void pickUpTreasure(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Treasure t = new Treasure(99, "treasure", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, t);;
		levels.add(oneLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.pickup(p);
		
		assertFalse(oneLevel.hasTheItemOnThisLevel(t));
		assertTrue(p.getInventory().contains(t));
	}
	
	@Test
	public void chestTest(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Chest c = new Chest(99, "chest", oneLevel.getLoc(5, 5), null);
		oneLevel.addItemLoc(5, 5, c);;
		levels.add(oneLevel);
		Treasure t = new Treasure(98, "treasure", oneLevel.getLoc(5, 5));
		
		c.setContainedItem(t);
		c.setOpen(false);
		
		assertFalse(c.isOpened());
		assertTrue(c.getContainedItem().equals(t));
	}
	
	@Test
	public void ItemTest(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Item c = new Fruit(99, "fruit", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, c);;
		levels.add(oneLevel);
		
		c.setLocation(oneLevel.getLoc(3, 5));
		c.setDescription("a fruit");
		
		
		assertFalse(c.isContainer());
		assertTrue(c.getDescription().equals("a fruit"));
		assertTrue(c.getID() == 99);
		assertTrue(c.getLocation() == oneLevel.getLoc(3,5));
	}
	
	@Test
	public void dropItem(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Fruit f = new Fruit(99, "a fruit", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, f);
		levels.add(oneLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.pickup(p);
		
		game.dropItem(p, f);
		assertTrue(oneLevel.hasTheItemOnThisLevel(f));
		assertFalse(p.getInventory().contains(f));
	}
	
	@Test
	public void TestUseMap(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Maps m = new Maps(99, "a map", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, m);
		levels.add(oneLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.pickup(p);
		
		game.playerUseItem(p, m);
		assertTrue(p.getInventory().contains(m));
		assertTrue(p.hasMap());
		assertTrue(m.isEnableDisplay());
	}
	
	@Test
	public void TestHasAuthorityToUnlock(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Key k = new Key(99, "a key", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, k);
		levels.add(oneLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		p.getLocation().setType(Location.Type.STEP);
		p.pickUpItem(k);
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		
		assertTrue(p.getInventory().contains(k));
		assertTrue(p.hasKey());
		assertTrue(game.hasAuthorityToUnlock(p));
	}
	
	@Test
	public void TestEnterToOtherWorld(){
		List<Level> levels = new ArrayList<Level>();
		Level oneLevel = createLevel();
		Level otherLevel = createLevel();
		Key k = new Key(99, "a key", oneLevel.getLoc(5, 5));
		oneLevel.addItemLoc(5, 5, k);
		levels.add(oneLevel);
		levels.add(otherLevel);
		Map<String, Player> players = new HashMap<String, Player>();
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		p.setLocation(new Teleporter(5, 5, Type.STEP, otherLevel));
		//p.getLocation().setType(Location.Type.STEP);
		p.pickUpItem(k);
		Game game = new Game(Game.GameStatus.RUNNING, levels, players, new HashSet<Integer>());
		game.enterToOtherWorld(p);
		
		assertTrue(otherLevel.equals(p.getCurrentLevel()));
	}
	
	@Test
	public void getLocations(){
		Level oneLevel = createLevel();
		
		Player p = new Player(0, 2, "gotyou");
		p.setCurrentLevel(oneLevel);
		p.setLocation(oneLevel.getLoc(5, 5));
		
		assertFalse(oneLevel.getFrontLoc(p) == p.getLocation());
		assertFalse(oneLevel.getBackLoc(p) == p.getLocation());
		assertFalse(oneLevel.getLeftLoc(p) == p.getLocation());
		assertFalse(oneLevel.getRightLoc(p) == p.getLocation());
	}

	@Test
	public void clearItems(){
		Level oneLevel = createLevel();
		Fruit f = new Fruit(99, "a fruit", oneLevel.getLoc(5, 5));
		Key k = new Key(88, "a key", oneLevel.getLoc(9, 9));
		oneLevel.addItemLoc(5, 5, f);
		oneLevel.addItemToLevel(f);
		oneLevel.addItemLoc(9, 9, k);
		oneLevel.addItemToLevel(k);
		assertFalse(oneLevel.getItems().size() == 0);
		oneLevel.clearItems();
		assertTrue(oneLevel.getItems().size() == 0);

	}
	
	@Test
	public void locEqual(){
		Location one = new Location(3, 3, Location.Type.GRASS);
		Location two = new Location(3, 5, Location.Type.GRASS);
		if(one.equals(two)){
			assertTrue(one.equals(two));
		}
		
	}
	
	@Test
	public void testLevel(){
		Level testLevel = createLevel();
		
		Player p1 = new Player(0, 1, "p1");
		Player p2 = new Player(1, 1, "p2");
		Player p3 = new Player(2, 1, "p3");
		
		Location locationToStart1 = testLevel.setCharacterStartsLocation();
		p1.setLocation(locationToStart1);
		Location locationToStart2 = testLevel.setCharacterStartsLocation();
		p2.setLocation(locationToStart2);
		Location locationToStart3 = testLevel.setCharacterStartsLocation();
		p3.setLocation(locationToStart3);
		assertTrue(p1.getLocation().getX() == locationToStart1.getX());
		assertTrue(p2.getLocation().getX() == locationToStart2.getX());
		assertTrue(p3.getLocation().getX() == locationToStart3.getX());

	}
	
	/**
	 * this is private method to create level
	 * @return
	 */
	private Level createLevel(){
		
		Location[][]locs = new Location[18][18];
		
		for(int i = 0; i<locs.length; i++){
			for(int j = 0; j<locs[0].length; j++){
				
				if(i<=6 || i>=12){
					locs[i][j] = new Location(i, j, Location.Type.GRASS);
				}
				else{
					locs[i][j] = new Location(i, j, Location.Type.WATER);
				}
			}
		}
		Map<Integer, MovingCharacter> chars = new HashMap<Integer, MovingCharacter>();
		Bee b = new Bee(new FlyStrategy(20), new Location(1, 3));
		b.setLocation(locs[1][3]);
		Log l = new Log(new FloatStrategy(10), new Location(1, 6));
		chars.put(1, b);
		chars.put(2, l);
		Level newLevel = new Level(locs, chars);
		return newLevel;
	}
	
	
}
