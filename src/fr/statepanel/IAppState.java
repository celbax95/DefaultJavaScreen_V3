package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics2D;

public interface IAppState {
	void draw(Graphics2D g) throws StateRequest;

	Color getBackgroundColor();

	String getName();

	int getRepaintRate();

	void setActive(boolean active);

	void setRepainter(Repainter repainter);
}