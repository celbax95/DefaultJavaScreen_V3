package fr.statepanel;

import javax.swing.JPanel;

public interface Repainter extends Runnable {
	void interrupt();

	void repaint();

	void setPanel(JPanel panel);

	void setRate(int rate);

	void start();
}
