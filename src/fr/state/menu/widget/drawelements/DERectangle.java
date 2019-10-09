package fr.state.menu.widget.drawelements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import fr.logger.Logger;
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DERectangle implements DrawElement {

	private boolean lock;

	private Point pos;

	private Point halfSize;

	private Point size;
	private Color color;

	private BorderData border;

	private int roundBorder;

	private TextData label;

	/**
	 *
	 */
	public DERectangle() {
		super();
		this.pos = new Point();
		this.size = new Point();
		this.color = Color.black;
		this.border = null;
		this.label = null;
		this.roundBorder = 0;
	}

	public DERectangle(DERectangle other) {
		this();
		if (other == null)
			return;
		this.setPos(new Point(other.pos));
		this.setHalfSize(new Point(other.halfSize));
		this.setSize(new Point(other.size));
		this.setColor(
				new Color(other.color.getRed(), other.color.getGreen(), other.color.getBlue(), other.color.getAlpha()));
		this.border = other.border == null ? null : other.border.clone();
		this.label = other.label == null ? null : other.label.clone();
		this.lock = false;
		this.roundBorder = other.roundBorder;
	}

	/**
	 * @param pos
	 * @param size
	 * @param color
	 */
	public DERectangle(Point pos, Point size, Color color, BorderData border, TextData label) {
		this();
		this.setPos(pos);
		this.setSize(size);
		this.setColor(color);
		this.setBorder(border);
		this.setLabel(label);
	}

	@Override
	public DrawElement clone() {
		return new DERectangle(this);
	}

	@Override
	public void draw(Graphics2D g, Point ref) {

		Point absp = ref.clone().add(this.pos);

		if (this.color != null) {
			g.setColor(this.color);
		}

		if (this.roundBorder == 0) {
			g.fillRect(absp.ix(), absp.iy(), this.size.ix(), this.size.iy());
		} else {
			g.fill(new RoundRectangle2D.Double(absp.ix(), absp.iy(), this.size.ix(), this.size.iy(), this.roundBorder,
					this.roundBorder));
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
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
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

	public int getRoundBorder() {
		return this.roundBorder;
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
	 * @param border the border to set</br>
	 *               border.state = 0 : inner</br>
	 *               border.state = 1 : outter
	 */
	public void setBorder(BorderData border) {
		if (border != null) {
			this.border = border.clone();
			this.border.lock();
		}
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	private void setHalfSize(Point halfSize) {
		this.halfSize = halfSize;
	}

	/**
	 * @param label the label to set</br>
	 *              label.state = 0 : x and y relative</br>
	 *              label.state = 1 : x center / y relative</br>
	 *              label.state = 2 : x relative / y </br>
	 *              label.state = 3 : x and y center
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

	public void setRoundBorder(int roundBorder) {
		assert roundBorder >= 0;
		this.roundBorder = roundBorder * 2;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		this.size = size;
		this.halfSize = size.clone().div(2);
	}
}
