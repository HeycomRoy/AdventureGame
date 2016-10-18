package view.renderTesting;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import view.levels.Frog;
import view.levels.GameSprite;
import view.levels.TilePane;
import view.levels.Tree;
import view.levels.ViewLabel;
import view.renderTesting.RenderPane;
import view.riverLevel.Alligator_Icon;
import view.riverLevel.Bee_Icon;
import view.riverLevel.Bridge;
import view.riverLevel.BridgeFront;
import view.riverLevel.Log_Icon;
import view.riverLevel.RiverBackground;
import model.character.MovingCharacter;
import model.character.Player;
import model.game.Location;
import model.game.Point;

/**
 * RiverPane is the parent JLayeredPane for the River Level. It holds the river
 * background and various components of the level.
 *
 * @author leonarglen
 *
 */

public class RiverPanel extends RenderPane implements KeyListener {

	private RiverBackground riverBG;
	private Bridge bridgeLabel;

	private Tree treeLabel;
	private Tree tree2Label;
	private Frog frogLabel;
	
	private Alligator_Icon alligatorLabel;
	private List<Log_Icon> logs = new ArrayList<Log_Icon>();

	public RiverPanel(TestFrame parent, Location[][] locationArray) {

		orientation = "N";
		// obtains the array of locations (things on the game grid) 
		// from the game
		// the variable is held in the super class RenderPane
		this.locationArray = locationArray;

		this.parentFrame = parent;

		this.currentLevel = parentFrame.getCurrentLevel();

		// add the game items to the level
		addItemsToLevel();

		addKeyListener(this);

		setFocusable(true);
		requestFocusInWindow();

	}

	/**
	 * When the left and right rotate buttons are clicked, this method picks the
	 * next appropriate orientation to move to. The common methods from the
	 * parent class are done first before doing these specific tasks
	 *
	 */
	public void rotate(String dir) {
		super.rotate(dir);

		logs.clear();
		this.removeAll();
		addItemsToLevel();
		validate();
		repaint();

	}

	/**
	 * updatePane is called by the parent JFrame at each clock tick so the
	 * components of the rendering are updated
	 *
	 */
	public void updatePane(Location[][] locationArray) {
		super.updatePane(locationArray);

		// logCounter is used in the River Level
		int logCounter = 0;
		// ==================== MOVING CHARACTERS ==========================
		for (Map.Entry<Integer, MovingCharacter> entry : currentLevel.getMovingCharacters().entrySet()) {

			MovingCharacter mc = entry.getValue();

			int beeX = mc.getPoint().getX();
			int beeY = mc.getPoint().getY();

			Point rotatedPoint = transposeMC(mc.getPoint());

			int mcX = rotatedPoint.getX();
			int mcY = rotatedPoint.getY();

			switch (mc.getMCID()) {
			case "A":
				alligatorLabel.moveAcrossTile(mcX, mcY);
				break;
			case "B":
				setBeeLocation(beeX, beeY);
				break;
			case "L":
				// move each Log_Icon read from the RiverPane list according to
				// the matching index of the Log in the Game

				if (logCounter >= logs.size())
					logCounter = 0;

				if (logs.size() > 0) {
					Log_Icon log = logs.get(logCounter);

					logCounter++;
					log.moveAcrossTile(mcX, mcY);
				}
				break;
			default:

			}
		}
	}
	

	
	

	

