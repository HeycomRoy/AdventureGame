package controller.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.character.Player;
import model.game.Game;
import model.game.Location;

/**
 * Creates a real world minimap that displays items, players, rooms and treasures in the game.
 * @author Liam Reeves
 */

public class MiniMap extends JPanel {

	private static final int size = 8;
	private static Color GRASS = Color.GREEN;
	private static Color WATER = Color.BLUE;
	private static Color BRIDGE = new Color(255,255,0);
	private static Color TREE = new Color(0,100,0);
	private static Color STONE = Color.gray;
	private static Color MOUNTAIN = new Color(255,255,50);
	private static Color ITEM = Color.YELLOW;
	private static Color ROAD = new Color(225,255,255);
	private static Color ROADSTRIPE = new Color(2,2,0);
	private static Color STEP = new Color(255,225,0);

	public Game game;
	public Player player;

	public MiniMap(Game game, Player player){
		this.game = game;
		this.player = player;

	}


	public void paint(Graphics g){

		g.setColor(Color.BLACK);
		g.fillRect(0,0, 400,400);

		//if(player.hasMap()){

		for(int z = 0; z < game.worlds.size(); z++){
		for(int x = 0; x < 18; x++){
			for(int y = 0; y < 18; y++){

				Location loc = game.worlds.get(z).getLoc(x,y);

				System.out.println(loc.getType());

				switch (loc.getType()){
				case GRASS: g.setColor(GRASS); break;
				case TREE: g.setColor(TREE); break;
				case WATER: g.setColor(WATER); break;
				case BRIDGE: g.setColor(BRIDGE); break;
				case STONE: g.setColor(STONE); break;
		    	case MOUNTAIN: g.setColor(MOUNTAIN); break;
		    	case ROAD: g.setColor(ROAD); break;
		    	case ROADSTRIPE: g.setColor(ROADSTRIPE); break;
		    	case STEP: g.setColor(STEP); break;
		    	default: g.setColor(Color.WHITE); break;
				}

				if(loc.getItem() != null){
					g.setColor(ITEM);
				}


				g.fillRect(x*size + 41, y*size + 41, size - 1, size - 1);

				}
		}
			}
		//}

	}

}
