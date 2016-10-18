package view.levels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * ViewLabel provides a JLabel with a string describing the view that the
 * player is currently in. It is called in the class for rendering a level
 * of the game, e.g. 'RiverPane'
 * 
 * 
 * @author Glen
 *
 */
public class ViewLabel extends JLabel{
	private String orientation;
	private String level;

	public ViewLabel(String level, String orientation){
		this.orientation = orientation;
		this.level = level;
		Font viewFont = new Font("Lucida Sans", Font.PLAIN, 18);
		setForeground(Color.white);
		setFont(viewFont);
		setText(words());
		setSize(250,30);

	}

	private String words(){
		String orient = "";
		switch(orientation){
		case "N":
			orient = "North";
			break;
		case "E":
			orient = "East";
			break;
		case "S":
			orient = "South";
			break;
		case "W":
			orient = "West";
			break;
			default:
		}

		return "On " + level + " Level, facing " + orient;
	}

}
