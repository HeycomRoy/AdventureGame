package util.tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.Test;

import main.Main;
import model.character.Bee;
import model.character.FlyStrategy;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Game;
import model.game.Level;
import model.game.Location;
import model.game.Point;
import model.game.Game.GameStatus;
import model.game.Location.Type;
import model.items.Chest;
import model.items.Fruit;
import model.items.Item;
import model.items.Treasure;
import util.Parser;
import util.Recorder;

/**
 * Tests for the static methods used for save game functionality
 * All tests check that the output string consists of all the same tokens
 * 
 *  @author forancono
 */
public class RecorderTests {
	
	/**
	 * Tests the output from passing in a known game
	 */
	@Test
	public void test0_saveGame() {
		Game game = new Game(GameStatus.RUNNING, new LinkedList<>(), new HashMap<>(), new HashSet<>());
		String expected = "<game state=\"0\" level=\"0\" >\n\t</game>";
		String output = Recorder.gameString(game);
		checkTokens(expected, output);
	}
	
	/**
	 * Tests the output from passing in a known level
	 */
	@Test
	public void test1_saveLevel() {
		Level level = new Level(new Location[][]{{new Location(Type.GRASS, null)}}, new HashMap<>());
		String expected = "<level size=\"1\" > <location type=\"grass\" /> </level>";
		String output = Recorder.levelString(level, 0);
		checkTokens(expected, output);
	}
	
	/**
	 * Tests the output from passing in a known location
	 */
	@Test
	public void test2_saveLocation() {
		Location location = new Location(Type.MOUNTAIN, null);
		String expected = "<location type=\"mountain\" />";
		String output = Recorder.locationString(location, 0);
		checkTokens(expected, output);
		
		Location location1 = new Location(Type.BRIDGE, new Treasure(0, Parser.TREASURE, null));
		String expected1 = "<location type=\"bridge\" >\n\t"
				+ "<item id=\"0\" type=\"treasure\" container=\"false\" />\n"
				+ "</location>";
		String output1 = Recorder.locationString(location1, 0);
		checkTokens(expected1, output1);
	}
	
	/**
	 * Tests the output from passing in a known player
	 */
	@Test
	public void test3_savePlayer() {
		Player player1 = new Player(0, Player.MAX_LIVES, Main.DEFAULT_USER);
		player1.setPoint(new Point(0, 0));
		List<Level> level = new LinkedList<>();
		level.add(new Level(null, null));
		String expected1 = "<player id=\"0\" username=\"David Pearce\" level=\"0\" lives=\"3\" x=\"0\" y=\"0\" > </player>";
		String output1 = Recorder.playerString(player1, level, 0);
		checkTokens(expected1, output1);
	}
	
	/**
	 * Tests the output from passing in a known moving character
	 */
	@Test
	public void test4_saveMovingCharacter() {
		MovingCharacter mc = new Bee(new FlyStrategy(1), null);
		mc.setPoint(new Point(2, 3));
		mc.setID(0);
		String expected = "<character id=\"0\" type=\"bee\" strategyVariable=\"1\" x=\"2\" y=\"3\" />";
		String output = Recorder.characterString(mc, 0);
		checkTokens(expected, output);
	}
	
	/**
	 * Tests the output from passing in a known item
	 */
	@Test
	public void test5_saveItem() {
		Item a = new Fruit(0, Parser.FRUIT, null);
		String expectedA = "<item id=\"0\" type=\"fruit\" container=\"false\" />";
		String outputA = Recorder.itemString(a, 0);
		checkTokens(expectedA, outputA);
		
		Item b = new Chest(0, Parser.CHEST, null, a);
		String expectedB = "<item id=\"0\" type=\"chest\" container=\"true\" >\n" + expectedA + " </item>";
		String outputB = Recorder.itemString(b, 0);
		checkTokens(expectedB, outputB);
	}
	
	/*
	 * Helper methods
	 */
	
	/**
	 * Runs each input along a scanner checking each token for string equality
	 * 
	 * @param expected - the desired output
	 * @param output - the actual output
	 */
	
	public void checkTokens(String expected, String output) {
		try {
		Scanner a = new Scanner(expected);
		Scanner b = new Scanner(output);
		while (b.hasNext()) {
			String tokenA = a.next();
			String tokenB = b.next();
			if (!tokenA.equals(tokenB)) {
				System.err.println("Expecting : " + tokenA + " - Received : " + tokenB);
			}
			assert tokenA.equals(tokenB);
		}
		a.close();
		b.close();
		} catch (NoSuchElementException nsee) {
			System.err.println("Number of tokens unequal");
			assert false;
		}
	}
}
