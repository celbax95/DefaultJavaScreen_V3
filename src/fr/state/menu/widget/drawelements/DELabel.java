package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;

import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DELabel implements DrawElement {

	private Point pos;

	private Point size;

	private TextData label;

	/**
	 * @param pos
	 * @param size
	 * @param label
	 */
	public DELabel() {
		super();
		this.pos = new Point();
		this.size = new Point();
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
		this.size = size;
		this.label = label;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(this.label.getColor());
		g.setFont(this.label.getFont());

		g.drawString(this.label.getText(), this.pos.ix() + this.label.getPos().ix(),
				this.pos.iy() + this.label.getPos().iy());
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
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.label.getSize();
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(TextData label) {
		this.label = label;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}
}
