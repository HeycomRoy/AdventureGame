package model.game;

/**
 *This class represents a point on the world(board)
 *
 * @author yaoyucui
 *
 */
public class Point{

	public int x;
	public int y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Method is needed to match setLocation in java.awt.Point
	 *
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y){

		this.x = x;
		this.y = y;

	}


}
