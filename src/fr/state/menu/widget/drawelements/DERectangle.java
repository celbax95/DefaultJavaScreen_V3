package fr.state.menu.widget.drawelements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.DrawElement;
import fr.state.menu.widget.TextData;
import fr.util.point.Point;

public class DERectangle implements DrawElement {

	private abstract class BorderDrawer {
		public int prevBorderState;

		public abstract void draw(Graphics2D g);
	}

	private abstract class LabelDrawer {
		public int prevLabelState;

		public abstract void draw(Graphics2D g);
	}

	private Point pos;

	private Point halfSize;

	private Point size;

	private Color color;

	private BorderData border;

	private TextData label;

	private LabelDrawer labelDrawer;

	private BorderDrawer borderDrawer;

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

	private void changeBorderDrawer() {
		if (this.borderDrawer.prevBorderState == this.border.getState())
			return;

		switch (this.border.getState()) {
		case 0:
			// Inner
			this.borderDrawer = new BorderDrawer() {
				@Override
				public void draw(Graphics2D g) {
					Area a = new Area(new Rectangle(DERectangle.this.pos.ix(), DERectangle.this.pos.iy(),
							DERectangle.this.size.ix(), DERectangle.this.size.iy()));

					Area b = new Area(
							new Rectangle(DERectangle.this.pos.ix() + DERectangle.this.border.getThickness(),
									DERectangle.this.pos.iy() + DERectangle.this.border.getThickness(),
									DERectangle.this.size.ix() - DERectangle.this.border.getThickness() * 2,
									DERectangle.this.size.iy() - DERectangle.this.border.getThickness() * 2));

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
							new Rectangle(DERectangle.this.pos.ix() - DERectangle.this.border.getThickness(),
									DERectangle.this.pos.iy() - DERectangle.this.border.getThickness(),
									DERectangle.this.size.ix() + DERectangle.this.border.getThickness() * 2,
									DERectangle.this.size.iy() + DERectangle.this.border.getThickness() * 2));

					Area b = new Area(new Rectangle(DERectangle.this.pos.ix(), DERectangle.this.pos.iy(),
							DERectangle.this.size.ix(), DERectangle.this.size.iy()));

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
					g.drawString(DERectangle.this.label.getText(), DERectangle.this.label.getPos().ix(),
							DERectangle.this.label.getPos().iy());
				}
			};
			break;
		case 1:
			// Relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DERectangle.this.label.getText(),
							DERectangle.this.pos.ix() + DERectangle.this.label.getPos().ix(),
							DERectangle.this.pos.iy() + DERectangle.this.label.getPos().iy());
				}
			};
			break;
		case 2:
			// X centered - Y relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DERectangle.this.label.getText(),
							DERectangle.this.pos.ix() + DERectangle.this.halfSize.ix()
									- DERectangle.this.label.getSize().ix() / 2,
							DERectangle.this.pos.iy() + DERectangle.this.size.iy());
				}
			};
			break;
		case 3:
			// X relative - Y centered
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DERectangle.this.label.getText(),
							DERectangle.this.pos.ix() + DERectangle.this.size.ix(),
							DERectangle.this.pos.iy() + DERectangle.this.halfSize.iy()
									- DERectangle.this.label.getSize().iy() / 2);
				}
			};
			break;
		case 4:
			// Centered
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g) {
					g.drawString(DERectangle.this.label.getText(),
							DERectangle.this.pos.ix() + DERectangle.this.halfSize.ix()
									- DERectangle.this.label.getSize().ix() / 2,
							DERectangle.this.pos.iy() + DERectangle.this.halfSize.iy()
									- DERectangle.this.label.getSize().iy() / 2);
				}
			};
			break;
		}
	}

	@Override
	public void draw(Graphics2D g) {

		if (this.color != null) {
			g.setColor(this.color);
		}

		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());

		this.drawBorder(g);

		this.drawLabel(g);
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

		g.setFont(this.label.getFont());
		g.setColor(this.label.getColor());

		this.labelDrawer.draw(g);
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
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
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

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size;
		this.halfSize = size.clone().div(2);
	}
}