	/**
	 * addItemsToLevel method to add the relevant components to this level,
	 * including the background. Items are added to different layers so that
	 * moving sprites can pass over or under them.
	 *
	 */
	private void addItemsToLevel() {

		riverBG = new RiverBackground(orientation, locationArray);
		add(riverBG, new Integer(0));
		riverBG.setBounds(0, 0, paneWidth, paneHeight);

		frogLabel = new Frog(orientation);
		add(frogLabel, new Integer(8));
		frogLabel.setBounds(500, 450, 100, 113);

		beeLabel = new Bee_Icon(orientation);
		add(beeLabel, new Integer(10));
		beeLabel.setBounds(beeLabel.getSpriteX(), beeLabel.getSpriteY(), beeLabel.getWd(), beeLabel.getHt());

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

		alligatorLabel = new Alligator_Icon(orientation);
		add(alligatorLabel, new Integer(5));
		alligatorLabel.setBounds(alligatorLabel.getSpriteX(), alligatorLabel.getSpriteY(), alligatorLabel.getWd(),
				alligatorLabel.getHt());

		// a method to add a number of logs to the river.
		addSomeLogs();

		// a string label describing which level and orientation the player is
		// on
		ViewLabel viewLbl = new ViewLabel("River", orientation);
		add(viewLbl, new Integer(20));
		viewLbl.setBounds(20, 20, 250, 30);

	}

	/**
	 * treeLocation has the x and y coordinates for the tree images for each of
	 * the orientations. Since the origin of the image is the top left corner,
	 * these numbers must take that into account to have the tree centre or
	 * bottom of the trunk on the 'tree' tile, which the player cannot pass
	 * through.
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
	private void addSomeLogs() {
		logs.add(new Log_Icon(orientation, logStartLoc(1), 2));
		logs.add(new Log_Icon(orientation, logStartLoc(2), 2));
		logs.add(new Log_Icon(orientation, logStartLoc(3), 4));
		logs.add(new Log_Icon(orientation, logStartLoc(4), 4));
		logs.add(new Log_Icon(orientation, logStartLoc(5), 2));
		logs.add(new Log_Icon(orientation, logStartLoc(6), 3));

		for (Log_Icon log : logs) {

			add(log, new Integer(5));

			log.setBounds(log.getSpriteX(), log.getSpriteY(), log.getWd(), log.getHt());

		}

	}

	/**
	 * logStartLoc determines the start location for the logs, so that they are
	 * offset to start with.
	 *
	 * @param logNum
	 * @return
	 */
	private Point logStartLoc(int logNum) {

		int start = 228;
		Point logLoc = new Point(0, 0);
		switch (logNum) {
		case 1:
			logLoc.setLocation(10, start);
			break;
		case 2:
			logLoc.setLocation(450, start + 38);
			break;
		case 3:
			logLoc.setLocation(180, start + 76);
			break;
		case 4:
			logLoc.setLocation(70, start + 114);
			break;
		case 5:
			logLoc.setLocation(500, start + 152);
			break;
		case 6:
			logLoc.setLocation(40, start + 190);
			break;

		default:
		}

		return logLoc;
	}

	public Frog getFrog() {
		return frogLabel;
	}

	public Bee_Icon getBee() {
		return beeLabel;
	}

	public List<Log_Icon> getLogs() {
		return logs;
	}

	public Alligator_Icon getAlligator() {
		return alligatorLabel;
	}

	/**
	 * Gets a List of tiles that are designated river tiles from the background.
	 *
	 * @return
	 */
	public List<TilePane> getRiverFromBG() {
		return riverBG.getRiverTiles();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		Player player = parentFrame.getAPlayer();

		int fX = frogLabel.getX();
		int fY = frogLabel.getY();

		try {
			char key = Character.toLowerCase(e.getKeyChar());

			switch (key) {
			case 'w':
				frogLabel.setIcon("frog.png");
				frogLabel.setLocation(fX, fY - 3);
				movePlayer(player);
				break;
			case 'a':
				frogLabel.setIcon("frogL.png");
				frogLabel.setLocation(fX - 2, fY);
				movePlayer(player);
				break;
			case 's':
				frogLabel.setIcon("frogD.png");
				frogLabel.setLocation(fX, fY + 2);
				movePlayer(player);
				break;
			case 'd':
				frogLabel.setIcon("frogR.png");
				frogLabel.setLocation(fX + 2, fY);
				movePlayer(player);
				break;
			default:
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void movePlayer(Player player) {
		int gridX = (int) ((frogLabel.getX() + 25) / 38);
		int gridY = (int) ((frogLabel.getY() + 25) / 38);
		player.setLocation(new Location(gridX, gridY));

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}