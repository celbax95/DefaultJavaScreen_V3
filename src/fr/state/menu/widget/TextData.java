package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import fr.util.point.Point;

public class TextData {

	public Font font;

	public String text;
	public Color color;
	public Color secondColor;

	public TextData() {
		this.text = "";
		this.color = Color.black;
		this.secondColor = Color.black;
		this.font = new Font("Arial", Font.PLAIN, 20);
	}

	public TextData(Font font, String text, Color color, Color secondColor) {
		super();
		this.font = font;
		this.text = text;
		this.color = color;
		this.secondColor = secondColor;
	}

	public Point getSize() {

		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

		return new Point(this.font.getStringBounds(this.text, frc).getWidth(),
				this.font.getStringBounds(this.text, frc).getHeight());
	}
}
