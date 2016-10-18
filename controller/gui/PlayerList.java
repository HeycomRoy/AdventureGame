package controller.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.character.Player;
import model.game.Game;
import model.game.Location;

/**
 * Creates a Jpanel to display the current players on the server
 * 
 * @Author Liam Reeves
 */

public class PlayerList extends JPanel {

	public PlayerList(Game game) {


		JLabel caption = new JLabel("Current Players:");
		JLabel playersLbl = new JLabel();

		String names = "";
		for (Player p : game.players.values()) {
			names = names + p.getPlayerName() + ", "; // Can change to player names
		}

		playersLbl.setText(names);

		setMinimumSize(new Dimension(400, 50));
		setPreferredSize(new Dimension(400, 50));

		add(caption);
		add(playersLbl);

	}

}
