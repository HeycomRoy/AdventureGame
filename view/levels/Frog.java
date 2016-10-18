package view.levels;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Frog extends GameSprite{
	private int x;
	private int y;
	private int delta = 3;
	
	public Frog(String orientation) {
		
		//Depending on player, load different image of frog, so we can differentiate by player on screen
		ImageIcon frog = new ImageIcon("src/images/frog.png");
		setIcon(frog);
		setSize(100, 113);
		
		Point labelPoint = this.getLocation();
		x = (int) (labelPoint.getX() + 0.5d);
		y = (int) (labelPoint.getY() + 0.5d);
		
//	
//		MotionWithKeyBindings motion = new MotionWithKeyBindings(this);
//		motion.addAction("LEFT", -delta,  0);
//		motion.addAction("RIGHT", delta,  0);
//		motion.addAction("UP",    0, -delta);
//		motion.addAction("DOWN",  0,  delta);
		
	}
	
	public void changeXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setIcon(String imageFile){
			this.setIcon(new ImageIcon("src/images/" + imageFile));
	}


	
}
