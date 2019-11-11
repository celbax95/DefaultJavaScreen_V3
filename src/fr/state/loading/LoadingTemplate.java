package fr.state.loading;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import fr.util.point.Point;
import fr.window.WinData;

public class LoadingTemplate {

	private WinData windata;

	private String title;
	private Point titlePos, titleSize; // Calculated

	private Font font;

	private ProgressBar progress;

	public LoadingTemplate(WinData windata, int steps) {
		this.windata = windata;

		this.setTitle(this.windata.getDefaultWindowSize().y / 3);

		this.setProgressBar(this.windata.getDefaultWindowSize().y - this.windata.getDefaultWindowSize().y / 4,
				new Point(this.windata.getDefaultWindowSize().x - this.windata.getDefaultWindowSize().x / 5, 50),
				steps);

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				for (int i = 0; i < 11; i++) {
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// e.printStackTrace();
//					}
//					LoadingTemplate.this.progress.setCurrent(LoadingTemplate.this.progress.getCurrent() + 1);
//				}
//			}
//		}).start();
	}

	public void addSteps(int x) {
		this.progress.setCurrent(this.progress.getCurrent() + x);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(this.font);
		g.drawString(this.title, this.titlePos.ix(), this.titlePos.iy());
		this.progress.draw(g);
	}

	public void setMaxSteps(int x) {
		this.progress.setMaxSteps(x);
	}

	private void setProgressBar(double d, Point size, int steps) {

		Point pos = new Point((this.windata.getDefaultWindowSize().x - size.x) / 2, d);

		this.progress = new ProgressBar(pos, size, steps, Color.LIGHT_GRAY, new Color(0, 120, 0), 1, Color.WHITE);
	}

	public void setStep(int x) {
		this.progress.setCurrent(x);
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
