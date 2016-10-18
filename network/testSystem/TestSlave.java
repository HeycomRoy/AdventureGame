package network.testSystem;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A slave is the client - gets the game state from the master
 * and updates it's local copy of the game to match
 */
public class TestSlave extends Thread implements KeyListener {

    public static final char UP = 'w';
    public static final char LEFT = 'a';
    public static final char DOWN = 's';
    public static final char RIGHT = 'd';
    
    private int id = 0;
    
    // socket of the slave
    private final Socket sock;

    // stream object to send and recieve data from
    private DataInputStream in;
    private DataOutputStream out;

    // local game object
  //  private Game game;
    private TestGame tg;
    
    public TestSlave(Socket s) {
        sock = s;
    }
        
    public void run() {
        try {
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            // get id
            id = in.readInt();
            System.out.println("Player " + id);
            // initialise the static parts of the game
            tg = new TestGame();
            // create local display
            TestFrame tf = new TestFrame(tg, this);
            // loop reading from socket
            boolean loop = true;
            while(loop) {
                // read game
                int size = in.readInt();
                //System.out.println(size);
                byte[] data = new byte[size];
                in.readFully(data);
                // update game
                tg.gameFromNetwork(data);
                // refresh display
                tf.reFresh(tg.toCharArray());
            }       
            sock.close();
         } catch (Exception e) {
            System.out.println("Server down!");
            System.exit(1);
            //e.printStackTrace();
         }
    }

    @Override
	public void keyPressed(KeyEvent e) {
		try {
		    
	    	char key = Character.toLowerCase(e.getKeyChar());
	    	//System.out.println(key);
    		switch (key) {
		        case UP :
		            out.writeChar(UP);
		            break;
		        case LEFT :
		            out.writeChar(LEFT);
		            break;
		        case DOWN :
		            out.writeChar(DOWN);
		            break;
                case RIGHT :
                    out.writeChar(RIGHT);
                    break;
		    }
		    out.flush();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}
