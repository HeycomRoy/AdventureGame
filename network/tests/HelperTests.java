package network.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import main.Main;
import model.character.Bee;
import model.character.FlyStrategy;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Location;
import model.game.Point;
import model.game.Location.Type;
import model.items.Chest;
import model.items.Fruit;
import model.items.Item;
import model.items.Treasure;
import network.Helper;

/**
 * Tests of the byte arrays delivered by the network and the
 * objects constructed/updated from them
 * 
 * @author forancono
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelperTests {

	/**
	 * Test creating a byte array of a location and reconstructing 
	 * the object from the byte array 	
	 */
	@Test 
	public void test0_locationTo() {
		Location location = new Location(Type.MOUNTAIN, null);
		byte[] output = Helper.locationToNetwork(location);
		byte[] expected = valuesToByteArray(new int[]{6, 0});
		checkByteArrays(output, expected);
		
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(output));
		Location out = Helper.locationFromNetwork(dis);
		assert out.getType() == location.getType();
		
		
	}
	
	@Test 
	public void test1_player() {
		int id = rrnn();
		int x = rrnn();
		int y = rrnn();
		Point p = new Point(x, y);
		// make two functionally equivalent players		
		Player serverPlayer = new Player(id, Player.MAX_LIVES, Main.DEFAULT_USER);
		Player clientPlayer = new Player(id, Player.MAX_LIVES, Main.DEFAULT_USER);
		serverPlayer.setPoint(p);
		clientPlayer.setPoint(p);
		Map<String, Player> serverPlayers = new HashMap<>();
		Map<String, Player> clientPlayers = new HashMap<>();
		serverPlayers.put(serverPlayer.getPlayerName(), serverPlayer);
		clientPlayers.put(clientPlayer.getPlayerName(), clientPlayer);
		
		// update server player
		serverPlayer.getPoint().x ++;
		serverPlayer.pickUpItem(new Fruit(5, null, null));
		serverPlayer.pickUpItem(new Treasure(7, null, null));
		
		// send across the 'network'
		byte[] output = Helper.playerToNetworkDynamic(serverPlayer);
		byte[] expected = concat(new byte[][]{
			valuesToByteArray(new int[]{serverPlayer.getID(), serverPlayer.getPoint().x, serverPlayer.getPoint().y}),
			stringToByteArray(serverPlayer.getPlayerName()),
			valuesToByteArray(new int[]{serverPlayer.getLives(), serverPlayer.getInventory().size(), 5, 0, 7, 3})
		});
		// check output
		checkByteArrays(output, expected);
		
		ByteArrayInputStream bias = new ByteArrayInputStream(output);
		DataInputStream dis = new DataInputStream(bias);
		
		// get initial data to id player
		int[] charData = Helper.movingCharFromNetworkDynamic(dis);
		Point point = new Point(charData[1], charData[2]);
		String username = Helper.getUserName(dis);
		assert clientPlayers.containsKey(username);
		
		// update client player from server output
		Helper.playerFromNetworkDynamic(dis, clientPlayers.get(username), point);
		
		// check position is consistent
		assert clientPlayer.getPoint().x == serverPlayer.getPoint().x && clientPlayer.getPoint().y == serverPlayer.getPoint().y;
		
		// step though the inventories checking for equal item ids
		assert clientPlayer.getInventory().size() == serverPlayer.getInventory().size();
		Iterator<Item> cItr = clientPlayer.getInventory().iterator();
		Iterator<Item> sItr = serverPlayer.getInventory().iterator();
		while (cItr.hasNext() && sItr.hasNext()) {
			assert cItr.next().getID() == sItr.next().getID();
		}
	}
		
	@Test 
	public void test2_newPlayer() {
		int id = 10;
		int x = 34;
		int y = 96; 
		Player player = new Player(id, Player.MAX_LIVES, Main.DEFAULT_USER);
		player.setPoint(new Point(x, y));
		byte[] output = Helper.playerToNetwork(player);
		byte[] expected = concat(new byte[][]{
			valuesToByteArray(new int[]{id, player.getPoint().x, player.getPoint().y, player.getLives()}),
			stringToByteArray(player.getPlayerName()),
			valuesToByteArray(new int[]{player.getInventory().size()})
		});
		checkByteArrays(output, expected);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(output);
		DataInputStream dis = new DataInputStream(bais);
		
		Player newPlayer = Helper.playerFromNetwork(dis);
		assert newPlayer != null;
		assert newPlayer.getPlayerName().equals(player.getPlayerName());
	}

	@Test 
	public void test3_char() {
		int id = 4;
		int x = 9;
		int y = 12;
		MovingCharacter mc = new MovingCharacter() {
			@Override
			public void move() {}
		};
		
		HashMap<Integer, MovingCharacter> mcs = new HashMap<>();
		mcs.put(id, mc);
		MovingCharacter ms = new MovingCharacter() {
			@Override
			public void move() {}
		};
		mc.setPoint(new Point(x, y));
		mc.setID(id);
		ms.setPoint(new Point(x, y));
		ms.setID(id);
		ms.getPoint().x ++;
		ms.getPoint().y ++;
		byte[] output = Helper.movingCharToNetwork(ms);
		byte[] expected = valuesToByteArray(new int[]{ms.getID(), ms.getPoint().x, ms.getPoint().y});
		checkByteArrays(output, expected);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(output);
		DataInputStream dis = new DataInputStream(bais);
		
		int[] charData = Helper.movingCharFromNetworkDynamic(dis);
		assert mcs.containsKey(charData[0]);
		assert charData[1] == ms.getPoint().x && charData[2] == ms.getPoint().y;
 	}
	
	@Test 
	public void test4_newChar() {
		int id = rrnn();
		int x = rrnn();
		int y = rrnn();
		int strat_var = rrnn();
		MovingCharacter mc = new Bee(new FlyStrategy(strat_var), null);
		mc.setPoint(new Point(x, y));
		mc.setID(id);
		
		byte[] output = Helper.movingCharToNetworkInit(mc);
		byte[] expected = valuesToByteArray(new int[]{id, x, y, 0, strat_var});
		checkByteArrays(output, expected);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(output);
		DataInputStream dis = new DataInputStream(bais);
		
		MovingCharacter mo = Helper.movingCharFromNetwork(dis);
		assert mo != null;
		assert mo.getClass().equals(mc.getClass());
		assert mc.getPoint().x == mo.getPoint().x && mc.getPoint().y == mo.getPoint().y;
		assert mc.getID() == mo.getID();
	}
	
	@Test 
	public void test5_newItemTo() {
		int id = 100;
		Item holding = new Treasure(id, null, null);
		Item item = new Chest(++ id, null, null, holding);
		byte[] output = Helper.itemToNetwork(item);
		byte[] expected = valuesToByteArray(new int[]{id, 4, -- id, 3});
		checkByteArrays(output, expected);
		
		
	}
	
	@Test 
	public void test6_itemFrom() {
		int id = 43;
		ByteArrayInputStream bais = new ByteArrayInputStream(valuesToByteArray(new int[]{id, 0}));
		DataInputStream dis = new DataInputStream(bais);
		Item i = Helper.itemFromNetwork(dis);
		assert i != null;
		assert i.getID() == id;
		assert i instanceof Fruit;
	}
	
	@Test 
	public void test7_itemTo() {
		int id = 42;
		int x = 1;
		int y = 4;
		Item fruit = new Fruit(id, null, null);
		byte[] output = Helper.itemToNetworkDynamic(fruit, x, y);
		byte[] expected = valuesToByteArray(new int[]{id, 0, x, y});
		checkByteArrays(output, expected);
	}
	
	
	
	/*
	 * Helper methods for constructing byte arrays to compare against
	 */
	
	@SuppressWarnings("unused")
	private void print(byte[] ba) {
		for (byte b : ba) System.out.print(b + ", ");
		System.out.println();
	}
	
	private void checkByteArrays(byte[] output, byte[] expected) {
		assert (output.length == expected.length);
		for (int i = 0; i < output.length; ++ i) {
			assert (output[i] == expected[i]);
		}
	}
	
	private byte[] valuesToByteArray(int[] values) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			for (int value : values) dos.writeInt(value);
		} catch (Exception e) {
			System.err.println("Failed making test array");
			assert false;
		}
		return baos.toByteArray();
	}
	
	private byte[] stringToByteArray(String str) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeUTF(str);
		} catch (Exception e) {
			System.err.println("Failed making test array");
			assert false;
		}
		return baos.toByteArray();
	}
	
	private byte[] concat(byte[][] bas) {
		int length = 0;
		for (byte[] ba : bas) length += ba.length;
		byte[] newArray = new byte[length];
		int index = 0;
		for (byte[] ba : bas) {
			for (byte b : ba) {
				newArray[index] = b;
				index ++;
			}
		}
		return newArray;
	}
	
	
	/**
	 * Return a Random Natural Number
	 * @return
	 */
	public static int rrnn() {
		return (int)(Math.random() * Integer.MAX_VALUE);
	}
}
