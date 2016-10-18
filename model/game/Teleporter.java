package model.game;



/**
 * This class represents a Teleporter object
 * @author yaoyucui
 */
public class Teleporter extends Location{

	Level toLevel;

	/**
	 * Constructor
	 * @param x
	 * @param y
	 * @param type
	 * @param level
	 */
	public Teleporter(int x, int y, Type type, Level level) { //the type here should be path
		super(x, y, type);
		this.toLevel = level;
	}

	public Level getOtherWorld() {
		return toLevel;
	}

	@Override
	public String toString(){
		return "T";
	}
}
