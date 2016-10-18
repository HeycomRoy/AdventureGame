package view.roadLevel;

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
 * RoadBackground is a class that provides the tile images for the Road Level
 * background. It has an 18 x 18 grid of 38x38 px tiles. The right image for
 * each tile is taken from a csv file made for each orientation (N, E, S, W)
 *
 * @author leonarglen
 *
 */
public class RoadBackground extends JPanel {

	private List<TilePane> roadTiles;

	private Location[][] places;

	private int count = 0;

	private String orientation;

	public RoadBackground(String orientation, Location[][] locationArray) {
		this.orientation = orientation;
		roadTiles = new ArrayList<TilePane>();
		places = locationArray;
		// loadPlaces(orientation);

		setBackground(Color.black);
		setLayout(new GridLayout(18, 18));

		// Create a TilePane for each grid location, and
		// add it to the JPanel
		Location ret = null;
		for (int i = 0; i < places.length; i++) {
			for (int j = 0; j < places[i].length; j++) {
				ret = places[i][j];

				TilePane tile = new TilePane(ret, this);
				if (ret.getType().equals(Type.ROAD)) {
					roadTiles.add(tile);
				}
				add(tile, new Integer(0));
			}
		}

		// for (int i = 0; i < places.length; i++) {
		// for (int j = 0; j < places[i].length; j++) {
		// ret = places[i][j];
		//
		// System.out.print(ret.getType() + "|");
		//
		// }
		// System.out.println("");
		// }

	}

	/**
	 * The supplied array is for the North-facing direction Rotate according to
	 * the orientation
	 * 
	 * @param locationArray
	 * @return
	 */
	private Location[][] getRotated(Location[][] locationArray) {

		Location[][] ret = locationArray;
		switch (orientation) {
		case "E":
			ret = rotateLeft(locationArray);
			break;
		case "S":
			ret = rotateRight(locationArray);
			ret = rotateRight(ret);
			break;
		case "W":
			ret = rotateRight(locationArray);
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
		/* W and H are already swapped */
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

	public List<TilePane> getRoadTiles() {
		return roadTiles;
	}

	public String getOrientation() {
		return orientation;
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
			isFile = new File("src/files/Road_North.csv");
			break;
		case "E":
			isFile = new File("src/files/Road_East.csv");
			break;
		case "S":
			isFile = new File("src/files/Road_South.csv");
			break;
		case "W":
			isFile = new File("src/files/Road_West.csv");
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
