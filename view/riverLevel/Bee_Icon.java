package view.riverLevel;

import java.awt.Dimension;

import javax.swing.ImageIcon;

import model.game.Point;
import view.levels.GameSprite;

public class Bee_Icon extends GameSprite {
	private ImageIcon bee;

	Dimension beeDim;

	public Bee_Icon(String orientation) {
		animationLoc = new Point(-10, 90);
		this.orientation = orientation;

		if (orientation.equals("N")) {
			bee = new ImageIcon("src/images/bee.png");
			spriteWd = 64;
		} else if (orientation.equals("E")) {
			bee = new ImageIcon("src/images/bee_east.png");
			spriteWd = 86;
		}
		else if (orientation.equals("S")) {
			bee = new ImageIcon("src/images/bee_south.png");
			spriteWd = 64;
		}
		else if (orientation.equals("W")) {
			bee = new ImageIcon("src/images/bee_west.png");
			spriteWd = 86;
		}
		spriteHt = 64;
		setIcon(bee);
		beeDim = new Dimension(spriteWd, spriteHt);
		transposeAnimation();

	}

	@Override
	public Dimension getPreferredSize() {
		return beeDim;
	}

	@Override
	public Dimension getMaximumSize() {
		return beeDim;
	}


}
