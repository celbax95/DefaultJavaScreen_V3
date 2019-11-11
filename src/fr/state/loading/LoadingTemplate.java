package fr.state.loading;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import fr.util.point.Point;
import fr.window.WinData;

public class LoadingTemplate {

	WinData windata;

	String title;
	Point titlePos, titleSize; // Calculated

	Font font;

	ProgressBar progress;

	public LoadingTemplate(WinData windata) {
		this.windata = windata;

		this.setTitle(this.windata.getDefaultWindowSize().y / 3);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.setFont(this.font);
		g.drawString(this.title, this.titlePos.ix(), this.titlePos.iy());
		// this.progress.draw(g);
	}

	private void setTitle(double d) {

		this.title = "Loading ...";

		this.font = new Font("Copperplate Gothic Bold", Font.PLAIN, 100);

		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);

		this.titlePos = new Point();
		this.titleSize = new Point();

		this.titleSize.set(new Point(this.font.getStringBounds(this.title, frc).getWidth(),
				this.font.getStringBounds(this.title, frc).getHeight()));

		this.titlePos = new Point(this.windata.getDefaultWindowSize().x / 2D - this.titleSize.x / 2D, d);
	}
}
