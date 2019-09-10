package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;

import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DELabel implements DrawElement {

	private Point pos;

	private boolean lock;

	private TextData label;

	/**
	 * @param pos
	 * @param size
	 * @param label
	 */
	public DELabel() {
		super();
		this.pos = new Point();
		this.label = null;
	}

	/**
	 * @param pos
	 * @param size
	 * @param label
	 */
	public DELabel(Point pos, Point size, TextData label) {
		this();
		this.pos = pos;
		this.label = label;
	}

	@Override
	public void draw(Graphics2D g, Point ref) {
		g.setColor(this.label.getColor());
		g.setFont(this.label.getFont());

		Point absTextPos = ref.clone().add(this.pos).add(this.label.getPos());

		g.drawString(this.label.getText(), absTextPos.ix(), absTextPos.iy());
	}

	/**
	 * @return the label
	 */
	public TextData getLabel() {
		return this.label;
	}

	/**
	 * @return the pos
	 */
	@Override
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the size
	 */
	@Override
	public Point getSize() {
		return this.label.getSize();
	}

	@Override
	public boolean isLocked() {
		return this.lock;
	}

	@Override
	public void lock() {
		this.lock = true;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(TextData label) {
		if (this.lock)
			return;
		this.label = label;
	}

	/**
	 * @param pos the pos to set
	 */
	@Override
	public void setPos(Point pos) {
		if (this.lock)
			return;
		this.pos = pos;
	}
}
