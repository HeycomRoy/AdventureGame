package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import controller.initialGUI.InitialFrame;
import model.game.Game;
import network.OverLord;
import network.Slave;
import util.Parser;

/**
 * Frog game
 */

public class Main {

	public static final int DEFAULT_PORT = 22222;
	public static final int MIN_PORT = 5001;
	public static final int MAX_PORT = 65535;
	public static final String DEFAULT_FILE = "./src/util/multiLevelGame.xml";
	public static final String DEFAULT_USER = "David Pearce";

	public static final int MAX_NAME = 20;


	public static final String usage = "Options :\n"
			+ "--master | -M : declare this program as a server\n"
			+ "--single | -S : declare this program as single player\n"
			+ "--connect | -C <server_address> : specify the server address to connect to\n"
			+ "--port | -P <port_number> : specify the port number to connect to(client) or listen on(server)\n"
			+ "--game | -G <file_path> : specify a game file to load\n"
			+ "--name | -N <username> : specify a username for the player\n";

	/*
	 * Main entry point of program
	 */
	public static void main(String[] args) {
		
		boolean noargs = false;
		if (args.length == 0) noargs = true;
		
		boolean master = false;
		boolean single_player = false;
		int port = -1;
		String address = null;
		String filename = null;
		String username = null;


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
					if (i >= args.length - 1) argsError("No server address specified");
					address = args[++ i];
					break;
				case "-port" :
				case "P" :
					if (i >= args.length - 1) argsError("No port number specified");
					port = Integer.parseInt(args[++ i]);
					break;
				case "-game" :
				case "G" :
					if (i >= args.length -1) argsError("No game file specified");
					filename = args[++ i];
					break;
				case "-name" :
				case "N" :
					if (i >= args.length - 1) argsError("No player name specified");
					username = args[++ i];
					if (username.length() > MAX_NAME) username = username.substring(0, MAX_NAME);
					break;
				case "-single" :
				case "S" :
					single_player = true;
					break;
				}
			}
		}

		// checks on command line options
		if (master) {
			if (address != null) argsError("Can't be server and client explicitly");
			if (single_player) argsError("Single player needs no master");
		}

		if (single_player) {
			if (address != null) argsError("Single player need not connect to a server");
		}

		if (!noargs) {
			// check for username
			if (single_player || address != null) {
				if (username == null) {
					System.out.println("No username specified, using default - " + DEFAULT_USER);
					username = DEFAULT_USER;
				}
			}
	
			// check for port number
			if (port == -1) {
				System.out.println("Using default port - " + DEFAULT_PORT);
				port = DEFAULT_PORT;
			}
	
			// check if port number is in range
			if (port < MIN_PORT || port > MAX_PORT) {
				argsError("Port number not within the range " + MIN_PORT + " - " + MAX_PORT);				
			}
	
			// check for game file
			if (filename == null) {
				System.out.println("No game file specified, using default - " + DEFAULT_FILE);
				filename = DEFAULT_FILE;
			}
		}

		// run game with options
		if (master) { // server
			Game game = null;
			String gamefile = null;
			try {
				gamefile = filetoString(new File(filename));
				game = Parser.load(gamefile);
			} catch (FileNotFoundException e) {
				argsError("Game file not found");
			}
			runMaster(game, port, gamefile);
		} else if (address != null) { // client
			runSlave(address, port, username);
		} else if (single_player) { // single player game
			Game game = null;
			String gamefile = null;
			try {
				gamefile = filetoString(new File(filename));
				game = Parser.load(new File(filename));
			} catch (FileNotFoundException e) {
				argsError("Game file not found");
			}
			runHost(game, DEFAULT_PORT, username, gamefile);
		} else new InitialFrame();
	}

	private static String filetoString(File file) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			return sb.toString();
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close(); 
			} catch (IOException e) {}
		}
		
	}

	/*
	 * Run server from here
	 */
	public static void runMaster(Game game, int port, String gamefile) {
		System.out.println("Running master");
		OverLord ol = new OverLord(game, port, gamefile);
		ol.start();
	}

	/*
	 * Run a single player from here
	 */
	public static void runHost(Game game, int port, String username, String gamefile) {
		System.out.println("Running single player ");
		OverLord ol = new OverLord(game, port, gamefile);
		ol.start();
		try {
			Thread.sleep(100);
			runSlave(InetAddress.getLocalHost().getHostName(), ol.getPort(), username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Run a client from here
	 */
	public static void runSlave(String address, int port, String username) {
		// new slave to socket
		Socket sock = null;
		try {
			sock = new Socket(address, port);
		} catch (Exception e) {
			System.out.println("Cannot connect to master");
			return;
		}
		System.out.println("Running slave");
		new Slave(sock, username).start();
	}

	/*
	 * Fatal error in arguments - print usage and exit
	 */
	public static void argsError(String comment) {
		System.out.println("Invalid arguments - " + comment);
		System.out.println(usage);
		System.exit(1);
	}
}

