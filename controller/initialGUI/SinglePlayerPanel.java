package controller.initialGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import model.game.Game;
import util.Parser;

/**
 * This panel creates a new single player game. It asks the user for a Username and Game file. 
 * 
 * @author Liam Reeves
 * */

public class SinglePlayerPanel extends JPanel {

	public File gameFile;

	public SinglePlayerPanel() {

		this.setBackground(new Color(0, 255, 0)); //Creates the singleplayer panel
		JLabel text = new JLabel();
		text.setBounds(190, 0, 200, 50);
		Font myFont = new Font("Serif", Font.BOLD, 16);
		text.setFont(myFont);
		text.setText("<html>Single Player<br>Type a Username</html>");

		setLayout(null);

		JTextField username = new JTextField();
		username.setBounds(175, 50, 150, 50);

		JButton fileBtn = new JButton("Please Select a Game File");
		fileBtn.setBounds(150, 120, 200, 30);
		fileBtn.setVisible(true);
		fileBtn.addActionListener(new ActionListener() { //selects the game file
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				gameFile = chooser.getSelectedFile();
				fileBtn.setText(gameFile.getName());
			}
		});

		JButton startBtn = new JButton("Play Game");
		startBtn.setBounds(150, 160, 200, 30);
		startBtn.setVisible(true);
		startBtn.addActionListener(new ActionListener() { //Runs the game as long as all the parameters are valid
			public void actionPerformed(ActionEvent e) {
				String name = username.getText();
				if (name.length() > 20) {
					JOptionPane.showMessageDialog(null, "Username too long. Max charcters = 20");
				} else {
					Game game = null;
					try {
						 game = Parser.load(gameFile);
					} catch (FileNotFoundException fnfe) {
						JOptionPane.showMessageDialog(null, "Failed to load game file");
						System.err.println("Failed to load game file");	
						return;
					}
					Main.runHost(game, Main.DEFAULT_PORT, name, null); //If valid parameters runs the single player game

				}
			}
		});

		this.add(username);
		this.add(fileBtn);
		this.add(startBtn);
		this.add(text);

		setVisible(true);

	}

}
