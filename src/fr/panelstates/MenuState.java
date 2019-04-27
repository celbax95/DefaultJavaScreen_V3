package fr.panelstates;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.statepanel.AppState;
import fr.statepanel.StateRequest;

public class MenuState extends AppState {

	public MenuState() {
		super();
	}

	@Override
	public void draw(Graphics2D g) throws StateRequest {
		g.setBackground(Color.black);
	}

	@Override
	public Color getBackgroundColor() {
		return Color.blue;
	}

	@Override
	public String getName() {
		return "Menu";
	}

	@Override
	public int getRepaintRate() {
		return 50;
	}

	@Override
	public void setActive(boolean active) throws RuntimeException {
		super.setActive(active);
	}
}
