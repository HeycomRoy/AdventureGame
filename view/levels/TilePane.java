package view.levels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.game.Location;
import model.game.Location.Type;
import view.riverLevel.RiverBackground;



/**
 * class TilePane is used for the 38x38 px tiles on the game board
 *
 * @author Glen and Orion
 *
 */
public class TilePane extends JLabel {
	private Type tileType;
	private Dimension dim;
	private JPanel parent;
	private Location loc;

	/**
	 * Constructor for a TilePane with a Location argument
	 *
	 * @param loc
	 */

	public TilePane(Location loc, JPanel parent) {

		this.parent = parent;
		this.loc = loc;
		dim = new Dimension(38, 38);

		setMinimumSize(dim);
		setBackground(new Color(144, 188, 212));
		setOpaque(true);
		tileType = loc.getType();
		
		if (tileType != null) {
			try {
				setIcon(getTileImage());
			} catch (NullPointerException e) {
				System.out.println("File for " + tileType + " is not found");
			}
		}
		
		this.setName("" + loc.getType());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(dim);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(dim);
	}

	/**
	 * A constructor for a TilePane without a Location argument
	 */
	public TilePane() {

		Dimension dim = new Dimension(38, 38);
		setMaximumSize(dim);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		setBackground(new Color(144, 188, 212));

		setOpaque(true);

	}

	/**
	 * Get a tile image according to the Type in the Location
	 *
	 * @param loc
	 * @return
	 *
	 *
	 */
	private ImageIcon getTileImage() {
		//TO DO: ADD MORE TYPES FOR DIFFERENT TERRAINS
		switch (tileType) {
		case GRASS:
			return new ImageIcon("src/images/grass.png");
		case WATER:
			return new ImageIcon("src/images/river11.png");
		case BRIDGE:
			return new ImageIcon("src/images/bridge.png");
		case ROAD:
			return new ImageIcon("src/images/road.png");
		case ROADSTRIPE:
			return new ImageIcon("src/images/roadstripe.png");
		case STEP:
			return new ImageIcon("src/images/step.png");
		case TREE:
			return new ImageIcon("src/images/grass.png");
		case MOUNTAIN:
			if(loc.getY()%2==1){
			this.setBounds(2*loc.getX()*18,2*loc.getY()*18,76,76);
			} else {
				this.setBounds((2*loc.getX()*18)-9,(2*loc.getY()*18)-9,76,76);
			}
			return new ImageIcon("src/images/mountain.png");
		default:
			return new ImageIcon("src/images/grass.png");
		}

	}

	/**
	 * returns an image for the river tile, according to the orientation and
	 * alternating the images in sequence. Flip and count are in the parent
	 * RiverBackground.
	 *
	 * @return
	 */

	private ImageIcon riverImg() {

		ImageIcon retImg = null;
		if (parent instanceof RiverBackground) {

			RiverBackground riverBG = (RiverBackground) parent;

			if (riverBG.getOrientation().equals("N") || riverBG.getOrientation().equals("S")) {
				if (riverBG.getFlip() == true) {

					retImg = new ImageIcon("src/images/river11.png");

				} else {

					retImg = new ImageIcon("src/images/river12.png");
				}
				riverBG.flipFlip();

			} else if (riverBG.getOrientation().equals("E") || riverBG.getOrientation().equals("W")) {
				if (riverBG.getFlip() == true) {

					retImg = new ImageIcon("src/images/river13.png");

				} else {
					retImg = new ImageIcon("src/images/river14.png");
				}
				// for the E & W views to get the river tile images to line up
				if (riverBG.getCount() == 6)
					riverBG.resetCount();
				else
					riverBG.flipFlip();
				riverBG.incrCount();
			}
		}
		return retImg;
	}



	/**
	 * Get a tile image according to a string derived from the Location
	 *
	 * @param loc
	 * @return
	 */
	private ImageIcon getTileImg(Location loc) {

		switch (loc.getTileStr()) {
		case "G":
			return new ImageIcon("src/images/grass.png");
		case "R":
			return riverImg();
		case "Br":
			return new ImageIcon("src/images/bridge.png");
		case "Rd":
			return new ImageIcon("src/images/road.png");
		case "Rs":
			return new ImageIcon("src/images/roadstripe.png");
		case "Tr":
			return new ImageIcon("src/images/grass.png");
		case "St":
			return new ImageIcon("src/images/step.png");
		case "D":
			return new ImageIcon("src/images/doorstep.png");
		default:
			return new ImageIcon("src/images/grass.png");
		}

	}










	public String toString() {
		return loc.toString();
	}

}