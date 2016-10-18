package view.renderTesting;

import java.util.Scanner;
import java.util.Set;

import model.character.Alligator;
import model.character.Bat;
import model.character.Bee;
import model.character.FloatStrategy;
import model.character.FlyStrategy;
import model.character.Log;
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
import model.items.Key;
import model.items.Maps;
import model.items.Treasure;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class to load the game world from disk
 * @author forancono
 *
 * Grammar for game file
 * GAME ::= '<game' 'state="'NUM'"' 'level="'NUM'"''>' [LEVEL]+ [PLAYER]* '</game>'
 * PLAYER ::= '<player' 'id="'NUM'"' 'name="'USERNAME'"' 'level="'NUM'"' 'lives="'NUM'"' 'x="'NUM'"' 'y="'NUM'"''>' [ITEM]* '</player>'
 * LEVEL ::= '<level' 'size="'NUM'">' [LOCATION]+ [CHARACTER]* '</level>'
 * CHARACTER ::= '<character' 'id="'NUM'"' 'type="'CHAR_TYPE'"' 'strategyVariable="'NUM'"' 'x="'NUM'"' 'y="'NUM'"/>'
 * LOCATION ::= '<location' 'type="'LOC_TYPE'"' '>' [ITEM] '</location>'
 * ITEM ::= '<item' 'id="'NUM'"' 'type="'ITEM_TYPE'"' 'container="'BOOL'"' '>' [ITEM]* '</item>'
 * CHAR_TYPE ::= 'bee' | 'bat' | 'log' | 'alligator'
 * LOC_TYPE ::= 'grass' | 'tree' | 'water' | 'bridge' | 'stone' | 'path'
 * ITEM_TYPE ::= 'fruit' | 'key' | 'map' | 'treasure' | 'chest'
 * USERNAME ::= [A-Z|a-z]+
 * BOOL ::= 'true' | 'false'
 * NUM ::= [0-9]+
 *
 */
public class ParserX {

	// xml element names
    public static final String GAME = "game";
    public static final String PLAYER = "player";
    public static final String LEVEL = "level";
    public static final String CHARACTER = "character";
    public static final String LOCATION = "location";
    public static final String ITEM = "item";

    // character type literals
    public static final String BEE = "bee";
    public static final String BAT = "bat";
    public static final String LOG = "log";
    public static final String ALLIGATOR = "croc";
    public static final String HIPPO = "hippo";

    // location type literals
    public static final String GR = "grass";
    public static final String TR = "tree";
    public static final String WA = "water";
    public static final String BR = "bridge";
    public static final String ST = "stone";
//    public static final String PA = "step";
    public static final String MO = "mountain";
	public static final String RO = "road";
	public static final String RS = "road_stripe";
	public static final String SE = "step";

    // item type literals
    public static final String FRUIT = "fruit";
    public static final String KEY = "key";
    public static final String MAP = "map";
    public static final String TREASURE = "treasure";
    public static final String CHEST = "chest";

    // boolean literals
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    // useful chars as strings to allow concatenation with ints
    public static final String LT = "<";
    public static final String GT = ">";
    public static final String SL = "/";
    public static final String NEWLINE = "\n";
    public static final String TAB = "\t";
    public static final String EQ = "=";
    public static final String QU = "\"";
    public static final String SP = " ";
    public static final String COMMENT_START = "<!--";
    public static final String COMMENT_END = "-->";


    // common error messages
    public static final String BAD_START = "Bad start tag on ";
    public static final String BAD_END = "Bad end tag on ";
	public static final String BAD_ATTRIBUTE = "Bad attribute in ";
	public static final String BAD_VALUE = "Bad value";
	public static final String BAD_TYPE = "Bad type";
	public static final String EXPECTING ="Expecting ";

	// attribute names
	public static final String TYPE = "type";
	public static final String ID = "id";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String SIZE = "size";
	public static final String STATE = "state";
	public static final String USERNAME = "username";
	public static final String LIVES = "lives";
	public static final String STRATEGY_VARIABLE = "strategyVariable";
	private static final int FRUIT_SIZE = 5;
	private static final int TREASURE_SIZE = 4;


    /*
     *  Loads the game
     */
    public static GameTest load(File f) throws FileNotFoundException{
        try {
            Scanner s = new Scanner(f);
            return parseGame(s);
        } catch (Exception e) {
        	System.err.println(e.getMessage());
        	throw new FileNotFoundException();
        }
    }

