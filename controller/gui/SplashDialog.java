package controller.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SplashDialog extends JDialog{
	
	Dimension dim = new Dimension(350,230);
	Dimension btnDim = new Dimension(100,50);
	
	public SplashDialog(){
		setLayout(null);
		setMinimumSize(dim);
		getContentPane().setBackground(new Color(230, 180, 20));
		
		JLabel nameCaption = new JLabel("Enter Name:");
		nameCaption.setBounds(20,20,120,30);
		
		JTextField nameField = new JTextField();
		nameField.setBounds(20, 60, 120, 30);
	
		String[] levelStr = {"Mountain", "River", "Road"};
		JComboBox selectLevel = new JComboBox(levelStr);
		selectLevel.setSelectedIndex(1);
		selectLevel.setBounds(170,60,120,30);
				
		JButton start = new JButton("Start");
		start.setBounds(170, 120, 100, 30);
		
		add(nameCaption);
		add(nameField);
		add(selectLevel);
		add(start);
		setVisible(true);
	
	}
	
	public static void main(String[] args){
		SplashDialog sD = new SplashDialog();
	}
	

}
