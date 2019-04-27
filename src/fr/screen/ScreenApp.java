package fr.screen;

import java.awt.Color;
import java.awt.Graphics2D;

public interface ScreenApp {
	public void appLoop(Graphics2D g, double time);

	public Color getBackgroundColor();

	public String getScreenTitle();

	public ScreenApp init(int scrWidth, int scrHeight);
}
