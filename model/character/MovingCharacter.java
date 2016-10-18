package model.character;

import model.game.Location;
import model.game.Point;
/**
 * This abstract moving character class 
 * @author yaoyucui
 *
 */
public abstract class MovingCharacter{
	
	private Point point;
	protected String mcID;
	protected int speed;
	protected Strategy strategy;
	protected Location location;
	private Direction direction;
	private int id; // unique identification for saving/loading/through network

	private Sprite sprite;
	
	public enum Sprite{
		BEE, ALLIGATOR, HIPPO, BAT,
	}
	
	public enum Direction {
        NORTH, EAST, SOUTH, WEST;
	}

	public MovingCharacter(){}
	public MovingCharacter(Strategy strategy, Location location) {
		this.strategy = strategy;
		this.location = location;
		
	}

	public Point getPoint() {
		return point;
	}

	
	public void setPoint(Point point){
		this.point = point;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Strategy getStrategy(){
		return this.strategy;
	}

	public void setStrategy(Strategy strategy){
		this.strategy = strategy;
	}
	
	/**
	 *  the characteristics of the move is determined in each
	 *  separate character's class
	 */
	public abstract void move();

	public Location getLocation(){
		return this.location;
	}

	public void setLocation(Location loc){
		this.location = loc;
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public String getMCID(){
		return mcID;
	}
	
	public int getID() {
		return this.id;
	}

	public void setID(int i) {
		this.id = i;
	}
}
