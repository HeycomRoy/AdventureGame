package view.riverLevel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BridgeFront extends JLabel {
	private ImageIcon bridge;
	private int x;
	private int y;
	private int bridgeHt;
	private int bridgeWd;

	public BridgeFront(String orientation) {
		
		if (orientation.equals("E")) {
			bridge = new ImageIcon("src/images/bridge_side1.png");
			bridgeHt = 74;
			bridgeWd = 260;
			x = 215;
			y = 552;
		
					
		} else if (orientation.equals("W")) {
			bridge = new ImageIcon("src/images/bridge_side1.png");
			bridgeHt = 74;
			bridgeWd = 260;
			x = 215;
			y = 52;
		}
		
		setSize(bridgeWd, bridgeHt);
		setIcon(bridge);
				
}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getHt(){
		return bridgeHt;
	}
	
	public int getWd(){
		return bridgeWd;
	}
}
