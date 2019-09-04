package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import fr.util.point.Point;

public class TextData {

	private Point pos;

	private Point size;

	private Font font;

	private String text;

	private Color color;

	private int state;

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
		super();
		this.setPos(pos);
		this.setFont(font);
		this.setText(text);
		this.setColor(color);
		this.setState(state);
	}

	private Point calcSize() {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

		return new Point(this.font.getStringBounds(this.text, frc).getWidth(),
				this.font.getStringBounds(this.text, frc).getHeight());
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
		assert font != null;
		this.font = font;
		this.calcSize();
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		assert pos != null;
		this.pos = pos;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		assert state >= 0;
		this.state = state;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		assert text != null;
		this.text = text;
		this.calcSize();
	}
}
