package fr.state.menu.widget;

import java.awt.Color;

public class BorderData {

	private int thickness;

	private Color color;

	private int state;

	private boolean lock;

	public BorderData() {
		super();
		this.thickness = 0;
		this.color = Color.BLACK;
		this.state = 0;
	}

	public BorderData(BorderData other) {
		this();
		if (other == null)
			return;
		this.setThickness(other.thickness);
		this.setColor(new Color(other.color.getRed(), other.color.getGreen(), other.color.getBlue(),
				other.color.getAlpha()));
		this.setState(other.state);
		this.lock = false;
	}

	public BorderData(int thickness, Color color, int state) {
		this();
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

	public boolean isLocked() {
		return this.lock;
	}

	public void lock() {
		this.lock = true;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		if (this.lock)
			return;
		assert color != null;
		this.color = color;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		if (this.lock)
			return;
		assert state >= 0;
		this.state = state;
	}

	/**
	 * @param size the size to set
	 */
	public void setThickness(int size) {
		if (this.lock)
			return;
		assert size >= 0;
		this.thickness = size;
	}
}
