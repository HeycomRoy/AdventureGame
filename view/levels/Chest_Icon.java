package view.levels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import view.levels.GameSprite;

public class Chest_Icon extends JLabel {
	private ImageIcon chest;
	private Point chestLoc;


	public Chest_Icon(boolean closed) {
		chestLoc = new Point(10, 342);

		Dimension chestDim = new Dimension(60,60);

		if(closed){

			chest = new ImageIcon("src/images/chest_closed.png");

		} else chest = new ImageIcon("src/images/chest.png");


		setIcon(chest);
		setSize(60, 60);

	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

	}
}