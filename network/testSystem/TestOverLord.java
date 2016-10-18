package network.testSystem;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;


/**
 * An OverLord is the single thread which listens for incomming connections 
 */
 
public class TestOverLord extends Thread {
    
    public static final int MAX_CONNECTIONS = 128;
    public static final int MAX_BACKLOG = 5;
    public static final int SLEEP = 50;
    
    private TestGame game;
    private int port;
    
    public TestOverLord(TestGame g, int p) {
        game = g;
        port = p;
    }
    
    public void run() {
        TestMaster[] overseers = new TestMaster[MAX_CONNECTIONS];
        ServerSocket masterMaster = null; 
        try {
            
            try {
                masterMaster = new ServerSocket(port, MAX_BACKLOG);
            } catch (IOException e) {
                masterMaster = new ServerSocket(0, MAX_BACKLOG);
            }
            System.out.println("bound to port - " + masterMaster.getLocalPort());
            int i = 0;
            // wait for all connections
            while (true) { // loop listening to new connections
                if (i < MAX_CONNECTIONS) {
                    Socket newConnection = masterMaster.accept();
                    System.out.println("New connection from " + newConnection.getInetAddress().getHostAddress());
                    int id = game.newPlayer();
                    overseers[i] = new TestMaster(newConnection, id, SLEEP, game);
                    overseers[i].start();
                    i ++;
                } else {
                    Thread.yield(); // do nothing
                }          
            }
            
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
        	try {
        		masterMaster.close();
        	} catch (IOException e) {
        		System.err.println("Failed to close listening socket\n" + e.getMessage());
        	}
         }
    }
    
    public int getPort() {
        return port;
    }
}
