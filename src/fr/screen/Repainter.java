package fr.screen;

import javax.swing.JPanel;

public class Repainter implements Runnable {

	private static final int PAUSE = 10;

	private JPanel jp;

	public Repainter(JPanel jp) {
		super();
		this.jp = jp;
	}

	public void run() {
		while (true) {
			jp.repaint();
			try {
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
			}
		}
	}
}
