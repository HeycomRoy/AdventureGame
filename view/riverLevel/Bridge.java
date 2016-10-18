package view.riverLevel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bridge extends JLabel {
	private ImageIcon bridge;
	private int x;
	private int y;
	private int bridgeHt;
	private int bridgeWd;

	public Bridge(String orientation) {
		if (orientation.equals("N")) {
			bridge = new ImageIcon("src/images/bridge_top.png");
			bridgeHt = 260;
			bridgeWd = 90;
			x = 49;
			y = 215;

		} else if (orientation.equals("E")) {
			bridge = new ImageIcon("src/images/bridge_side2.png");
			bridgeHt = 105;
			bridgeWd = 260;
			x = 215;
			y = 520;
		
		} else if (orientation.equals("S")) {
			bridge = new ImageIcon("src/images/bridge_top.png");
			bridgeHt = 260;
			bridgeWd = 90;
			x = 544;
			y = 215;
			
		} else if (orientation.equals("W")) {
			bridge = new ImageIcon("src/images/bridge_side2.png");
			bridgeHt = 105;
			bridgeWd = 260;
			x = 215;
			y = 20;
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
