package controller.initialGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import model.game.Game;
import util.Parser;

/**
 * This panel creates a new server for clients to join. It asks the user for a port number and 
 * a game file to load
 * @author Liam Reeves
 * */

public class ServerPanel extends JPanel {

	public File gameFile;

	public ServerPanel() {

		this.setBackground(new Color(0, 255, 255)); //creates the server panel
		JLabel text = new JLabel();
		text.setBounds(190, 0, 200, 50);
		Font myFont = new Font("Serif", Font.BOLD, 16);
		text.setFont(myFont);
		text.setText("<html>Server<br>Type a port number</html>");

		setLayout(null);

		JTextField port = new JTextField();
		port.setBounds(175, 50, 150, 50);

		JButton fileBtn = new JButton("Please Select a Game File");
		fileBtn.setBounds(150, 120, 200, 30);
		fileBtn.setVisible(true);
		fileBtn.addActionListener(new ActionListener() { //selects file
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				gameFile = chooser.getSelectedFile();
				fileBtn.setText(gameFile.getName());
			}
		});

		JButton startBtn = new JButton("Start Server");
		startBtn.setBounds(150, 160, 200, 30);
		startBtn.setVisible(true);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int portNumber = Main.DEFAULT_PORT;
				try {
					portNumber = Integer.parseInt(port.getText()); //check port is valid
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Not a number - using default port - " + Main.DEFAULT_PORT);
				}
				if (portNumber < Main.MIN_PORT || portNumber > Main.MAX_PORT) {
					JOptionPane.showMessageDialog(null, "Port number out of bounds");
				} else {
					Game game = null;
					try {
						 game = Parser.load(gameFile);
					} catch (FileNotFoundException fnfe) {
						JOptionPane.showMessageDialog(null, "Failed to load game file"); //checks file is valid
						System.err.println("Failed to load game file");	
						return;
					}
					
					
					Main.runMaster(game, portNumber, null); //runs the server if parameters are valid

				}
			}
		});

		fileBtn.setOpaque(true);

		this.add(port);
		this.add(fileBtn);
		this.add(startBtn);
		this.add(text);

		setVisible(true);

	}
	
}
