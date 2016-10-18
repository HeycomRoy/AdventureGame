package network.testSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TestGame {

    // Game board
    public static final int SIZE = 18;
    public static final char TILE = ' ';
    public static final char PLAYER = ' ';
    public static final char N = '^';
    public static final char S = 'v';
    public static final char E = '>';
    public static final char W = '<';
    private char[][] level = new char[SIZE][SIZE];

    private Map<Integer, TestPlayer> players = new HashMap<>();

    private int nextPlayer = 0;

    public TestGame() {
        for (int i = 0; i < SIZE; ++ i) {
            for (int j = 0; j < SIZE; ++ j) {
                level[i][j] = TILE;
            }
        }
    }

    public synchronized TestPlayer player(int id) {
        return players.get(id);
    }

    public synchronized int newPlayer() {
        int x = (int)(Math.random() * SIZE);
        int y = (int)(Math.random() * SIZE);
        TestPlayer newP = new TestPlayer(x, y);
        players.put(++ nextPlayer, newP);
        return nextPlayer;
    }

    public synchronized void playerQuit(int id) {
        clearPlayer(players.remove(id));
    }

    public void clearPlayer(TestPlayer tp) {
        level[tp.x][tp.y] = TILE;
    }

    public void drawPlayer(TestPlayer tp) {
        switch (tp.d) {
            case NORTH :
                level[tp.x][tp.y] = N;
                break;
            case SOUTH :
                level[tp.x][tp.y] = S;
                break;
            case EAST :
                level[tp.x][tp.y] = E;
                break;
            case WEST :
                level[tp.x][tp.y] = W;
                break;
        }
    }

    public synchronized void gameFromNetwork(byte[] input) {
        try {
            ByteArrayInputStream b = new ByteArrayInputStream(input);
            DataInputStream d = new DataInputStream(b);
            int numPlayers = d.readInt();
            for (int i = 0; i < numPlayers; ++ i) {
                int id = d.readInt();
                TestPlayer old = player(id);

                if (old != null) {
                    clearPlayer(old);
                    old.x = d.readInt();
                    old.y = d.readInt();
                    switch (d.readInt()) {
                        case 0 :
                            old.d = Direction.NORTH;
                            break;
                        case 1 :
                            old.d = Direction.EAST;
                            break;
                        case 2 :
                            old.d = Direction.SOUTH;
                            break;
                        case 3 :
                            old.d = Direction.WEST;
                            break;
                    }
                    drawPlayer(old);
                } else {
                    TestPlayer tp = new TestPlayer(d.readInt(), d.readInt());
                    players.put(id, tp);
                    drawPlayer(tp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] gameToNetwork() {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream d = new DataOutputStream(b);
            d.writeInt(players.size());
            for (Map.Entry<Integer, TestPlayer> ent : players.entrySet()) {
                TestPlayer p = ent.getValue();
                int id = ent.getKey();
                int x = p.x;
                int y = p.y;
                d.writeInt(id);
                d.writeInt(x);
                d.writeInt(y);
                switch (p.d) {
                    case NORTH :
                        d.writeInt(0);
                        break;
                    case EAST :
                        d.writeInt(1);
                        break;
                    case SOUTH :
                        d.writeInt(2);
                        break;
                    case WEST :
                        d.writeInt(3);
                        break;
                }
            }
            for (int i = 0; i < 10000; ++ i) {
                d.writeInt(0);
            }
            return b.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public char[] toCharArray() {
        char[] output = new char[SIZE * SIZE];
        for (int i = 0; i < SIZE; ++ i) {
            for (int j = 0; j < SIZE; ++ j) {
                int index = i * SIZE + j;
                output[index] = level[i][j];
            }
        }
        return output;
    }

    public boolean isClear(int x, int y) {
        for (Map.Entry<Integer, TestPlayer> player : players.entrySet()) {
            TestPlayer p = player.getValue();
            if (p.x == x && p.y == y) return false;
        }
        return true;
    }

    public synchronized void moveLeft(int id) {
        TestPlayer p = player(id);
        p.left();
        drawPlayer(p);
    }

    public synchronized void moveUp(int id) {
        TestPlayer p = player(id);
        int newx, newy;
        switch (p.d) {
            case NORTH :
                if (p.x == 0) newx = SIZE - 1;
                else newx = p.x - 1;
                if (isClear(newx, p.y)) {
                    clearPlayer(p);
                    p.x = newx;
                    drawPlayer(p);
                }
                break;
            case EAST :
                if (p.y == SIZE - 1) newy = 0;
                else newy = p.y + 1;
                if (isClear(p.x, newy)) {
                    clearPlayer(p);
                    p.y = newy;
                    drawPlayer(p);
                }
                break;
            case SOUTH :
                if (p.x == SIZE - 1) newx = 0;
                else newx = p.x + 1;
                if (isClear(newx, p.y)) {
                    clearPlayer(p);
                    p.x = newx;
                    drawPlayer(p);
                }
                break;
            case WEST :
                if (p.y == 0) newy = SIZE - 1;
                else newy = p.y - 1;
                if (isClear(p.x, newy)) {
                    clearPlayer(p);
                    p.y = newy;
                    drawPlayer(p);
                }
                break;
        }
    }

    public synchronized void moveRight(int id) {
        TestPlayer p = player(id);
        p.right();
        drawPlayer(p);
    }

    public synchronized void moveDown(int id) {
        TestPlayer p = player(id);
        p.left();
        p.left();
        drawPlayer(p);
    }

    // fields for main
    public static final int DEFAULT_PORT = 22222;
    public static final int MAX_CONNECTIONS = 128;
    public static final int SLEEP_TIME = 50;

    public static void main(String args[]) {
        boolean master = false;
        int port = DEFAULT_PORT;

        String address = null;

        // loop through command line arguments for server client options
        for (int i = 0; i != args.length; ++ i) {
            if (args[i].startsWith("-")) {
                String arg = args[i].substring(1);
                switch (arg) {
                    case "-master" :
                    case "M" :
                        master = true;
                        break;
                    case "-connect" :
                    case "C" :
                        if (i >= args.length - 1) argsError();
                        address = args[++ i];
                        break;
                    case "-port" :
                    case "P" :
                        if (i >= args.length - 1) argsError();
                        port = Integer.parseInt(args[++ i]);
                        break;
                }
            }
        }

        // checks on command line options
        if (address != null && master) {
            System.out.println("Cannot be a master and a slave");
            System.exit(1);
        }
        // check port number is in range
        if (port < 5001 || port > 65535) {
            System.out.println("Incorrect port number");
            System.exit(1);
        }

        // run the game
        if (master) {                           // server
            runMaster(new TestGame(), port);
        } else if (address != null) {           // client
            runSlave(address, port);
        } else {                                // single player = server + client
            runHost(new TestGame(), port);
        }
    }

    private static void runMaster(TestGame game, int port) {
        TestOverLord ol = new TestOverLord(game, port);
        ol.start();
    }

    private static void runHost(TestGame game, int port) {
        TestOverLord ol = new TestOverLord(game, port);
        ol.start();
        try {
            Thread.sleep(100);
            runSlave(InetAddress.getLocalHost().getHostName(), ol.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runSlave(String address, int port) {
        // new slave to socket
        Socket sock = null;
		try {
			sock = new Socket(address, port);
		} catch (Exception e) {
			System.err.println("Failed to connect");
			e.printStackTrace();
			System.exit(1);
		}
		String masterAddress = sock.getInetAddress().getHostAddress();
		System.out.println("Connected to " + port + " @ " + masterAddress);
        new TestSlave(sock).run();
    }


    public static void argsError() {
        System.out.println("Usage for server - java TestGame --master \n" +
            "Usage for client - java --connect <hostname> --port <port number>");
        System.exit(1);
    }


    /*
     * Player in test game, known by coordinates
     */
    public class TestPlayer {
        public int x;
        public int y;
        public Direction d;

        public TestPlayer(int x, int y) {
            this.x = x;
            this.y = y;
            this.d = Direction.NORTH;
        }

        public void left() {
            switch(d) {
                case NORTH :
                    d = Direction.WEST;
                    break;
                case SOUTH :
                    d = Direction.EAST;
                    break;
                case EAST :
                    d = Direction.NORTH;
                    break;
                case WEST :
                    d = Direction.SOUTH;
                    break;
            }
        }

        public void right() {
            switch(d) {
                case NORTH :
                    d = Direction.EAST;
                    break;
                case SOUTH :
                    d = Direction.WEST;
                    break;
                case EAST :
                    d = Direction.SOUTH;
                    break;
                case WEST :
                    d = Direction.NORTH;
                    break;
            }
        }
    }

    public enum Direction {
            NORTH, EAST, SOUTH, WEST
    }

}
