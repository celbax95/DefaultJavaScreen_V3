package fr.menu;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Page du menu
 *
 * @author Loic.MACE
 *
 */
public class Page {

	public List<Widget> widgets;

	public void addWidget(Widget widget) {
		this.widgets.add(widget);
	}

	public void draw(Graphics2D g) {
		for (Widget widget : this.widgets) {
			widget.draw(g);
		}
	}

}
