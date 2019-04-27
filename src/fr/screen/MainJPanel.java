package fr.screen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainJPanel extends JPanel { // implements Statable {

	private static MainJPanel instance;

	static {
		instance = new MainJPanel();
	}

	private int WIDTH, HEIGHT;

	// private AppState state;

	private Thread repainter;

	private MainJPanel() {
		super();
		this.WIDTH = 0;
		this.HEIGHT = 0;

		this.setSize(this.WIDTH, this.HEIGHT);
	}

	private MainJPanel(int width, int height) {
		super();

		// taille du panel
		this.WIDTH = width;
		this.HEIGHT = height;

		this.setSize(this.WIDTH, this.HEIGHT);

//		this.repainter = new Thread(new Repainter(this, this.state.getRepainterRate()));
//		this.repainter.start();
	}

	public void init(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;

		this.setSize(this.WIDTH, this.HEIGHT);
	}

	/**
	 * Methode appellee par le repainter, pour actualiser l'affichage dans la
	 * fenetre
	 */
	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);

		this.setBackground(Color.WHITE);

		// this.setBackground(appScreen.getBackgroundColor());

		// this.state.draw((Graphics2D) g2);
	}

//	public void setState(AppState state) {
//		this.state = state;
//		this.repainter.setRate(state.getRepaintRate());
//	}

	public static MainJPanel getInstance() {
		return instance;
	}
}
