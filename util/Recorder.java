package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import model.character.Alligator;
import model.character.Bat;
import model.character.Bee;
import model.character.Log;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Game;
import model.game.Level;
import model.game.Location;
import model.game.Point;
import model.items.Chest;
import model.items.Fruit;
import model.items.Item;
import model.items.Key;
import model.items.Maps;
import model.items.Treasure;
/**
 * Class to save the game world to disk
 * @author forancono
 *
 * Grammar for game file
 * GAME ::= '<game' 'state="'NUM'"' 'level="'NUM'"''>' [LEVEL]+ [PLAYER]* '</game>'
 * PLAYER ::= '<player' 'id="'NUM'"' 'name="'USERNAME'"' 'level="'NUM'"' 'lives="'NUM'"' 'x="'NUM'"' 'y="'NUM'"''>' [ITEM]* '</player>'
 * LEVEL ::= '<level' 'size="'NUM'">' [LOCATION]+ [CHARACTER]* '</level>'
 * CHARACTER ::= '<character' 'id="'NUM'"' 'type="'CHAR_TYPE'"' 'strategyVariable="'NUM'"' 'x="'NUM'"' 'y="'NUM'"/>'
 * LOCATION ::= '<location' 'type="'LOC_TYPE'"' '>' [ITEM] '</location>'
 * ITEM ::= '<item' 'id="'NUM'"' 'type="'ITEM_TYPE'"' '>' [ITEM]* '</item>'
 * CHAR_TYPE ::= 'bee' | 'bat' | 'log' | 'hippo' | 'croc'
 * LOC_TYPE ::= 'grass' | 'tree' | 'water' | 'bridge' | 'stone' | 'path'
 * ITEM_TYPE ::= 'fruit' | 'key' | 'map' | 'treasure' | 'chest'
 * USERNAME ::= [A-Z|a-z]+
 * BOOL ::= 'true' | 'false'
 * NUM ::= [0-9]+
 *
 */
public class Recorder {
	 /*
     * Saves the game
     */
    public static void save(File f, Game g) {
    	Writer w = null;
    	try {
    		w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
    		w.write(gameString(g));
    	} catch (IOException e) {
			System.err.println("Failed to save the game");
			System.exit(1);
		} finally {
			try {
				w.close();
			} catch (Exception e) {

			}
		}
    }

    /*
     * GAME ::= '<game' 'state="'NUM'"' 'level="'NUM'"''>' [LEVEL]+ [PLAYER]* '</game>'
     */
    public static String gameString(Game game) {
        String output = makeStartTag(Parser.GAME);
        int state;
        switch (game.getState()) {
        case RUNNING :
        	state = 0;
        	break;
        case GAMEWON :
        	state = 1;
        	break;
        case ENDS :
        	state = 2;
        	break;
        	default :
        		state = -1; // an error
        }
        output += makeAttributeString(Parser.STATE, Integer.toString(state));
        output += makeAttributeString(Parser.LEVEL, Integer.toString(game.getLevels().size()));
        output += Parser.GT + Parser.NEWLINE;
        for (Level l : game.getLevels()) {
        	output += levelString(l, 1);
        }
        for (Player p : game.getPlayers()) {
        	output += playerString(p, game.getLevels(), 1);
        }
        output += makeEndTag(Parser.GAME);
        return output;
    }

   /*
    * LEVEL ::= '<level' 'size="'NUM'">' [LOCATION]+ [CHARACTER]* '</level>'
    */
    public static String levelString(Level level, int depth) {
        String output = tabToDepth(depth);
        output += makeStartTag(Parser.LEVEL);
        int size = level.getLocations().length;
        output += makeAttributeString(Parser.SIZE, Integer.toString(size));
        output += Parser.GT + Parser.NEWLINE;
        for (Location[] la : level.getLocations()) {
        	for (Location lo : la) {
        		output += locationString(lo, depth + 1);
        	}
        }
        if (level.getCharacters() != null) {
        	for (MovingCharacter mc : level.getCharacters()) {
        		output += characterString(mc, depth + 1);
        	}
        }
        output += tabToDepth(depth);
        output += makeEndTag(Parser.LEVEL);
        return output;
    }

    /*
     * PLAYER ::= '<player' 'id="'NUM'"' 'name="'USERNAME'"' 'level="'NUM'"' 'lives="'NUM'"' 'x="'NUM'"' 'y="'NUM'"''>' [ITEM]* '</player>'
     */
	public static String playerString(Player player, List<Level> levels, int depth) {
        String output = tabToDepth(depth);
        output += makeStartTag(Parser.PLAYER);
        output += makeAttributeString(Parser.ID, Integer.toString(player.getID()));
        output += makeAttributeString(Parser.USERNAME, player.getPlayerName());
        int level = levels.size() - 1;
        for (; level > 0; -- level) {
        	if (player.getCurrentLevel().equals(levels.get(level))) break;
        }
        output += makeAttributeString(Parser.LEVEL, Integer.toString(level));
        output += makeAttributeString(Parser.LIVES, Integer.toString(player.getLives()));
        Point p = player.getPoint();
        output += makeAttributeString(Parser.X, Integer.toString(p.x));
        output += makeAttributeString(Parser.Y, Integer.toString(p.y));
        output += Parser.GT + Parser.NEWLINE;
        for (Item it : player.getInventory()) {
        	output += itemString(it, depth + 1);
        }
        output += tabToDepth(depth);
        output += makeEndTag(Parser.PLAYER);
        return output;
    }

