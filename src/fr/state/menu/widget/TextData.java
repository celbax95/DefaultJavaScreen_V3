package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import fr.logger.Logger;
import fr.util.point.Point;

public class TextData {

	private Point pos;

	private Point size;

	private Font font;

	private String text;

	private Color color;

	private int state;

	private boolean lock;

	/**
	 * @param pos
	 * @param size
	 * @param font
	 * @param text
	 * @param color
	 * @param state
	 */
	public TextData() {
		super();
		this.pos = new Point();
		this.size = new Point();
		this.font = new Font("Arial", Font.PLAIN, 15);
		this.text = "";
		this.color = Color.black;
		this.state = 0;
		this.lock = false;
	}

	/**
	 * @param pos
	 * @param size
	 * @param font
	 * @param text
	 * @param color
	 * @param state
	 */
	public TextData(Point pos, Font font, String text, Color color, int state) {
		this();
		this.setPos(pos);
		this.setFont(font);
		this.setText(text);
		this.setColor(color);
		this.setState(state);
	}

	public TextData(TextData other) {
		this();
		if (other == null)
			return;
		this.setPos(new Point(other.pos));
		this.setSize(new Point(other.size));

		this.font = new Font(other.font.getFamily(), other.font.getStyle(), other.font.getSize());
		this.setText(new String(other.text));
		this.setColor(new Color(other.color.getRed(), other.color.getGreen(), other.color.getBlue(),
				other.color.getAlpha()));
		this.state = other.state;
		this.lock = false;
	}

	@Override
	public TextData clone() {
		return new TextData(this);
	}

	/**
	 * state = 0 : Relative</br>
	 * state = 1 : X centered</br>
	 * state = 2 : Y centered</br>
	 * state = 3 : Centered
	 */
	public void draw(Graphics2D g, Point holderPos, Point holderSize) {

		Point absp = this.pos.clone();

		int state = this.state;

		if (holderPos == null || holderSize == null) {
			state = 0;
		} else if (holderPos != null) {
			absp.add(holderPos);
		}

		g.setFont(this.font);
		g.setColor(this.color);

		switch (state) {
		case 0:
			// Relative
			g.drawString(this.text, absp.ix(), absp.iy());
			break;
		case 1:
			// X centered - Y relative
			g.drawString(this.text, absp.ix() + holderSize.ix() / 2 - this.size.ix() / 2, absp.iy());
			break;
		case 2:
			// X relative - Y centered
			g.drawString(this.text, absp.ix(), absp.iy() + holderSize.iy() / 2 + holderSize.iy() / 4);
			break;
		case 3:
			// Centered
			Point holderHalfSize = holderSize.clone().div(2);
			g.drawString(this.text, absp.ix() + holderHalfSize.ix() - this.size.ix() / 2,
					absp.iy() + holderHalfSize.iy() + this.size.iy() / 4);
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
	 * @return the font
	 */
	public Font getFont() {
		return this.font;
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
		return this.size;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
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
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert font != null;
		this.font = font;
		this.setSize();
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert pos != null;
		this.pos = pos;
	}

	private void setSize() {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

		this.size.set(new Point(this.font.getStringBounds(this.text, frc).getWidth(),
				this.font.getStringBounds(this.text, frc).getHeight()));
	}

	private void setSize(Point point) {
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
	 * @param text the text to set
	 */
	public void setText(String text) {
		if (this.lock) {
			this.lockWarn();
			return;
		}
		assert text != null;
		this.text = text;
		this.setSize();
	}
}
