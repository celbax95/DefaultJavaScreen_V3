package fr.state.menu;

import java.awt.Graphics2D;

import fr.inputs.Input;

public interface MenuPage {

	void draw(Graphics2D g);

	boolean isLoaded();

	void load();

	void update(Input input);
}
