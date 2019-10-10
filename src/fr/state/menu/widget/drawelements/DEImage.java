package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;
import java.awt.Image;

import fr.logger.Logger;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.data.TextData;
import fr.util.point.Point;

public class DEImage implements DrawElement {

	private boolean lock;

	private Point pos;
	private Point size;

	private BorderData border;

	private TextData label;

	private Image image;

	public DEImage() {
		super();
		this.pos = new Point();
		this.size = new Point();

		this.border = null;

		this.label = null;

		this.image = null;
	}

	public DEImage(DEImage other) {
		this();
		if (other == null)
			return;
		this.lock = false;
		this.setPos(new Point(other.pos));
		this.setSize(new Point(other.size));
		this.border = other.border == null ? null : other.border.clone();
		this.label = other.label == null ? null : other.label.clone();
		this.setImage(other.image);
	}

	/**
	 * @param pos
	 * @param size
	 * @param border
	 * @param label
	 * @param image
	 */
	public DEImage(Point pos, Image image, BorderData border, TextData label) {
		this();
		this.setPos(pos);
		this.setImage(image);
		this.setBorder(border);
		this.setLabel(label);
	}

	@Override
	public DrawElement clone() {
		return new DEImage(this);
	}

	@Override
	public void draw(Graphics2D g, Point ref) {
		// Absolute Point
		Point absp = ref.clone().add(this.pos);

		if (this.image != null) {
			g.drawImage(this.image, absp.ix(), absp.iy(), null);
		}

		if (this.border != null) {
			this.border.draw(g, absp, this.size);
		}

		if (this.label != null) {
			this.label.draw(g, absp, this.size);
		}
	}

	/**
	 * @return the border
	 */
	public BorderData getBorder() {
		return this.border;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return this.image;
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
		return this.size;
	}

	@Override
	public boolean isLocked() {
		return this.lock;
	}

	@Override
	public void lock() {
		this.lock = true;
		if (this.border != null) {
			this.border.lock();
		}
		if (this.label != null) {
			this.label.lock();
		}
	}

	private void lockWarn() {
		Logger.warn("Les paramêtres de n'ont pas été changés car l'objet est verouillé");
	}

	/**
	 * @param border the border to set
	 */
	public void setBorder(BorderData border) {
		if (border != null) {
			this.border = border.clone();
			this.border.lock();
		}
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert image != null;
		this.image = image;
		this.size.set(image.getWidth(null), image.getHeight(null));
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(TextData label) {
		if (label != null) {
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

	private void setSize(Point size) {
		this.size = size;
	}
}
