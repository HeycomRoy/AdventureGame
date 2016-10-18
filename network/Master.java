package network;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import model.game.Game;
/**
 * Master is the listening process receiving events from slaves
 * Also pushes updates of the game world to slaves
 *
 * @author forancono
 */
public class Master extends Thread {

	private final Game game;  // shared game world
	private final int clock; // server sleep time
	private final int id; // id of master - slave pair
	private final Socket sock;
	private DataInputStream dis;
	private DataOutputStream dos;
	public Master(Socket s, int i, int c, Game g) {
		game = g;
		clock = c;
		sock = s;
		id = i;
	}
	
	public void run() {
        try {
            dis = new DataInputStream(sock.getInputStream());
            dos = new DataOutputStream(sock.getOutputStream());
            // send the slave it's id
            dos.writeInt(id);
            dos.flush();        
            // send the initial game state 
            //System.out.println("Before");
            byte[] init = game.toNetworkStatic();
            //.out.println("After");
            dos.writeInt(init.length);
            dos.write(init);
            dos.flush();
            boolean loop = true;
            int index = 0;
            while (loop) {
            	// check if the client has sent anything
                if (dis.available() != 0) {
                	char direction = dis.readChar();
                	String who = sock.getInetAddress().getHostAddress();

                	try {
                		System.out.println(sock.getInetAddress().getHostAddress());

                		switch (direction) {
                		case Slave.UP :
                			System.out.println(id + " @ " + who + " wants up");
                			game.getPlayer(id).moveUp();
                			break;
                		case Slave.LEFT :
                			System.out.println(id + " @ " + who + " wants left");
                			game.getPlayer(id).moveLeft();
                			break;
                		case Slave.DOWN :
                			System.out.println(id + " @ " + who + " wants down");
                			game.getPlayer(id).moveDown();
                			break;
                		case Slave.RIGHT :
                			System.out.println(id + " @ " + who + " wants right");
                			game.getPlayer(id).moveRight();
                			break;
                		}
                	} catch (Exception e) {
                		System.err.println("Game failed to do input");
                	}
                }
                // send dynamic game world
                byte[] newWorld = game.toNetworkDynamic();
                dos.writeInt(newWorld.length);
                dos.write(newWorld);
                dos.flush();
                try {
                	sleep(clock);
                	if ((index ++ % 500) == 0) {
                		//game.printGame();
                	}
                } catch (InterruptedException ie) {
                	System.err.println("Master thread failed to sleep all night");
                }
            }
            sock.close();
        } catch (IOException ioe) {
        	// client disconnected
        	try {
        		game.removePlayer(game.getPlayer(id));
        		sock.close();
        	} catch (Exception e) {
        		System.err.println("Master thread failed to end connection gracefully");
			}
        	System.out.println("Player - " + game.getPlayer(id).getPlayerName() + " disconnected");
        }
	}
}