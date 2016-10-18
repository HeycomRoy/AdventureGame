package controller.initialGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
 * This panel is used for a client to join an already running server. It requires the user to
 * input a username for their character, IP Address and Port number of the server.
 * 
 * @author Liam Reeves
 * */

public class ClientPanel extends JPanel {


	public ClientPanel() {

		this.setBackground(new Color(0, 167, 255)); //Creates the Client panel
		JLabel text = new JLabel();
		text.setBounds(190, 0, 200, 50);
		Font myFont = new Font("Serif", Font.BOLD, 16);
		text.setFont(myFont);
		text.setText("<html>Client<br>Type a Username</html>");

		setLayout(null);

		JTextField username = new JTextField();
		username.setBounds(175, 50, 150, 30);

		JTextField IP = new JTextField("Please Type a Server Address");
		IP.setBounds(150, 100, 200, 20);
		IP.setVisible(true);

		JTextField port = new JTextField("Please Type a Port Number");
		port.setBounds(150, 130, 200, 20);
		port.setVisible(true);

		JButton startBtn = new JButton("Play Game");
		startBtn.setBounds(150, 160, 200, 20);
		startBtn.setVisible(true);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = username.getText();
				if (name.length() > 20) {
					JOptionPane.showMessageDialog(null, "Username too long. Max charcters = 20"); //Checks username is valid
				} else {

					String IPAddress = IP.getText();
					if(IPAddress.isEmpty()){
						JOptionPane.showMessageDialog(null, "Non Valid Address."); //Checks IP is valid - address need not be x.x.x.x format
						System.err.println("Invalid Address");	
						return;
					}

					int portNumber = Main.DEFAULT_PORT;
					try{
						portNumber = Integer.parseInt(port.getText());
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Not a number - using default port - " + Main.DEFAULT_PORT); //Checks port is valid
					}
					if (portNumber < Main.MIN_PORT || portNumber > Main.MAX_PORT) {
						JOptionPane.showMessageDialog(null, "Port number out of bounds");
					}

					Main.runSlave(IPAddress, portNumber, name); //If valid parameters joins server

				}
			}
		});


		this.add(username);
		this.add(IP);
		this.add(startBtn);
		this.add(text);
		this.add(port);

		setVisible(true);

	}

}
