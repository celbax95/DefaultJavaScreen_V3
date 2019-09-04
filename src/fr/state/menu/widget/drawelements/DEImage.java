package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DEImage implements DrawElement {

	private abstract class BorderDrawer {
		public int prevBorderState;

		public abstract void draw(Graphics2D g);
	}

	private abstract class LabelDrawer {
		public int prevLabelState;

		public abstract void draw(Graphics2D g);
	}

	private LabelDrawer labelDrawer;

	private BorderDrawer borderDrawer;

	private Point pos;

	private Point size;

	private BorderData border;

	private TextData label;

	private Image image;

	/**
	 *
	 */
	public DEImage() {
		super();
		this.pos = new Point();
		this.size = new Point();

		this.border = null;

		this.label = null;

		this.image = null;

		this.labelDrawer = new LabelDrawer() {
			@Override
			public void draw(Graphics2D g) {
			}
		};
		this.labelDrawer.prevLabelState = -1;

		this.borderDrawer = new BorderDrawer() {
			@Override
			public void draw(Graphics2D g) {
			}
		};
		this.borderDrawer.prevBorderState = -1;
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

	private void changeBorderDrawer() {
		if (this.borderDrawer.prevBorderState != this.border.getState())
			return;

		switch (this.border.getState()) {
		case 0:
			// Inner
			this.borderDrawer = new BorderDrawer() {
				@Override
				public void draw(Graphics2D g) {
					Area a = new Area(new Rectangle(DEImage.this.pos.ix(), DEImage.this.pos.iy(),
							DEImage.this.size.ix(), DEImage.this.size.iy()));

					Area b = new Area(
							new Rectangle(DEImage.this.pos.ix() + DEImage.this.border.getThickness(),
									DEImage.this.pos.iy() + DEImage.this.border.getThickness(),
									DEImage.this.size.ix() - DEImage.this.border.getThickness() * 2,
									DEImage.this.size.iy() - DEImage.this.border.getThickness() * 2));

					a.subtract(b);

					g.fill(a);
				}
			};
			break;
		case 1:
			// outter
			this.borderDrawer = new BorderDrawer() {
				@Override
				public void draw(Graphics2D g) {
					Area a = new Area(
							new Rectangle(DEImage.this.pos.ix() - DEImage.this.border.getThickness(),
									DEImage.this.pos.iy() - DEImage.this.border.getThickness(),
									DEImage.this.size.ix() + DEImage.this.border.getThickness() * 2,
									DEImage.this.size.iy() + DEImage.this.border.getThickness() * 2));

					Area b = new Area(new Rectangle(DEImage.this.pos.ix(), DEImage.this.pos.iy(),
							DEImage.this.size.ix(), DEImage.this.size.iy()));

					a.subtract(b);

					g.fill(a);
				}
			};
			break;
		}
	}

	private void changeLabelDrawer() {
		if (this.labelDrawer.prevLabelState == this.label.getState())
			return;

		switch (this.label.getState()) {
		case 0:
			// Absolute
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DEImage.this.label.getText(), DEImage.this.label.getPos().ix(),
							DEImage.this.label.getPos().iy());
				}
			};
			break;
		case 1:
			// Relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DEImage.this.label.getText(),
							DEImage.this.pos.ix() + DEImage.this.label.getPos().ix(),
							DEImage.this.pos.iy() + DEImage.this.label.getPos().iy());
				}
			};
			break;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		this.drawBorder(g);

		if (this.image != null) {
			g.drawImage(this.image, this.pos.ix(), this.pos.iy(), null);
		}
	}

	private void drawBorder(Graphics2D g) {
		if (this.border == null)
			return;

		this.changeBorderDrawer();

		g.setColor(this.border.getColor());

		this.borderDrawer.draw(g);
	}

	private void drawLabel(Graphics2D g) {
		if (this.label == null)
			return;

		this.changeLabelDrawer();

		g.setColor(this.label.getColor());
		g.setFont(this.label.getFont());

		this.labelDrawer.draw(g);
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
	 * @param border the border to set
	 */
	public void setBorder(BorderData border) {
		this.border = border;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		assert image != null;
		this.image = image;
		this.size.set(image.getWidth(null), image.getHeight(null));
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
