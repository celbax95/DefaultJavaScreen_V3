package fr.screen;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.util.time.Timer;

@SuppressWarnings("serial")
public class Root extends JPanel {

	private static ScreenApp scrApp;

	private static Root single;

	private static int WIDTH, HEIGHT;

	private Timer t;

	private Root(ScreenApp scrApp, int w, int h, int m) {
		super();
		WIDTH = w;
		HEIGHT = h;
		this.setLocation(m, m);
		this.setSize(w, h);
		Root.scrApp = scrApp;
		if (scrApp != null)
			scrApp.init(w, h);

		Thread repainter = new Thread(new Repainter(this));
		repainter.start();

		t = new Timer();
	}

	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;
		this.setBackground(scrApp.getBackgroundColor());

		if (scrApp != null)
			scrApp.appLoop(g, t.lastTick());

		t.tick();
	}

	protected static void addScreenApp(ScreenApp scrApp) {
		Root.scrApp = scrApp;
		scrApp.init(WIDTH, HEIGHT);
	}

	public static Root create(ScreenApp scrApp, int w, int h, int m) {
		if (single == null) {
			single = new Root(scrApp, w, h, m);
			return single;
		} else
			return null;
	}
}
