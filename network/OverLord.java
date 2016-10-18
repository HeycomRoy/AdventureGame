package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import model.character.Player;
import model.game.ClockThread;
import model.game.Game;
import model.game.Point;

/**
 * An OverLord is the single thread which listens for incoming connections
 * 
 * @author forancono
 */

public class OverLord extends Thread {

	public static final int MAX_BACKLOG = 5;
	public static final int SLEEP = 20;

	private Game game;
	private int port;
	//private String gamefile;

	public OverLord(Game g, int p, String gamefile) {
		game = g;
		port = p;
		new ClockThread(SLEEP, game).start();
		//this.gamefile = gamefile;
	}

	public void run() {
		Map<Integer, Master> masters = new HashMap<>();
		ServerSocket masterMaster = null;
		try {
			// attempt to use desired port number else pick free port
			try {
				masterMaster = new ServerSocket(port, MAX_BACKLOG);
			} catch (IOException e) {
				System.err.println("Specified port not available, OS choosing free port");
				masterMaster = new ServerSocket(0, MAX_BACKLOG);
			}
			System.out.println("Bound to port - " + masterMaster.getLocalPort());
			port = masterMaster.getLocalPort();
			
			// loop listening to new connections
			while (true) { 
				Socket newConnection = masterMaster.accept();
				System.out.println("New connection from : " + newConnection.getRemoteSocketAddress().toString());
				// get a username from the client
				String username = Helper.getUserName(new DataInputStream(newConnection.getInputStream()));
				System.out.println(username);
				// send a String of the save file to the client
//				DataOutputStream dos = new DataOutputStream(newConnection.getOutputStream());
//				dos.writeUTF(gamefile);
//				dos.flush();
				int id;
				if (game.hasUser(username)) {
					id = game.getPlayerID(username);
					System.out.println(id);
					if (masters.containsKey(id)) {
						System.out.println("Hi");
						id = game.addPlayer(username + "_1");
						System.out.println(id);
						
					}
				}else id = game.addPlayer(username);
				Player player = game.getPlayer(id);
				player.setPoint(new Point(0, 0));
				masters.put(id, new Master(newConnection, id, SLEEP, game));
				masters.get(id).start();
			}
		} catch (IOException ioe) {
			// what leads here?
		} finally {
			try {
				masterMaster.close();
			} catch (IOException e) {
				System.err.println("Failed to close listening socket");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Gets the port number associated with this overlord
	 * @return
	 */
	public int getPort() {
		return port;
	}
}
