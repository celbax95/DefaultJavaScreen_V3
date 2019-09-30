package fr.state.menu.widget.drawelements;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.logger.Logger;
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DEImage implements DrawElement {

	private abstract class BorderDrawer {
		public int prevBorderState;

		public abstract void draw(Graphics2D g, Point absp);
	}

	private abstract class LabelDrawer {
		public int prevLabelState;

		public abstract void draw(Graphics2D g, Point absp);
	}

	private boolean lock;

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
			public void draw(Graphics2D g, Point absp) {
			}
		};
		this.labelDrawer.prevLabelState = -1;

		this.borderDrawer = new BorderDrawer() {
			@Override
			public void draw(Graphics2D g, Point absp) {
			}
		};
		this.borderDrawer.prevBorderState = -1;
	}

	public DEImage(DEImage other) {
		this();
		if (other == null)
			return;
		this.lock = false;
		this.setPos(new Point(other.pos));
		this.setSize(new Point(other.size));
		this.border = new BorderData(other.border);
		this.label = new TextData(other.label);
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

	private void changeBorderDrawer() {
		if (this.borderDrawer.prevBorderState != this.border.getState())
			return;

		switch (this.border.getState()) {
		case 0:
			// Inner
			this.borderDrawer = new BorderDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					Area a = new Area(new Rectangle(absp.ix(), absp.iy(), DEImage.this.size.ix(),
							DEImage.this.size.iy()));

					Area b = new Area(new Rectangle(absp.ix() + DEImage.this.border.getThickness(),
							absp.iy() + DEImage.this.border.getThickness(),
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
				public void draw(Graphics2D g, Point absp) {
					Area a = new Area(new Rectangle(absp.ix() - DEImage.this.border.getThickness(),
							absp.iy() - DEImage.this.border.getThickness(),
							DEImage.this.size.ix() + DEImage.this.border.getThickness() * 2,
							DEImage.this.size.iy() + DEImage.this.border.getThickness() * 2));

					Area b = new Area(new Rectangle(absp.ix(), absp.iy(), DEImage.this.size.ix(),
							DEImage.this.size.iy()));

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
			// Relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					Point absTextPos = absp.clone().add(DEImage.this.label.getPos());
					g.drawString(DEImage.this.label.getText(), absTextPos.ix(), absTextPos.iy());
				}
			};
			break;
		}
	}

	@Override
	public DrawElement clone() {
		return new DEImage(this);
	}

	@Override
	public void draw(Graphics2D g, Point ref) {
		// Absolute Point
		Point absp = ref.clone().add(this.pos);

		this.drawBorder(g, absp);

		if (this.image != null) {
			g.drawImage(this.image, absp.ix(), absp.iy(), null);
		}

		this.drawLabel(g, absp);
	}

	private void drawBorder(Graphics2D g, Point absp) {
		if (this.border == null)
			return;

		this.changeBorderDrawer();
		g.setColor(this.border.getColor());

		this.borderDrawer.draw(g, absp);
	}

	private void drawLabel(Graphics2D g, Point absp) {
		if (this.label == null)
			return;

		this.changeLabelDrawer();

		g.setColor(this.label.getColor());
		g.setFont(this.label.getFont());

		this.labelDrawer.draw(g, absp);
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
		this.border = border;
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
		this.label = label;
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