	/*
	 * CHARACTER ::= '<character' 'id="'NUM'"' 'type="'CHAR_TYPE'"' 'strategyVariable="'NUM'"' 'x="'NUM'"' 'y="'NUM'"/>'
	 */
    public static String characterString(MovingCharacter character, int depth) {
    	String output = tabToDepth(depth);
    	output += makeStartTag(Parser.CHARACTER);
    	output += makeAttributeString(Parser.ID, Integer.toString(character.getID()));
    	String type = "";
    	if (character instanceof Bee) {
    		type = Parser.BEE;
    	} else if (character instanceof Bat) {
    		type = Parser.BAT;
    	} else if (character instanceof Log) {
    		type = Parser.LOG;
    	} else if (character instanceof Alligator) {
    		type = Parser.ALLIGATOR;
    	}
    	output += makeAttributeString(Parser.TYPE, type);
    	int value = character.getStrategy().getValue();
    	output += makeAttributeString(Parser.STRATEGY_VARIABLE, Integer.toString(value));
    	Point p = character.getPoint();
    	output += makeAttributeString(Parser.X, Integer.toString(p.x));
    	output += makeAttributeString(Parser.Y, Integer.toString(p.y));
    	output += Parser.SL + Parser.GT + Parser.NEWLINE;
    	return output;
    }

    /*
     * LOCATION ::= '<location' 'type="'LOC_TYPE'"' '>' [ITEM] '</location>'
     */
    public static String locationString(Location location, int depth) {
    	String output = tabToDepth(depth);
    	output += makeStartTag(Parser.LOCATION);
    	String type = "";
    	switch (location.getType()) {
    	case GRASS :
    		type = Parser.GR;
    		break;
    	case TREE :
    		type = Parser.TR;
    		break;
    	case WATER :
    		type = Parser.WA;
    		break;
    	case BRIDGE :
    		type = Parser.BR;
    		break;
    	case STONE :
    		type = Parser.ST;
    		break;
//    	case PATH :
//    		type = Parser.PA;
//    		break;
    	case MOUNTAIN :
    		type = Parser.MO;
    		break;
		case ROAD:
			type = Parser.RO;
			break;
		case ROADSTRIPE:
			type = Parser.RS;
			break;
		case STEP:
			type = Parser.SE;
			break;
		default:
			break;
    	}
    	output += makeAttributeString(Parser.TYPE, type);
    	
    	if (location.getItem() != null) {
    		output += Parser.GT + Parser.NEWLINE;
    		output += itemString(location.getItem(), depth + 1);
    		output += tabToDepth(depth);
        	output += makeEndTag(Parser.LOCATION);
    	} else {
    		output += Parser.SL + Parser.GT + Parser.NEWLINE;
    	}
    	return output;
    }

    /*
     * ITEM ::= '<item' 'id="'NUM'"' 'type="'ITEM_TYPE'"' '/>'
     */
    public static String itemString(Item item, int depth) {
    	String output = tabToDepth(depth);
    	output += makeStartTag(Parser.ITEM);
    	output += makeAttributeString(Parser.ID, Integer.toString(item.getID()));
    	String type = "";
    	String container = Parser.FALSE;
    	if (item instanceof Fruit) {
    		type = Parser.FRUIT;
    	} else if (item instanceof Key) {
    		type = Parser.KEY;
    	} else if (item instanceof Maps) {
    		type = Parser.MAP;
    	} else if (item instanceof Treasure) {
    		type = Parser.TREASURE;
    	} else if (item instanceof Chest) {
    		type = Parser.CHEST;
    		container = Parser.TRUE;
    	}
    	output += makeAttributeString(Parser.TYPE, type);
    	output += makeAttributeString(Parser.CONTAINER, container);
    	if (item instanceof Chest) {
    		Chest chest = (Chest)item;
    		output += Parser.GT + Parser.NEWLINE;
    		output += itemString(chest.getContainedItem(), depth + 1);
    		output += makeEndTag(Parser.ITEM);
    	} else {
    		output += Parser.SL + Parser.GT + Parser.NEWLINE;
    	}
    	return output;
    }

    public static String makeStartTag(String lit) {
    	return Parser.LT + lit + Parser.SP;
    }

    public static String makeEndTag(String lit) {
    	return Parser.LT + Parser.SL + lit + Parser.GT + Parser.NEWLINE;
    }

    public static String makeAttributeString(String att_name, String value) {
    	return att_name + Parser.EQ + Parser.QU + value + Parser.QU + Parser.SP;
    }

    private static String tabToDepth(int depth) {
		String output = "";
    	for (int i = 0; i < depth; ++ i) {
			output += Parser.TAB;
		}
    	return output;
	}
}
