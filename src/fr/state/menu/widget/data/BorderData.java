package fr.state.menu.widget.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.logger.Logger;
import fr.util.point.Point;

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

	@Override
	public BorderData clone() {
		return new BorderData(this);
	}

	/**
	 * state = 0 : Inner state = 1 : Outer
	 */
	public void draw(Graphics2D g, Point holderPos, Point holderSize) {

		g.setColor(this.color);

		switch (this.state) {
		case 0:
			Area a0 = new Area(new Rectangle(holderPos.ix() - 1, holderPos.iy() - 1, holderSize.ix() + 2,
					holderSize.iy() + 2));

			Area b0 = new Area(new Rectangle(holderPos.ix() + this.thickness, holderPos.iy() + this.thickness,
					holderSize.ix() - this.thickness * 2, holderSize.iy() - this.thickness * 2));

			a0.subtract(b0);

			g.fill(a0);
			break;
		case 1:
			Area a1 = new Area(new Rectangle(holderPos.ix() - this.thickness, holderPos.iy() - this.thickness,
					holderSize.ix() + this.thickness * 2, holderSize.iy() + this.thickness * 2));

			Area b1 = new Area(
					new Rectangle(holderPos.ix(), holderPos.iy(), holderSize.ix(), holderSize.iy()));

			a1.subtract(b1);

			g.fill(a1);
			break;
		}
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

	private void lockWarn() {
		Logger.warn("Les paramêtres de n'ont pas été changés car l'objet est verouillé");
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
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert state >= 0;
		this.state = state;
	}

	/**
	 * @param size the size to set
	 */
	public void setThickness(int size) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert size >= 0;
		this.thickness = size;
	}
}
