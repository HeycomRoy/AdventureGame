package view.renderTesting;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.character.MovingCharacter;
import model.game.Location;
import model.game.Point;
import view.levels.Frog;
import view.levels.TilePane;
import view.levels.Tree;
import view.levels.ViewLabel;
import view.renderTesting.RenderPane;
import view.riverLevel.Bee_Icon;
import view.riverLevel.Bridge;
import view.riverLevel.BridgeFront;
import view.roadLevel.Car_Left_Icon;
import view.roadLevel.Car_Right_Icon;
import view.roadLevel.RoadBackground;

/**
 * RiverPane is the parent JLayeredPane for the River Level.
 * It holds the river background and various components of the level.
 *
 * @author leonarglen
 *
 */

public class RoadPanel extends RenderPane implements KeyListener {

	private RoadBackground roadBG;
	private Bridge bridgeLabel;
	private TestFrame parentFrame;

	private Bee_Icon beeLabel;
	private Tree treeLabel;
	private Tree tree2Label;
	private Frog frogLabel;
	private List<Car_Left_Icon> carsL = new ArrayList<Car_Left_Icon>();
	private List<Car_Right_Icon> carsR = new ArrayList<Car_Right_Icon>();


public RoadPanel(TestFrame parent, Location[][] locationArray) {
		orientation = "N";
		
		this.locationArray = locationArray;

		this.parentFrame = parent;
		
		addItemsToLevel();

		
	}

	/**
	 * When the left and right rotate buttons are clicked, this method
	 * picks the next appropriate orientation to move to.
	 *
	 */
	public void rotate(String dir) {
		super.rotate(dir);

		carsL.clear();
		carsR.clear();
		this.removeAll();
		addItemsToLevel();
		validate();
		repaint();

	}
	
	/**
	 * updatePane is called by the parent JFrame at each clock tick
	 * so the components of the rendering are updated
	 * 
	 */
	public void updatePane() {
		super.updatePane(locationArray);
		
		// logCounter is used in the River Level
		int carCounter = 0;
		// ==================== MOVING CHARACTERS ==========================
		for(Map.Entry<Integer, MovingCharacter> entry: currentLevel.getMovingCharacters().entrySet()){
			MovingCharacter mc = entry.getValue();
			
			int beeX = mc.getPoint().getX();
			int beeY = mc.getPoint().getY();

			Point rotatedPoint = transposeMC(mc.getPoint());

			int mcX = rotatedPoint.getX();
			int mcY = rotatedPoint.getY();
			
			switch(mc.getMCID()){
			case "B":
				setBeeLocation(beeX, beeY);
				break;
				
			case "CL":
				// move each Log_Icon read from the RiverPane list according to
				// the matching index of the Log in the Game
				
				if (carCounter == carsL.size())
					carCounter = 0;
				Car_Left_Icon car = carsL.get(carCounter);
				
				car.moveAcrossTile(mcX, mcY);
				carCounter++;
				break;
			default:
				
			}
		}
	}
	
	
	
	
	
/**
 * addItemsToLevel method to add the relevant components to this level, including the
 * background. Items are added to different layers so that moving sprites can pass over
 * or under them.
 *
 */
	private void addItemsToLevel() {

		roadBG = new RoadBackground(orientation, locationArray);
		add(roadBG, new Integer(0));
		roadBG.setBounds(0, 0, paneWidth, paneHeight);

		frogLabel = new Frog(orientation);
		add(frogLabel, new Integer(8));
		frogLabel.setBounds(450, 450, 100, 113);


		tree2Label = new Tree(orientation);
		int t2X = treeLocation("t2X" + orientation);
		int t2Y = treeLocation("t2Y" + orientation);
		tree2Label.setBounds(t2X, t2Y, tree2Label.getTreeWd(), tree2Label.getTreeHt());
		add(tree2Label, new Integer(9));

		treeLabel = new Tree(orientation);
		int t1X = treeLocation("t1X" + orientation);
		int t1Y = treeLocation("t1Y" + orientation);
		treeLabel.setBounds(t1X, t1Y, treeLabel.getTreeWd(), treeLabel.getTreeHt());
		add(treeLabel, new Integer(9));

		bridgeLabel = new Bridge(orientation);
		add(bridgeLabel, new Integer(7));
		bridgeLabel.setBounds(bridgeLabel.getX(), bridgeLabel.getY(), bridgeLabel.getWd(), bridgeLabel.getHt());

		// for the side view of a bridge, the front part is overlaid
		// so that the 'frog' can be seen through the rails
		if (orientation.equals("E") || orientation.equals("W")) {
			BridgeFront bfLabel = new BridgeFront(orientation);
			add(bfLabel, new Integer(12));
			bfLabel.setBounds(bfLabel.getX(), bfLabel.getY(), bfLabel.getWd(), bfLabel.getHt());
		}



		// a method to add a number of cars to the road.
		addSomeCars();

		// a string label describing which level and orientation the player is on
		ViewLabel viewLbl = new ViewLabel("Road", orientation);
		add(viewLbl, new Integer(20));
		viewLbl.setBounds(20, 20, 250, 30);
	}

