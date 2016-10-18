package view.riverLevel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.game.Location;
import model.game.Location.Type;
import view.levels.TilePane;

/**
 * RiverBackground is a class that provides the tile images for the River Level
 * background. It has an 18 x 18 grid of 38x38 px tiles. The right image for
 * each tile is driven by the information stored in the xml file.
 *
 * @author leonarglen
 *
 */
public class RiverBackground extends JPanel {

	private List<TilePane> riverTiles;

	private Location[][] places;

	private boolean flip = true;

	private int count = 0;

	private String orientation;

	public RiverBackground(String orientation, Location[][] locationArray) {
		this.orientation = orientation;
		riverTiles = new ArrayList<TilePane>();

		setBackground(Color.black);
		setLayout(new GridLayout(18, 18));

		// Create a TilePane for each grid location, and
		// add it to the JPanel
		Location ret = null;
		for (int i = 0; i < locationArray.length; i++) {
			for (int j = 0; j < locationArray[i].length; j++) {
				ret = locationArray[i][j];

				TilePane tile = new TilePane(ret, this);
				if (ret.getType().equals(Type.WATER)) {
					riverTiles.add(tile);
				}
				add(tile, new Integer(0));
			}
		}
	}


	public List<TilePane> getRiverTiles() {
		return riverTiles;
	}

	public String getOrientation() {
		return orientation;
	}

	/**
	 * flip is used for the river tiles to flip from river11.png to river12.png
	 * when getting the image to place on consecutive river tiles.
	 *
	 */
	public void flipFlip() {
		flip = !flip;
	}

	public boolean getFlip() {
		return flip;
	}

	public int getCount() {
		return count;
	}

	public void incrCount() {
		count++;
	}

	public void resetCount() {
		count = 0;
	}

	/**
	 * Load the places (data for the board tiles) from a text file and convert
	 * them into Location objects
	 *
	 */
	private void loadPlaces(String orientation) {
		// places.clear();
		File isFile = null;
		switch (orientation) {
		case "N":
			isFile = new File("src/files/River_North.csv");
			break;
		case "E":
			isFile = new File("src/files/River_East.csv");
			break;
		case "S":
			isFile = new File("src/files/River_South.csv");
			break;
		case "W":
			isFile = new File("src/files/River_West.csv");
			break;
		default:

		}
		BufferedReader data;
		try {
			data = new BufferedReader(new FileReader(isFile));
			String line = data.readLine();
			while (line != null) {
				String[] values = line.split(",");
				int x = Integer.parseInt(values[0]);
				int y = Integer.parseInt(values[1]);
				String tileStr = values[2];
				Location loc = new Location(x, y, tileStr);
				// add the Locations to a List that makes
				// a graph of the board setup
				// places.add(loc);

				line = data.readLine();
			}
			data.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
