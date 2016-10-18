package view.renderTesting;

import java.awt.Dimension;

/**
 * RenderPane is the super-class for the rendered pane on the Application
 * Window. Common fields for the size, and methods like rotate, are held here.
 *
 *@author leonarglen
 *
 */
import javax.swing.JLayeredPane;

import controller.gui.GameFrame;
import model.game.Level;
import model.game.Location;
import model.game.Point;
import model.items.Item;
import view.levels.Item_Icon;
import view.renderTesting.TestFrame;
import view.riverLevel.Bee_Icon;

public abstract class RenderPane extends JLayeredPane {
	// The orientation of the visible pane N, E, S, W
	protected String orientation;
	protected int paneWidth = 684;
	protected int paneHeight = 684;
	protected Dimension paneDim;
	protected Location[][] locationArray;
	protected TestFrame parentFrame;
	protected Level currentLevel;
	protected Bee_Icon beeLabel;

	@Override
	public Dimension getPreferredSize() {
		return paneDim;
	}

	@Override
	public Dimension getMaximumSize() {
		return paneDim;
	}

	/**
	 * When the left and right rotate buttons are clicked, this method picks the
	 * next appropriate orientation to move to.
	 *
	 */
	public void rotate(String dir) {
		if (dir.equals("Left")) {
			switch (orientation) {
			case "N":
				this.orientation = "E";
				break;
			case "E":
				this.orientation = "S";
				break;
			case "S":
				this.orientation = "W";
				break;
			case "W":
				this.orientation = "N";
				break;
			default:
				this.orientation = "N";
			}

		} else if (dir.equals("Right")) {
			switch (orientation) {
			case "N":
				this.orientation = "W";
				break;
			case "W":
				this.orientation = "S";
				break;
			case "S":
				this.orientation = "E";
				break;
			case "E":
				this.orientation = "N";
				break;
			default:
				this.orientation = "N";
			}
		}
		// Rotate the array of tile locations to suit the orientation
		this.locationArray = getRotated();
	}



	/**
	 * Method used in each level to update the things on it according to the
	 * instructions passed from the game
	 *
	 * @param locationArray
	 */

	public void updatePane(Location[][] locationArray) {

		this.locationArray = locationArray;

		
		// Get the Items from the game and display them
		int itemY = 100;// item.getLocation().getY();

		for (Item item : currentLevel.getItems()) {
			String itemName = item.getDescription();
			int itemX = 590;// item.getLocation().getX();

			Item_Icon itemIcon = new Item_Icon(itemName, itemX, itemY);

			add(itemIcon, new Integer(5));
			itemIcon.setBounds(itemX, itemY, 60, 60);
			itemY = itemY + 50;
		}

	}
	
	/**
	 * transposeMC transposes the MovingCharacters on the RenderPane
	 * so they are in the right position when the pane is rotated
	 * 
	 * @param mcPoint
	 * @return
	 */
	protected Point transposeMC(Point mcPoint){
		
		int mcX = mcPoint.getX();
		int mcY = mcPoint.getY();
		
		int rMCX = mcX;
		int rMCY = mcY;

		switch (orientation) {

		case "E":
			rMCX = mcY;
			rMCY = 17 - mcX;

			break;
		case "S":
			rMCX = 17 - mcX;
			rMCY = 17 - mcY;

			break;
		case "W":
			rMCX = 17 - mcY;
			rMCY = mcX;
			break;
		default:

		}
		return new Point(rMCX, rMCY);
	
	}
	
	/**
	 * Set the Bee location according to the position from the
	 * game and the rotation
	 * 
	 * @param beeX
	 * @param beeY
	 */
	protected void setBeeLocation(int beeX, int beeY) {

		int rbX = beeX;
		int rbY = beeY;

		switch (orientation) {

		case "E":
			rbX = beeY;
			rbY = 684 - beeX;

			break;
		case "S":
			rbX = 684 - beeX;
			rbY = 684 - beeY - beeLabel.getHt();

			break;
		case "W":
			rbX = 684 - beeY - beeLabel.getWd();
			rbY = beeX;
			break;
		default:

		}

		beeLabel.setLocation(rbX, rbY);

	}
	
	

	/**
	 * The supplied array is for the North-facing direction; Rotate according to
	 * the orientation
	 *
	 * @param locationArray
	 * @return
	 */
	private Location[][] getRotated() {

		Location[][] ret = locationArray;
		switch (orientation) {
		case "E":
			ret = rotateLeft(ret);
			break;
		case "S":
			ret = rotateRight(ret);
			ret = rotateRight(ret);
			break;
		case "W":
			ret = rotateRight(ret);
			break;
		default:
		}

		return ret;
	}

	/**
	 * Rotate the array 90 degrees clockwise
	 *
	 * @param locationArray
	 * @return
	 */
	public Location[][] rotateRight(Location[][] locationArray) {
		int w = locationArray.length;
		int h = locationArray[0].length;
		Location[][] ret = new Location[h][w];
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w; ++j) {
				ret[i][j] = locationArray[w - j - 1][i];
			}
		}
		return ret;
	}

	/**
	 * Rotate the array 90 degrees counter-clockwise
	 *
	 * @param locationArray
	 * @return
	 */
	public Location[][] rotateLeft(Location[][] locationArray) {

		int w = locationArray.length;
		int h = locationArray[0].length;
		Location[][] ret = new Location[h][w];
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w; ++j) {
				ret[i][j] = locationArray[j][h - i - 1];
			}
		}
		return ret;
	}

}