    /*
     *  GAME ::= '<game' 'state="'NUM'"' 'levels="'NUM'"''>' [LEVEL]+ [PLAYER]* '</game>'
     */
    public static GameTest parseGame(Scanner s) {
    	try {
    	gobbleComment(s);
    	if (!openBlock(GAME, s)) {
            System.err.println(BAD_START + GAME);
            return null;
        }
        int state = parseAttributeNum("state", s);
        if (state == -1) {
        	System.err.println(BAD_ATTRIBUTE + GAME);
        	return null;
        }
        GameStatus status;
        switch (state) {
        case 0:
        	status = GameStatus.RUNNING;
        	break;
        case 1:
        	status = GameStatus.GAMEWON;
        	break;
        case 2:
        	status = GameStatus.ENDS;
        	break;
        	default:
        		status = null;
        }
        int numLevels = parseAttributeNum(LEVEL, s);
        if (numLevels == -1) {
        	System.err.println(BAD_ATTRIBUTE + GAME);
        	return null;
        }
        parseString(GT, s);

        Set<Integer> ids = new HashSet<>();
        List<Level> levels = new LinkedList<>();
        for (int a = 0; a < numLevels; ++ a) {
            levels.add(parseLevel(s, ids));
        }

        Map<String, Player> players = new HashMap<>();
        while (s.hasNext(LT + PLAYER)) {
            Player player = parsePlayer(s, ids, levels);
        	players.put(player.getPlayerName(), player);
        }
        if (!closeBlock(GAME, s)) {
            System.err.println(BAD_END + GAME);

            return null;
        } else return new GameTest(status, levels, players, ids);
    	} catch (NoSuchElementException nsee) {
			System.err.println(EXPECTING + GAME);
			return null;
		}
    }

    /*
     * LEVEL ::= '<level' 'size="'NUM'"' '>' [LOCATION]+ [CHARACTER]* '</level>'
     */
    public static Level parseLevel(Scanner s, Set<Integer> ids) {
    	try {
    		gobbleComment(s);
    		// parse start tag
    		if (!openBlock(LEVEL, s)) {
    			System.err.println(BAD_START + LEVEL);
    			return null;
    		}
    		int size = parseAttributeNum(SIZE, s);
    		if (size < 0) {
    			System.err.println(BAD_ATTRIBUTE + LEVEL);
    			return null;
    		}
    		parseString(GT, s);
    		// parse body
    		Location[][] grid = new Location[size][size];
    		if (!s.hasNext(LT + LOCATION)) {
    			System.err.println("Level must have locations");
    			return null;
    		}
    		int y = -1;
    		for (int i = 0; i < size * size; i ++) {
    			if (!s.hasNext(LT + LOCATION)) {
    				System.err.println(EXPECTING + LOCATION);
    				return null;
    			}
    			int x = i % size;
    			if (x == 0) y ++;
    			grid[x][y] = parseLocation(s, ids);
    		}
    		Map<Integer, MovingCharacter> characters = new HashMap<>();
    		while (s.hasNext(LT + CHARACTER)) {
    			MovingCharacter character = parseCharacter(s);
    			if (character != null) characters.put(character.getID(), character);
    		}
    		// parse end tag
    		if (!closeBlock(LEVEL, s)) {
    			System.err.println(BAD_END + LEVEL);
    			return null;
    		} else return new Level(grid, characters);
    	} catch (NoSuchElementException nsee) {
    		System.err.println(EXPECTING + LEVEL);
    		return null;
    	}
    }


    /*
	 * PLAYER ::= '<player' 'id="'NUM'"' 'name="'USERNAME'"' 'level="'NUM'"' 'lives="'NUM'"' 'x="'NUM'"' 'y="'NUM'"''>' [ITEM]* '</player>'
	 */
	public static Player parsePlayer(Scanner s, Set<Integer> ids, List<Level> levels) {
	    gobbleComment(s);
		// parse start tag
		if (!openBlock(PLAYER, s)) {
	        System.err.println(BAD_START + PLAYER);
	        return null;
	    }
	    int id = parseAttributeNum(ID, s);
	    if (ids.contains(id)) id = getRandomId(ids);
	    else ids.add(id);
	    String name = parseAttributeString(USERNAME, s);
	    int level = parseAttributeNum(LEVEL, s);
	    int lives = parseAttributeNum(LIVES, s);
	    int x = parseAttributeNum(X, s);
	    int y = parseAttributeNum(Y, s);
	    parseString(GT, s);
	    // parse body
	    List<Item> inventory = new LinkedList<>();
	    while (s.hasNext(LT + ITEM)) {
	        inventory.add(parseItem(s, ids));
	    }
	    // parse end tag
	    if (!closeBlock(PLAYER, s)) {
	        System.err.println(BAD_END + PLAYER);
	        return null;
	    } else {
	    	Player p = new Player(id, lives, name);
	    	p.setLevel(levels.get(level));
	    	p.setPoint(new Point(x, y));
	    	return p;
	    }
	}


