package controller.initialGUI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/** 
 * Creates a new GUI window that asks the user to input various information 
 * based on the type of game they want to play. 
 * @author Liam Reeves
 * */

public class InitialFrame extends JFrame {

	public InitialFrame(){
	Dimension frameSize = new Dimension(500, 700); //Creats the frame
	setMinimumSize(frameSize);
	setLayout(new GridLayout(3, 1));
	
	createSections(); //Adds the three panels to the frame
	
	
	setVisible(true);
	}
	
	public void createSections(){
		
		//JPanel Title = new JPanel();
		//Title.setBounds(0, 0, 500, 100);
		
		SinglePlayerPanel SinglePlayer = new SinglePlayerPanel();
		//SinglePlayer.setBounds(0, 100, 500, 200);
		
		ClientPanel client = new ClientPanel();
		//client.setBounds(0, 300, 500, 200);
		
		ServerPanel server = new ServerPanel();
		//server.setBounds(0, 500, 500, 200);
		
		
		this.add(SinglePlayer);
		this.add(client);
		this.add(server);
		//this.add(Title);
		
		
	}
	
	
}