	/**
	 * treeLocation has the x and y coordinates for the tree images for each of the
	 * orientations. Since the origin of the image is the top left corner, these
	 * numbers must take that into account to have the tree centre or bottom of the trunk
	 * on the 'tree' tile, which the player cannot pass through.
	 *
	 * @param treeNum
	 * @return
	 */
	private int treeLocation(String treeNum) {
		int ret = 0;

		switch (treeNum) {
		case "t1XN":
			ret = 300;
			break;
		case "t1XE":
			ret = -5;
			break;
		case "t1XS":
			ret = 150;
			break;
		case "t1XW":
			ret = 490;
			break;
		case "t2XN":
			ret = 150;
			break;
		case "t2XE":
			ret = 450;
			break;
		case "t2XS":
			ret = 370;
			break;
		case "t2XW":
			ret = 23;
			break;
		case "t1YN":
			ret = 420;
			break;
		case "t1YE":
			ret = 210;
			break;
		case "t1YS":
			ret = 14;
			break;
		case "t1YW":
			ret = 28;
			break;
		case "t2YN":
			ret = 20;
			break;
		case "t2YE":
			ret = 70;
			break;
		case "t2YS":
			ret = 450;
			break;
		case "t2YW":
			ret = 180;
			break;

		default:

		}

		return ret;
	}

	/**
	 * adding logs to the River Level. The orientation determines the picture
	 * used; the log start location where the log starts, followed by its
	 * movement speed.
	 *
	 */
	private void addSomeCars() {
		carsL.add(new Car_Left_Icon(orientation, carStartLoc(1), 2));
		carsL.add(new Car_Left_Icon(orientation, carStartLoc(2), 2));
		carsL.add(new Car_Left_Icon(orientation, carStartLoc(3), 4));
		
		for (Car_Left_Icon car : carsL) {
			add(car, new Integer(5));
			car.setBounds(car.getSpriteX(), car.getSpriteY(), car.getWd(), car.getHt());
		}
		
		carsR.add(new Car_Right_Icon(orientation, carStartLoc(4), 2));
		carsR.add(new Car_Right_Icon(orientation, carStartLoc(5), 2));
		carsR.add(new Car_Right_Icon(orientation, carStartLoc(6), 4));
		
		for (Car_Right_Icon car : carsR) {
			add(car, new Integer(5));
			car.setBounds(car.getSpriteX(), car.getSpriteY(), car.getWd(), car.getHt());
		}
	}

	/**
	 * carStartLoc determines the start location for the cars, so
	 * that they are offset to start with.
	 *
	 * @param carNum
	 * @return
	 */
	private Point carStartLoc(int carNum) {
		int startL = 340;
		int startR = 200;
		Point carLoc = new Point(0, 0);
		switch (carNum) {
		case 1:
			carLoc.setLocation(10, startL);
			break;
		case 2:
			carLoc.setLocation(450, startL);
			break;
		case 3:
			carLoc.setLocation(180, startL);
			break;
		case 4:
			carLoc.setLocation(70, startR);
			break;
		case 5:
			carLoc.setLocation(500, startR);
			break;
		case 6:
			carLoc.setLocation(40, startR);
			break;

		default:
		}

		return carLoc;
	}

	public Frog getFrog(){
		return frogLabel;
	}


	public List<Car_Left_Icon> getLeftCars() {
		return carsL;
	}
	
	public List<Car_Right_Icon> getRightCars() {
		return carsR;
	}


	/**
	 * Gets a List of tiles that are designated river tiles from the background.
	 *
	 * @return
	 */
	public List<TilePane> getRoadFromBG(){
		return roadBG.getRoadTiles();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}