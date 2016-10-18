package view.roadLevel;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import model.game.Point;
import view.levels.GameSprite;

public class Car_Right_Icon extends GameSprite{
	private ImageIcon car;
	private int speed;

	public Car_Right_Icon(String orientation, Point carLoc, int speed){
		this.orientation = orientation;

		animationLoc = carLoc;
		if (orientation.equals("N") || orientation.equals("S")) {
			car = new ImageIcon("src/images/car_top_right.png");
			spriteHt = 106;
			spriteWd = 200;

		} else if (orientation.equals("E") || orientation.equals("W")) {
			car = new ImageIcon("src/images/car_top_right.png");
			spriteHt = 106;
			spriteWd = 200;

		}
		transposeAnimation();
		setIcon(car);
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


	public void setCarLocation(){

	int carX = (int)animationLoc.getX();
	int carY = (int)animationLoc.getY();
	if(carX == 684 + Math.max(spriteWd, spriteHt)) this.setAnimLoc(-spriteWd,carY);
	else this.setAnimLoc(carX + this.speed, carY);
	transposeAnimation();

	}



}
