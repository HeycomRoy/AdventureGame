package view.levels;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tree extends JLabel {
	private ImageIcon tree;
		
	private int treeHt;
	private int treeWd;

	public Tree(String orientation) {
		if (orientation.equals("N") || orientation.equals("S")) {
			tree = new ImageIcon("src/images/top_tree.png");
			treeHt = 232;
			treeWd = 200;

		} else if (orientation.equals("E") || orientation.equals("W")) {
			tree = new ImageIcon("src/images/side_tree.png");
			treeHt = 218;
			treeWd = 200;
		}
		setSize(treeWd, treeHt);
		setIcon(tree);
	}

	public int getTreeHt() {
		return treeHt;
	}

	public int getTreeWd() {
		return treeWd;
	}
	
	
}
