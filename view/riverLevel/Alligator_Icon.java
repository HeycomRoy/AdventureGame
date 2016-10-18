package view.riverLevel;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import model.game.Point;
import view.levels.GameSprite;

public class Alligator_Icon extends GameSprite {
	private ImageIcon alligator;
	private int speed = 2;

	public Alligator_Icon(String orientation) {
		animationLoc = new Point(10, 342);
		this.orientation = orientation;
		switch (orientation) {
		case "N":
			alligator = new ImageIcon("src/images/alligator_north.png");
			spriteHt = 73;
			spriteWd = 200;
			break;
		case "E":
			alligator = new ImageIcon("src/images/alligator_east.png");
			spriteHt = 200;
			spriteWd = 73;
			break;
		case "S":
			alligator = new ImageIcon("src/images/alligator_south.png");
			spriteHt = 73;
			spriteWd = 200;
			break;
		case "W":
			alligator = new ImageIcon("src/images/alligator_west.png");
			spriteHt = 200;
			spriteWd = 73;
			break;
		default:

		}
		transposeAnimation();
		setIcon(alligator);
		setSize(spriteWd, spriteHt);

	}


	public void setAlliLocation(){

		int alliX = (int)animationLoc.getX();
		int alliY = (int)animationLoc.getY();
		if(alliX == 684 + Math.max(spriteWd, spriteHt)) this.setAnimLoc(-spriteWd,alliY);
		else this.setAnimLoc(alliX + this.speed, alliY);
		transposeAnimation();

		}

	public int getSpeed() {
		return speed;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

	}







}
