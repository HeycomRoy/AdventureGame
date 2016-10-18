package view.riverLevel;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import model.game.Point;
import view.levels.GameSprite;

public class Log_Icon extends GameSprite{
	private ImageIcon log;
	private int speed;

	public Log_Icon(String orientation, Point point, int speed){
		this.orientation = orientation;

		animationLoc = point;
		if (orientation.equals("N") || orientation.equals("S")) {
			log = new ImageIcon("src/images/log.png");
			spriteHt = 32;
			spriteWd = 107;

		} else if (orientation.equals("E") || orientation.equals("W")) {
			log = new ImageIcon("src/images/log_top.png");
			spriteHt = 107;
			spriteWd = 50;

		}
		//transposeAnimation();
		setIcon(log);
		setSize(spriteWd, spriteHt);

		this.speed = speed;
	}


	public int getSpeed(){
		return speed;
	}

	@Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

    }


	public void setLogLocation(){

	int logX = (int)animationLoc.getX();
	int logY = (int)animationLoc.getY();
	if(logX == 684 + Math.max(spriteWd, spriteHt)) this.setAnimLoc(-spriteWd,logY);
	else this.setAnimLoc(logX + this.speed, logY);
	transposeAnimation();

	}



}
