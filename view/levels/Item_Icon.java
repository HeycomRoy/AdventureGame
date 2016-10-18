package view.levels;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Item_Icon extends JLabel {
	private Dimension itemDim = new Dimension(38,38);
	private String itemName;	
	private int itemX;
	private int itemY;
	
	
	public Item_Icon(String itemName, int itemX, int itemY){
		this.itemName = itemName;
		this.itemX = itemX;
		this.itemY = itemY;
		String filePath = "src/images/" + itemName + ".png";
		ImageIcon itemImage = new ImageIcon(filePath);
		setIcon(itemImage);
		
	}
	
	public int getItemX(){
		return itemX;
	}
	
	public int getItemY(){
		return itemY;
	}
	
	
	
	

	
	

}
