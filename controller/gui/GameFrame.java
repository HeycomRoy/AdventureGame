package controller.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import javax.swing.*;

import model.character.Player;
import model.game.*;
import network.Slave;
import view.levels.*;
import view.riverLevel.*;
import view.roadLevel.RoadPane;

/**
 * Creates the Frame for the GUI which holds all the components of the GUI.
 *
 * @Author Liam Reeves
 */

public class GameFrame extends JFrame {

	public Game game;
	public Player player;
	public Slave slave;
	private String level;
	private RenderPane rp;
	private Container contentPane;
	private Location[][] locationArray;

	public GameFrame(Game game, Slave slave) {
		this.slave = slave;

		// public GameFrame(Game game, Slave slave, Player player) {
		// super("Frame");
		// this.slave = slave;
		// this.player = player;
		this.game = game;
		this.player = game.players.get(slave.username);

		 System.out.println("Length of location array: " + game.getLevels().get(0).getLocations().length);
		 locationArray =  game.getLevels().get(0).getLocations();


		Dimension frameSize = new Dimension(1234, 724);
		setMinimumSize(frameSize);
		setLayout(null);
		setVisible(true);

		contentPane = getContentPane();



		contentPane.setPreferredSize(frameSize);
		contentPane.setBackground(Color.lightGray);

		// ==================== LEVELS =============================
		level = "River"; // make it work!
		switch (level) {
		case "River":
			rp = new RiverPane(this, locationArray);
			break;
		case "Road":
//			rp = new RoadPane(this);
			break;
		case "Mountain":
			// rp = new BackgroundPane();}
			break;
		default:
			rp = new RiverPane(this, locationArray);
		}
		rp.setBounds(15, 15, 684, 684);
		rp.addKeyListener(slave);
		rp.setFocusable(true);
		rp.requestFocusInWindow();

		contentPane.add(rp);

		// ==================== COMPONENTS =============================
		InventoryFrame inventory = new InventoryFrame();
		// InventoryFrame inventory = new InventoryFrame(game, player); //
		// Creates
		// inventory
		inventory.setBounds(734, 50, 401, 101);

		JButton pickBtn = new JButton("Pick Up Item"); // Creates Drop button
		pickBtn.setBounds(734, 153, 120, 25);
		pickBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.pickup(player);
			}
		});

		JButton useBtn = new JButton("Use Item"); // Creates use button
		useBtn.setBounds(864, 153, 120, 25);
		useBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.playerUseItem(player, inventory.selectedItem);
			}
		});

		JButton dropBtn = new JButton("Drop Item"); // Creates Drop button
		dropBtn.setBounds(994, 153, 120, 25);
		dropBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.dropItem(player, inventory.selectedItem);
			}
		});

		// camera left button
		ImageIcon rotateLeft = new ImageIcon("src/images/rotateLeft.png");
		JButton rotateL = new JButton(rotateLeft);
		rotateL.setBounds(734, 600, 83, 79);
		rotateL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rp.rotate("Left");
			}
		});

		// camera right button
		ImageIcon rotateRight = new ImageIcon("src/images/rotateRight.png");
		JButton rotateR = new JButton(rotateRight);
		rotateR.setBounds(984, 600, 83, 79);
		rotateR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rp.rotate("Right");
			}
		});

		PlayerList playerList = new PlayerList(game); // Creates the player list
		// panel
		playerList.setBounds(734, 0, 400, 50);

		MiniMap minimap = new MiniMap(game, player);
		minimap.setBounds(734, 203, 400, 400);

		contentPane.add(useBtn); // Adds all the buttons to the Frame
		contentPane.add(dropBtn);
		contentPane.add(inventory);
		contentPane.add(rotateL);
		contentPane.add(rotateR);
		contentPane.add(playerList);
		contentPane.add(minimap);
		contentPane.add(pickBtn);

		// ==================== LEVELS =============================

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);
		pack();


	}

	/**
	 * Adds all the buttons the the application side of the frame. This method
	 * also creates and initializes the minimap and inventory system
	 *
	 */


	/**
	 * reFresh() is the method run by Slave to update the GameFrame application
	 * window and subsequently the sprites on the rendering, according to their
	 * current location in the game
	 *
	 */


	public void reFresh() {
		System.out.println("Current level: " + game.getLevels().get(0));
		Location[][] locationArray = game.getLevels().get(0).getLocations();

		rp.updatePane(locationArray);

	}

	public Level getCurrentLevel() {
		// TODO Auto-generated method stub
		return player.getCurrentLevel();
	}
}
