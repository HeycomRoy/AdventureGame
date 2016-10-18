package network;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import controller.gui.GameFrame;
import model.game.Game;
import model.game.Level;
import model.game.Location;

/**
 * A slave is the client - gets the game state from the master and updates it's
 * local copy of the game to match
 *
 * @author forancono
 */
public class Slave extends Thread implements KeyListener {
	
	// constants of keyboard actions
	public static final char UP = 'w';
	public static final char LEFT = 'a';
	public static final char DOWN = 's';
	public static final char RIGHT = 'd';
	// socket of the slave
	private final Socket sock;
	// stream object to send and receive data from
	private DataInputStream dis;
	private DataOutputStream dos;
	// local game object
	private Game game;
	private int id;
	public String username;
	//private String gamefile;

	public Slave(Socket s, String u) {
		sock = s;
		username = u;
		System.out.println("Connected to : " + sock.getRemoteSocketAddress().toString());
		try {
			dis = new DataInputStream(sock.getInputStream());
			dos = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			System.err.println("Failed to get data streams from socket");
		}

	}
	
	public void run() {
		try {
			// send username to overlord to request player
			dos.writeUTF(username);
			dos.flush();
//			gamefile = dis.readUTF();
//			System.out.println(gamefile);
			id = dis.readInt();
			int gameSize = dis.readInt();
			byte[] gameStart = new byte[gameSize];
			dis.readFully(gameStart);
			game = new Game(gameStart);
			//game = Parser.load(gamefile);
			GameFrame gf = new GameFrame(game, this);
			// loop reading from socket
			boolean loop = true;
			int index = 0;
			while (loop) {
				// read from the server
				int size = dis.readInt();
				//System.out.println(size);
				byte[] data = new byte[size];
				dis.readFully(data);
				// update the game
				game.fromNetworkDynamic(data);
				// refresh display
				gf.reFresh();
				if (index ++ % 100 == 0) {
					game.printGame();
				}

			}
			sock.close();
			dos.close();
		} catch (Exception e) {

		}
	}

	/**
	 * Write the lower case character of a key pressed
	 * Keyboard actions to be transmitted to the server 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		try {
			char key = Character.toLowerCase(e.getKeyChar());
			switch (key) {
			case UP:
				dos.writeChar(UP);
				break;
			case LEFT:
				dos.writeChar(LEFT);
				break;
			case DOWN:
				dos.writeChar(DOWN);
				break;
			case RIGHT:
				dos.writeChar(RIGHT);
				break;
			}
			dos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	/**
	 * Gets the id of the connection - also is the id of the player
	 */
	public int getConnectionId() {
		return id;
	}
}
