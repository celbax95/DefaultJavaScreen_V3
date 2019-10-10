package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;

import fr.logger.Logger;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.data.TextData;
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

	public DELabel(DELabel other) {
		this();
		if (other == null)
			return;
		this.setPos(new Point(other.pos));
		this.setLabel(other.label == null ? null : other.label.clone());
		this.lock = false;
	}

	/**
	 * @param pos
	 * @param size
	 * @param label
	 */
	public DELabel(Point pos, TextData label) {
		this();
		this.pos = pos;
		this.label = label;
	}

	@Override
	public DrawElement clone() {
		return new DELabel(this);
	}

	@Override
	public void draw(Graphics2D g, Point ref) {
		// Absolute Point
		Point absp = ref.clone().add(this.pos);

		this.label.draw(g, absp, null);
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
		this.label.lock();
	}

	private void lockWarn() {
		Logger.warn("Les paramêtres de n'ont pas été changés car l'objet est verouillé");
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(TextData label) {
		if (label != null) {
			if (this.lock) {
				this.lockWarn();
				return;
			}
			this.label = label.clone();
			this.label.lock();
		}
	}

	/**
	 * @param pos the pos to set
	 */
	@Override
	public void setPos(Point pos) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		this.pos = pos;
	}
}