	private static int getRandomId(Set<Integer> ids) {
		int rand = (int)(Math.random() * Integer.MAX_VALUE);
		while (ids.contains(rand)) {
			rand = (int)(Math.random() * Integer.MAX_VALUE);
		}
		ids.add(rand);
		return rand;
	}

	/*
     * LOCATION ::= '<location' 'type="'LOC_TYPE'"' '>' [ITEM] '</location>'
     */
    public static Location parseLocation(Scanner s, Set<Integer> ids) {
    	gobbleComment(s);
    	// parse start tag
    	if (!openBlock(LOCATION, s)) {
    	    System.err.println(BAD_START + LOCATION);
    	    return null;
    	}
		String type = parseAttributeString(TYPE, s);
		parseString(GT, s);
		// parse body
		Item item = null;
		if (s.hasNext(LT + ITEM)) item = parseItem(s, ids);
		// parse end tag
		if (!closeBlock(LOCATION, s)) {
		    System.err.println(BAD_END + LOCATION);
		    return null;
		} else {
		    Type locType;
			switch (type) {
                case GR :
                    locType = Type.GRASS;
                    break;
                case TR :
                    locType = Type.TREE;
                    break;
                case WA :
                    locType = Type.WATER;
                    break;
                case BR :
                    locType = Type.BRIDGE;
                    break;
                case ST :
                    locType = Type.STONE;
                    break;
//                case PA : // from path
//                    locType = Type.STEP;
//                    break;
                case MO :
                	locType = Type.MOUNTAIN;
                	break;
                case RO :
                	locType = Type.ROAD;
                	break;
                case RS :
                	locType = Type.ROADSTRIPE;
                	break;
                case SE :
                	locType = Type.STEP;
                    default :
                    	locType = null;
			}
			if (locType == null) {
				System.err.println(BAD_TYPE + LOCATION);
				return null;
			} else return new Location(locType, item);
        }
	}

    /*
     * ITEM ::= '<item' 'id="'NUM'"' 'type="'ITEM_TYPE'"' 'container="'BOOL'"' '/>'
     * always empty
     */
    public static Item parseItem(Scanner s, Set<Integer> ids) {
        try {
    	// parse start tag
    	if (!openBlock(ITEM, s)) {
            System.err.println(BAD_START + ITEM);
            return null;
        }
        int id = parseAttributeNum(ID, s);
        String type = parseAttributeString(TYPE, s);
        boolean container;
        if (parseAttributeString("container", s).equals(TRUE)) container = true;
        else container = false;

        // parse body
        Item holding = null;
        // handle container case container & ">"
        if (container) {
        	if (!parseString(GT, s)) {
        		System.err.println(BAD_START + ITEM);
                return null;
        	}
        	holding = parseItem(s, ids);
        	if (!parseString(LT + SL + ITEM + GT, s)) {
        		System.err.println(BAD_END + ITEM);
            	return null;
        	}
        } else { // empty element "/>" no close tag
        	if (!parseString(SL + GT, s)) {
        		System.err.println(BAD_END + ITEM);
        		return null;
        	}
        }


        // create object
        switch (type) {
        	case FRUIT :
        		int rand = (int) Math.random() * FRUIT_SIZE;
        		switch (rand) {
        		case 0 :
        			return new Fruit(id, "apple", null);
        		case 1 :
        			return new Fruit(id, "banana", null);
        		case 2 :
        			return new Fruit(id, "tomato", null);
        		case 3 :
        			return new Fruit(id, "strawberry", null);
        		case 4 :
        			return new Fruit(id, "orange", null);

        		}
        	case KEY :
        		return new Key(id, KEY, null);
        	case MAP :
        		return new Maps(id, MAP, null);
        	case TREASURE :
        		int rando = (int) Math.random() * TREASURE_SIZE;
        		switch (rando) {
        		case 0 :
        			return new Fruit(id, "pearl", null);
        		case 1 :
        			return new Fruit(id, "jewel_blue", null);
        		case 2 :
        			return new Fruit(id, "jewel_red", null);
        		case 3 :
        			return new Fruit(id, "jewel_white", null);

        		}
        	case CHEST :
        		return new Chest(id, CHEST, null, holding);
        		default :
        			return null;
        }
        } catch (NoSuchElementException nsee) {
        	System.err.println(EXPECTING + ITEM);
			return null;
		}
    }

