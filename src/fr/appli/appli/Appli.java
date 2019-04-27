package fr.appli.appli;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import fr.appli.scitem.objet.Objet;
import fr.screen.ScreenApp;
import fr.util.pointd.PointD;

public class Appli implements ScreenApp {
	private static Color backgroundColor = new Color(234, 234, 234);

	private List<SCItem> lsci = new LinkedList<SCItem>();
	private Point pos, size;

	private double time;

	@Override
	public void appLoop(Graphics2D g, double time) {
		this.time = time;
		for (SCItem i : lsci) {
			i.draw(g);
		}
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public String getScreenTitle() {
		return "App";
	}

	@Override
	public ScreenApp init(int scrWidth, int scrHeight) {
		this.pos = new Point(0, 0);
		this.size = new Point(scrWidth, scrHeight);
		lsci.add(new Objet(new PointD(50, 50), new PointD(50, 50), 200, this));

		for (SCItem i : lsci) {
			i.start();
		}
		return this;
	}

	public double time() {
		return time;
	}

}
