package fr.panelstates;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.statepanel.AppState;
import fr.statepanel.StateRequest;

public class MenuState extends AppState {

	private Color backgroundColor;

	private int ok;

	public MenuState() {
		super();
		this.backgroundColor = Color.PINK;
	}

	@Override
	public void draw(Graphics2D g) throws StateRequest {
	}

	@Override
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public String getName() {
		return "Menu";
	}

	@Override
	public int getRepaintRate() {
		return -1;
	}
}
