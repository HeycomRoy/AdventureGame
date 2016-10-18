package util.tests;

import java.util.Scanner;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import model.character.MovingCharacter;
import model.character.Player;
import model.game.Game;
import model.game.Level;
import model.game.Location;
import model.items.Item;
import util.Parser;

/**
 * Tests for load game functionality
 * 
 * @author forancono
 */

@FixMethodOrder(MethodSorters.JVM)
public class ParserTests {
	
	public static final String EMPTY_STRING = "";
	
	// test cases which should provide correct output
	public static final String[] goodGames = {
			"<game state=\"0\" level=\"0\" >\n</game>"
			};
    public static final String[] goodLevels = {
    		"<level size=\"1\" >\n\t<location type=\"grass\" >\n\t</location>\n</level>",
    		};
	public static final String[] goodLocations = {};
    public static final String[] goodPlayers = {};
	public static final String[] goodMovingCharacters = {};
	public static final String[] goodItems = {
			"<item id=\"0\" type=\"key\" container=\"false\" />", 
			"<item id=\"0\" type=\"chest\" container=\"true\" >\n"
			+ "\t<item id=\"0\" type=\"key\" container=\"false\" />\n"
			+ "</item>",
			"<item id=\"0\" type=\"chest\" container=\"true\" >\n"
			+ "\t<item id=\"0\" type=\"chest\" container=\"true\" >\n"
			+ "\t\t<item id=\"0\" type=\"key\" container=\"false\" />\n"
			+ "</item>\n"
			+ "</item>"};

	// test cases which should provide null
	public static final String[] badGames = {
			EMPTY_STRING, 
			"<game state=\"0\" level=\"0\" >\n</gae>", 
			"<game sate=\"0\" level=\"0\" >\n</game>",
			"<game state=\"0\" >\n</game>"
			};
    public static final String[] badLevels = {
    		EMPTY_STRING, 
    		"<level sze=\"1\" >\n\t"
    		+ "<location type=\"grass\" >\n"
    		+ "\t</location>\n</level>", 
    		"<leel size=\"1\" >\n\t<location type=\"grass\" >\n\t</location>\n</level>",
    		};
	public static final String[] badLocations = {EMPTY_STRING};
    public static final String[] badPlayers = {EMPTY_STRING};
	public static final String[] badMovingCharacters = {EMPTY_STRING};
	public static final String[] badItems = {
			EMPTY_STRING, 
			"<item id=\"0\" type=\"ky\" container=\"false\" />", 
			"<item id=\"0\" type=\"chest\" container=\"false\" >\n"
			+ "\t<item id=\"0\" type=\"key\" container=\"false\" />\n"
			+ "</item>",
			"< item id=\"0\" type=\"chest\" container=\"true\" >\n"
			+ "\t<item id=\"0\" type=\"chest\" container=\"true\" >\n"
			+ "\t\t<item id=\"0\" type=\"key\" container=\"false\" />\n"
			+ "</item>\n"
			+ "</item>"
			};
	
	/** 
	 * Tests if an input string is a valid/invalid constructor of a game
	 */
	@Test
	public void testGame() {
		for (String input : goodGames) {
			Game game = Parser.parseGame(new Scanner(input));
			assert game != null;
		}
		for (String input : badGames) {
			Game game = Parser.parseGame(new Scanner(input));
			assert game == null;
		}
	}
	
	/**
	 * Tests if an input string is a valid/invalid constructor of a level
	 */
	@Test
	public void testLevel() {
		for (String input : goodLevels) {
			Level level = Parser.parseLevel(new Scanner(input), null);
			assert level != null;
		}
		for (String input : badLevels) {
			Level level = Parser.parseLevel(new Scanner(input), null);
			assert level == null;
		}
	}
	
	/**
	 * Tests if an input string is a valid/invalid constructor of a location
	 */
	@Test
	public void testLocation() {
		for (String input : goodLocations) {
			Location location = Parser.parseLocation(new Scanner(input), null);
			assert location != null;
		}
		for (String input : badLocations) {
			Location location = Parser.parseLocation(new Scanner(input), null);
			assert location == null;
		}
		assert false;
	}
	
	/**
	 * Tests if an input string is a valid/invalid constructor of a player
	 */
	@Test
	public void testPlayer() {
		for (String input : goodPlayers) {
			Player player = Parser.parsePlayer(new Scanner(input), null, null);
			assert player != null;
		}
		for (String input : badPlayers) {
			Player player = Parser.parsePlayer(new Scanner(input), null, null);
			assert player == null;
		}
	}
	
	/**
	 * Tests if an input string is a valid/invalid constructor of a moving character
	 */
	@Test
	public void testMovingCharacter() {
		for (String input : goodMovingCharacters) {
			MovingCharacter moving_char = Parser.parseCharacter(new Scanner(input));
			assert moving_char != null;
		}
		for (String input : badMovingCharacters) {
			MovingCharacter moving_char = Parser.parseCharacter(new Scanner(input));
			assert moving_char == null;
		}
	}
	
	/**
	 * Tests if an input string is a valid/invalid constructor of an Item 
	 */
	@Test
	public void testItem() {
		for (String input : goodItems) {
			Item item = Parser.parseItem(new Scanner(input), null);
			assert item != null;
		}
		for (String input : badItems) {
			Item item = Parser.parseItem(new Scanner(input), null);
			assert item == null;
		}
	}
	
	
}
