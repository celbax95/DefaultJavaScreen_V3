package fr.state.menu.widget;

import java.awt.Color;

public class BorderData {

	private int thickness;

	private Color color;

	private int state;

	public BorderData() {
		this.thickness = 0;
		this.color = Color.BLACK;
		this.state = 0;
	}

	public BorderData(int thickness, Color color, int state) {
		super();
		this.setThickness(thickness);
		this.setColor(color);
		this.setState(state);
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * @return the size
	 */
	public int getThickness() {
		return this.thickness;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		assert color != null;
		this.color = color;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		assert state >= 0;
		this.state = state;
	}

	/**
	 * @param size the size to set
	 */
	public void setThickness(int size) {
		assert size >= 0;
		this.thickness = size;
	}
}
