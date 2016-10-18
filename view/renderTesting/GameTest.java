package view.renderTesting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.character.Alligator;
import model.character.Bee;
import model.character.FloatStrategy;
import model.character.FlyStrategy;
import model.character.Log;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Game;
import model.game.Level;
import model.game.Location;
import model.items.Item;
import util.Parser;

public class GameTest {
	private Map<String, Player> players;

	private List<Level> levels;

	private Level currentLevel;

	// used in timing of movement with different character speeds
	private int count;

	private Set<Integer> ids = null;

	private Game.GameStatus state;
	
	private TestFrame gameFrame;

	public enum GameStatus {
		RUNNING, ENDS, GAMEWON;
	}

	public GameTest(Game.GameStatus status, List<Level> levels, Map<String, Player> players, Set<Integer> ids) {
		this.state = status;
		this.levels = levels;
		this.players = players;
		this.ids = ids;

		String name = "Bert";
		String name2 = "Archie";

		addPlayer(name2);
		addPlayer(name);
		
		currentLevel = levels.get(0);

		//setupMCs();

		gameFrame = new TestFrame(this, null);
		
		Clock ticker = new Clock(50, this);
		ticker.start();

	}
//========================== CLOCK TICK ===================================
	/**
	 * Provides a clock tick for movement of game objects
	 * 
	 */
	public synchronized void clockTick() {
		count++;
		if (count == 1001)
			count = 0;
		
		for (Map.Entry<Integer, MovingCharacter> entry : currentLevel.getMovingCharacters().entrySet()) {
			MovingCharacter mc = entry.getValue();
			if (count == 0) mc.move();
			else if (count % 23 == 0 && mc.getSpeed() == 23) {
				mc.move();
			} else if (count % 19 == 0 && mc.getSpeed() == 19) {
				mc.move();
			} else if (count % 7 == 0 && mc.getSpeed() == 7) {
				mc.move();
			} else if (count % 3 == 0 && mc.getSpeed() == 3) {
				mc.move();
			} else if (mc.getSpeed() == 1) {
				mc.move();
			}
			//System.out.println("MC: " + mc.getMCID() + " x: " + mc.getLocation().getX() 
				//	+ "," + mc.getLocation().getY());
		}
		
		gameFrame.reFresh();
	
	}

	
	public List<Level> getLevels() {
		return levels;
	}

	public void printOutLocations() {
		Location[][] locationArray = levels.get(1).getLocations();
		for (int i = 0; i < locationArray.length; i++) {
			for (int j = 0; j < locationArray[i].length; j++) {
				System.out.println(i + "," + j + " has " + locationArray[j][i].getType());
			}
		}

	}

	public int addPlayer(String name) {
		int rand = (int) Math.random() * Integer.MAX_VALUE;
		if (ids.contains(rand)) {
			rand = (int) Math.random() * Integer.MAX_VALUE;
		}
		ids.add(rand);
		players.put(name, new Player(rand, Player.MAX_LIVES, name));

		return rand;
	}

	public Player getAPlayer() {

		return players.get("Bert");
	}

	public void pickup(Player player) {

	}

	public void playerUseItem(Player player, Item selectedItem) {

	}

	public void dropItem(Player player, Item selectedItem) {

	}

	private void setupMCs() {
		
		Map<Integer, MovingCharacter> mcsFromLevel = currentLevel.getMovingCharacters();

		FlyStrategy fs = new FlyStrategy(5);
		FloatStrategy ls = new FloatStrategy(5);

		//Alligator alli = new Alligator(fs, new Location(0, 8));
		Bee bee = new Bee(fs, new Location(0, 3));
//		Log log1 = new Log(ls, new Location(8, 8));
//		Log log2 = new Log(ls, new Location(2, 9));
//		Log log3 = new Log(ls, new Location(12, 10));
//		Log log4 = new Log(ls, new Location(3, 11));
//		Log log5 = new Log(ls, new Location(14, 12));
//		Log log6 = new Log(ls, new Location(14, 9));

		//mcsFromLevel.put(1, alli);
		mcsFromLevel.put(2, bee);
//		mcsFromLevel.put(3, log1);
//		mcsFromLevel.put(4, log2);
//		mcsFromLevel.put(5, log3);
//		mcsFromLevel.put(6, log4);
//		mcsFromLevel.put(7, log5);
//		mcsFromLevel.put(8, log6);

	}
	
	
	public Level getCurrentLevel(){
		return currentLevel;
	}
	
	

	// ========================== MAIN =========================

	public static void main(String[] args) throws Exception {

		String filename = "src/util/multiLevelGame.xml";

		File gameFile = new File(filename);

		GameTest gt = ParserX.load(gameFile);
		
		}

}

