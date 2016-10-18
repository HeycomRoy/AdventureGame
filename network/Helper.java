package network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import model.character.Alligator;
import model.character.Bat;
import model.character.Bee;
import model.character.FloatStrategy;
import model.character.FlyStrategy;
import model.character.Log;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Level;
import model.game.Location;
import model.game.Point;
import model.game.Location.Type;
import model.items.Chest;
import model.items.Fruit;
import model.items.Item;
import model.items.Key;
import model.items.Maps;
import model.items.Treasure;
import util.Parser;


/**
 * Class with static methods to encode/decode game elements over the network
 * 
 * @author forancono
 */
public class Helper {

	private static final int CHAR_SIZE = 3;

	/**
	 * Location to network - contains item
	 * |item|has item|type| 
	 */
	public static byte[] locationToNetwork(Location location){ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			switch (location.getType()) {
			case GRASS :
				dos.writeInt(0);
				break;
			case TREE :
				dos.writeInt(1);
				break;
			case WATER :
				dos.writeInt(2);
				break;
			case BRIDGE :
				dos.writeInt(3);
				break;
			case STONE :
				dos.writeInt(4);
				break;
			//case PATH :
			//	dos.writeInt(5);
			//	break;
			case MOUNTAIN :
				dos.writeInt(6);
				break;
			case ROAD:
				dos.writeInt(7);
				break;
			case ROADSTRIPE:
				dos.writeInt(8);
				break;
			case STEP:
				dos.writeInt(9);
				break;
				default :
					dos.writeInt(0);
			}
			if (location.getItem() == null) dos.writeInt(0);
			else {
				dos.writeInt(1);
				Item item = location.getItem();
				dos.write(Helper.itemToNetwork(item));
			}
			
		} catch (Exception e) {
			System.err.println("Failed making location byte array");
		}
		return baos.toByteArray();
	}
	
	/**
	 * Return a new location from the network
	 */
	public static Location locationFromNetwork(DataInputStream dis) {
		Location location = null;
		try {
			int type = dis.readInt();
			switch (type) {
			case 0 :
				location = new Location(Type.GRASS, null);
				break;
			case 1 :
				location = new Location(Type.TREE, null);
				break;
			case 2 :
				location = new Location(Type.WATER, null);
				break;
			case 3 :
				location = new Location(Type.BRIDGE, null);
				break;
			case 4 :
				location = new Location(Type.STONE, null);
				break;
			case 6 :
				location = new Location(Type.MOUNTAIN, null);
				break;
			case 7 :
				location = new Location(Type.ROAD, null);
				break;
			case 8 :
				location = new Location(Type.ROADSTRIPE, null);
				break;
			case 9 :
				location = new Location(Type.STEP, null);
				break;
			}
			assert location != null;
			switch (dis.readInt()) {
			case 0:
				// location done
				return location;
			case 1:
				// read item
				Item item = itemFromNetwork(dis);
				location.setItem(item);
				return location;	
			}
		} catch (IOException ioe) {
			System.err.println("Failed to read location from server");
		}
		return null; 
	}
	
	/**
	 * Method to turn a new player into byte array
	 * ..|item|inventory size|name|lives|char data|
	 */
	public static byte[] playerToNetwork(Player player) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(movingCharToNetwork(player));
			dos.writeInt(player.getLives());
			dos.writeUTF(player.getPlayerName());
			dos.writeInt(player.getInventory().size());
			for (Item item : player.getInventory()) {
				dos.write(itemToNetwork(item));
			}
		} catch (Exception e) {
			System.err.println("Failed making player byte array");
		}
		return baos.toByteArray();
	}
	
	/**
	 * Method get a player from a byte array
	 */
	public static Player playerFromNetwork(DataInputStream dis) {
		try {
			int[] charData = movingCharFromNetworkDynamic(dis);
			//System.out.println(charData[0]);
			int lives = dis.readInt();
			String userName = dis.readUTF();
			Player player = new Player(charData[0], lives, userName);
			player.setPoint(new Point(charData[1], charData[2]));
			
			int invSize = dis.readInt();
			List<Item> inv = player.getInventory();
			for (int i = 0; i < invSize; ++ i) {
				inv.add(itemFromNetwork(dis));
			}
			return player;
		} catch (Exception e) {
			System.err.println("Failed to build new player");
		}
		return null;
	}

	/**
	 * Make a player byte array
	 * ..|item|inv size|lives|username|charData|
	 */
	public static byte[] playerToNetworkDynamic(Player player) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(movingCharToNetwork(player));
			dos.writeUTF(player.getPlayerName());
			dos.writeInt(player.getLives());
			dos.writeInt(player.getInventory().size());
			List<Item> inv = player.getInventory();
			for (Item item : inv) {
				dos.write(itemToNetwork(item));
			}
		} catch (IOException ioe) {
			System.err.println("Failed to make player byte array");
		}
		return baos.toByteArray();
	}

	public static String getUserName(DataInputStream dis) {
		try {
			return dis.readUTF();
		} catch (Exception e) {
			System.err.println("Failed to read username");
		}
		return null;
	}
	
	
	public static void playerFromNetworkDynamic(DataInputStream dis, Player player, Point point) {
		try {
			List<Item> inv = player.getInventory();
			player.setLives(dis.readInt());
			player.setPoint(point);
			int invSize = dis.readInt();			
			for (int i = 0; i < invSize; ++i) {
				inv.add(itemFromNetwork(dis));
			}
		} catch (Exception e) {
			System.err.println("Failed to update player");
		}
	}

	/**
	 * Method to turn a moving character into a byte array
	 * |y|x|id|
	 */
	public static byte[] movingCharToNetwork(MovingCharacter mc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			Point p = mc.getPoint();
			dos.writeInt(mc.getID());
			dos.writeInt(p.x);
			dos.writeInt(p.y);
		} catch (IOException e) {
			System.err.println("Failed making moving character byte array");
		}
		return baos.toByteArray();
	}
	
	/** 
	 * Add a number representing the type of char and it's strategy variable
	 * |strategy variable|type|char data|
	 */
	public static byte[] movingCharToNetworkInit(MovingCharacter mc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(movingCharToNetwork(mc));
			if (mc instanceof Bee) {
				dos.writeInt(0);
			} else if (mc instanceof Bat) {
				dos.writeInt(1);
			} else if (mc instanceof Log) {
				dos.writeInt(2);
			} else if (mc instanceof Alligator) {
				dos.writeInt(3);
			}
			dos.writeInt(mc.getStrategy().getValue());
		} catch (IOException ioe) {
			
		}
		return baos.toByteArray();
	} 
	
	/**
	 * Get a new moving char from the network
	 */
	public static MovingCharacter movingCharFromNetwork(DataInputStream dis) {
		MovingCharacter mc = null;
		try {
			int[] charData = movingCharFromNetworkDynamic(dis);
			Point p = new Point(charData[1], charData[2]);
			int type = dis.readInt();
			int strategy_variable = dis.readInt();
			switch (type) {
			case 0:
				mc = new Bee(new FlyStrategy(strategy_variable), null);
				
				break;
			case 1:
				mc = new Bat(new FlyStrategy(strategy_variable), null);
				break;
			case 2:
				mc = new Log(new FloatStrategy(strategy_variable), null);
				break;
			case 3:
				mc = new Alligator(new FloatStrategy(strategy_variable), null);
				break;
			}
			if (mc != null) {
				mc.setPoint(p);
				mc.setID(charData[0]);
			}
		} catch (Exception e) {
			System.err.println("Failed to read moving character");
		}
		return mc;
	}

	/**
	 * Make an array of the values that represent a character id, x, y
	 */
	public static int[] movingCharFromNetworkDynamic(DataInputStream dis) {
		int[] data = new int[CHAR_SIZE];
		try {
			for (int i = 0; i < CHAR_SIZE; ++ i) {
				data[i] = dis.readInt();
			}
		} catch (Exception e) {
			System.err.println("Failed to read charData");
		}
		return data;
	}

	/**
	 * Method to turn an item into a byte array to be sent across the network
	 * |if chest -> item|type|id|
	 */
	public static byte[] itemToNetwork(Item item) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(item.getID());
		    int i;
			if (item instanceof Fruit) {
		    	i = 0;
		    } else if (item instanceof Key) {
		    	i = 1;
		    } else if (item instanceof Maps) {
		    	i = 2;
		    } else if (item instanceof Treasure) {
		    	i = 3;
		    } else if (item instanceof Chest) {
		    	i = 4;
		    } else {
		    	i = -1;
		    }
			dos.writeInt(i);
			if (item.isContainer()) {
				dos.write(itemToNetwork((((Chest) item).getContainedItem())));
			}
		} catch (IOException ex) {
			System.err.println("Failed making item byte array");
			
		}
		return baos.toByteArray();
	}
	
	/**
	 * Wrapper for item to add an xy to keep items on locations consistent
	 * |y|x|item|
	 */
	public static byte[] itemToNetworkDynamic(Item value, int x, int y) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(itemToNetwork(value));
			dos.writeInt(x);
			dos.writeInt(y);
		} catch (Exception e) {
			System.err.println("Failed making item byte array");
		}
		return baos.toByteArray();
	}

	/**
	 * Create a new item from a data input stream
	 */
	public static Item itemFromNetwork(DataInputStream dis) {
		int id = -2;
		try {
			id = dis.readInt();
			int type = dis.readInt();
			switch (type) {
			case 0 :
				return new Fruit(id, Parser.FRUIT, null);
			case 1 :
				return new Key(id, Parser.KEY, null);
			case 2 :
				return new Maps(id, Parser.MAP, null);
			case 3 :
				return new Treasure(id, Parser.TREASURE, null);
			case 4 :
				return new Chest(id, Parser.CHEST, null, itemFromNetwork(dis));
			}
		} catch (IOException e) {
			System.err.println("Failed to read an item");
			
		}
		return new Fruit(id, Parser.FRUIT, null);
	}

	/**
	 * put an Item in a location on a level
	 */
	public static void itemFromNetworkLevelDynamic(DataInputStream dis, Level level) {
		try {
			Item item = itemFromNetwork(dis);
			assert item != null;
			int x = dis.readInt();
			int y = dis.readInt();
			level.getLocations()[x][y].setItem(item);
		} catch (Exception e) {
			System.err.println("Failed to update an item");
		}
	}

}
