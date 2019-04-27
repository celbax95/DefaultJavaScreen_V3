package fr.statepanel;

public interface Repainter extends Runnable {
	void interrupt();

	void repaint();

	void setRate(int rate);

	void start();
}
