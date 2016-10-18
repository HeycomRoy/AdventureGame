package controller.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class Buttons extends JButton {
	
	public File I;
	
	public Buttons(String name, File I){
		super(name);
		this.I = I;
	}
	
	public void paint(Graphics g){
		try {
			g.drawImage(ImageIO.read(I), 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}
