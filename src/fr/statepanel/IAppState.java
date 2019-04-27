package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics2D;

public interface IAppState {
	public void draw(Graphics2D g) throws StateRequest;

	public Color getBackgroundColor();

	public String getName();

	public int getRepaintRate();

	public void setActive(boolean active);

	public void setRepainter(Repainter repainter);
}