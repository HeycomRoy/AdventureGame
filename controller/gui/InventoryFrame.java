package controller.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.character.Player;
import model.game.Game;
import model.items.Item;
import network.Slave;

/**
 * Displays the Inventory of the character and allows the user to select their items and use/drop them.
 * @Author Liam Reeves
 */

public class InventoryFrame extends JPanel implements MouseListener{
	
	private static final int size = 400/3;
	public int mouseX;
	public int mouseY;
	public Player player;
	public Item selectedItem;
	public List<Item> inventory;
	
	public InventoryFrame(){
		
		addMouseListener(new MouseAdapter() { 
	          public void mousePressed(MouseEvent e) { 
	           mouseX = e.getX();
	       	   mouseY = e.getY();
	       	   paint(getGraphics());
	          } 
	        });
	}
	
	public InventoryFrame(Game game, Player player){
		
		addMouseListener(new MouseAdapter() { 
	          public void mousePressed(MouseEvent e) { 
	           mouseX = e.getX();
	       	   mouseY = e.getY();
	       	   paint(getGraphics());
	          } 
	        });
		
		this.player = player;
	}

	public void paint(Graphics g){
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,401,101);
		
		//inventory = player.getInventory();
		int count = 0;
		
		for(int x = 0; x < 3; x++){
			int y = 0;
				Rectangle r = new Rectangle(x*size + 1, y*size + 1, size - 1, size - 1);
				
				if(r.contains(mouseX, mouseY)){ //Checks to see if item slot has been selected by user
					g.setColor(Color.YELLOW);
					
		           // selectedItem = inventory.get(count);
				}
				else{
				g.setColor(Color.WHITE);
				}
				g.fillRect(x*size + 1, y*size + 1, size - 1, 100 - 1);
//				if(inventory.get(count) != null){
//					try {
//						g.drawImage(ImageIO.read(new File ("src/images/" + inventory.get(count).toString() + "_inv.png")), x*size + 2, y* size + 2, size - 3, 100 - 3, null);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
				count++;
			}
			
		}
		
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
        mouseY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
