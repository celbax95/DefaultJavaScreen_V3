package fr.state.menu;

import java.awt.Graphics2D;

import fr.inputs.Input;

public interface Widget {

	void draw(Graphics2D g);

	void update(Input input);
}
