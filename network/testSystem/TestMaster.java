package network.testSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Master is the listening process receiving events from slaves
 * Also pushes updates of the game world to slaves
 * @author forancono
 *
 */
public class TestMaster extends Thread {

	private final TestGame tg; // shared game world
	private final int clock; // server sleep time
	private final int id; // id of master - slave pair
	private final Socket sock; 

	private DataInputStream in;
	private DataOutputStream out;

	public TestMaster(Socket s, int i, int c, TestGame g) {
		tg = g;
		clock = c;
		sock = s;
		id = i;
	}

	public void run() {
        try {
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            // send the slave the id of the pair
            out.writeInt(id);
            out.flush();            
            boolean loop = true;
            while (loop) {
                
                // check if the client has sent anything
                if (in.available() != 0) {
                    char direction = in.readChar();
                    String who = sock.getInetAddress().getHostAddress();
                    switch (direction) {
                        case TestSlave.UP :
                            System.out.println("Player " + id + " @ " + who + " wants up");
                            tg.moveUp(id);
                            break;
                        case TestSlave.LEFT :
                            System.out.println("Player " + id + " @ " + who + " wants left");
                            tg.moveLeft(id);
                            break;
                        case TestSlave.DOWN :
                            System.out.println("Player " + id + " @ " + who + " wants down");
                            tg.moveDown(id);
                            break;
                        case TestSlave.RIGHT : 
                            System.out.println("Player " + id + " @ " + who + " wants right");
                            tg.moveRight(id);
                            break;                        
                    }
                }
                byte[] newPos = tg.gameToNetwork();
                out.writeInt(newPos.length);
                out.write(newPos);
                out.flush();
                sleep(clock);
            }
            sock.close();
        } catch (Exception e) {
            // client disconnected 
            tg.playerQuit(id);
            System.out.println("Player " + id + " disconnected");
            try {
                sock.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}
}