    /*
     * CHARACTER ::= '<character' 'id="'NUM'"' 'type="'CHAR_TYPE'"' 'strategyVariable="'NUM'"' 'x="'NUM'"' 'y="'NUM'"/>'
     * always empty
     */
    public static MovingCharacter parseCharacter(Scanner s) {
        if (!openBlock(CHARACTER, s)) {
            System.err.println(BAD_START + CHARACTER);
            return null;
        }
        int id = parseAttributeNum(ID, s);
        String type = parseAttributeString(TYPE, s);
        int strategy_variable = parseAttributeNum(STRATEGY_VARIABLE, s);
        int x = parseAttributeNum(X, s);
        int y = parseAttributeNum(Y, s);
        if (!parseString(SL + GT, s)) {
            System.err.println(BAD_START + CHARACTER);
            return null;
        } else {
        	Point p = new Point(x, y);
            switch (type) {
                case BEE :
                    Bee be = new Bee(new FlyStrategy(strategy_variable), null);
                    be.setPoint(p);
                    be.setID(id);
                    return be;
                case BAT :
                    Bat ba = new Bat(new FlyStrategy(strategy_variable), null);
                    ba.setPoint(p);
                    ba.setID(id);
                    return ba;
                case LOG :
                    Log lo = new Log(new FloatStrategy(strategy_variable), null);
                    lo.setPoint(p);
                    lo.setID(id);
                    return lo;
                case ALLIGATOR :
                	Alligator al = new Alligator(new FloatStrategy(strategy_variable), null);
                	al.setPoint(p);
                	al.setID(id);
                	return al;
//                case HIPPO :
//                    return new Hippo(id, x, y);
//                    break;
                default :
                    System.err.println("Not a character");
                    return null;
            }
        }
    }

    /*
     * All numbers are natural numbers in this context
     */
    public static int parseNum(Scanner s) {
        if (s.hasNextInt()) return s.nextInt();
        else {
            System.err.println("Expecting a number");
            return -1;
        }
    }

    /*
     * Returns the value of a numerical attribute
     */
    public static int parseAttributeNum(String lit, Scanner s) {
    	String at = parseAttribute(lit, s);

    	int output;
        try {
        	output = Integer.parseInt(at);
        } catch (NumberFormatException nfe) {
        	System.err.println("Attribute value is not an integer " + at);
        	output = -1;
        }
    	return output;
    }

    /*
     * Returns the value of a textual attribute
     */
    public static String parseAttributeString(String lit, Scanner s) {
    	return parseAttribute(lit, s);
    }

    /*
     * Gobbles the fluff surrounding an attribute
     */
    public static String parseAttribute(String lit, Scanner s) {
    	String[] tokens = s.next().split(EQ);
    	if (tokens[0].equals(lit) && tokens.length == 2) {
    		String out = tokens[1].substring(1, tokens[1].length() - 1);
    		//System.out.println(out);
    		return out;
    	} else {
    		//System.out.println(tokens[0] + tokens[1]);
    		System.err.println("Expecting " + lit);
			return null;
    	}
    }

    /*
     * Gobbles a string - true if it was the expected string
     */
    public static boolean parseString(String st, Scanner sc) {
    	if (sc.hasNext(st)) {
    		sc.next(st);
    		return true;
    	} else {
    		System.err.println("Expecting " + st);
    		return false;
    	}
    }

    /*
     * Gobbles the opening tag of a block
     */
    public static boolean openBlock(String p, Scanner s) {
        String tag = s.next();
        if (tag.equals(LT + p)) return true;
        else {
            System.err.println(tag);
            return false;
        }
    }

    /*
     * Gobbles the closing tag of a block
     */
    public static boolean closeBlock(String p, Scanner s) {
        String tag = s.next();
        if (tag.equals(LT + SL + p + GT)) return true;
        else {
            System.err.println(tag);
            return false;
        }
    }

    /*
     * Gobbles a xml comment
     */
    public static void gobbleComment(Scanner s) {
    	if (s.hasNext(COMMENT_START)) {
    		while (!s.hasNext(COMMENT_END)) {
    			s.next();
    		}
    		s.next(COMMENT_END);
    	}
    }

    public static void main(String args[]) {
    	try {
			load(new File("./src/util/simpleGame.xml"));
			System.out.println("Loaded");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
