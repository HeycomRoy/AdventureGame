package view.levels;

import javax.swing.JLabel;

import model.game.Point;

public class GameSprite extends JLabel {
	protected String orientation;
	protected int spriteHt;
	protected int spriteWd;
	protected Point animationLoc; // animation location
	protected Point xyLocation; // screen location

	protected int stX;
	protected int stY;
	protected int localX;
	protected int localY;

	protected int count = 0;


	public void moveAcrossTile(int startX, int startY){

		switch(orientation){

		case "N":
			moveAcrossNorth(startX, startY);
			break;
		case "E":
			moveAcrossEast(startX, startY);
			break;
		case "S":
			moveAcrossSouth(startX, startY);
			break;
		case "W":
			moveAcrossWest(startX, startY);
			break;
		default:

		}

	}


	/**
	 * Used to smooth movement of a sprite across a tile at the local level
	 * startX and startY give the tile location.
	 *
	 * @param startX
	 * @param startY
	 */
	public void moveAcrossNorth(int startX, int startY) {

		double offset = (spriteWd / 17 * (17 - startX));

		stX = (startX * 38) - (int) offset;
		stY = startY;
		if (localX < stX || localX > stX + 38)
			localX = stX;
		localX = localX + 2;

		this.setLocation(localX, stY * 38);
	}

	public void moveAcrossEast(int startX, int startY) {

		double offset = (spriteWd / 17 * (17 - startX));

		stY = (startY * 38);
		stX = startX;
		
		if (localY < stY || localY > stY + 38)
			localY = stY;
		localY = localY - 2;

		this.setLocation(stX * 38, localY);
	}

	public void moveAcrossSouth(int startX, int startY) {

		double offset = (spriteWd / 17 * (17 - startX));

		stX = (startX * 38);
		stY = startY;
		if (localX < stX || localX > stX + 38)
			localX = stX;
		localX = localX - 2;

		this.setLocation(localX, stY * 38);
	}

	public void moveAcrossWest(int startX, int startY) {

		double offset = (spriteWd / 17 * (17 - startX));

		stY = (startY * 38) - (int) offset;
		stX = startX;
		if (localY < stY || localY > stY + 38)
			localY = stY;
		localY = localY + 2;

		this.setLocation(stX * 38, localY);
	}



	public Point getAnimLoc() {
		return animationLoc;
	}



	public void setAnimLoc(int newX, int newY) {
		animationLoc = new Point(newX, newY);
	}



	public void transposeAnimation() {
		xyLocation = transpose();
		this.setLocation(xyLocation.getX(), xyLocation.getY());
		validate();
		repaint();
	}



	public Point transpose() {
		int oldX = (int) animationLoc.getX();
		int oldY = (int) animationLoc.getY();
		int retX = oldX;
		int retY = oldY;
		switch (orientation) {
		case "N":
			retX = oldX;
			retY = oldY;
			break;
		case "E":
			retX = oldY;
			retY = 684 - oldX;
			break;
		case "S":
			retX = 684 - oldX;
			retY = 684 - oldY - spriteHt;
			break;
		case "W":
			retX = 684 - oldY - spriteWd;
			retY = oldX;
			break;
		default:
		}

		return new Point(retX, retY);
	}

	public int getHt() {
		return spriteHt;
	}

	public int getWd() {
		return spriteWd;
	}

	public int getSpriteX() {
		return this.getX();
	}

	public int getSpriteY() {
		return this.getY();
	}

}