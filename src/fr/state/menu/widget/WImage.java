package fr.state.menu.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.inputs.Input;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.point.Point;

public class WImage implements Widget {
	private Point pos;
	private Point size;

	private BorderData border;

	private Image image;

	private MenuPage page;

	public WImage(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.border = null;

		this.image = null;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.image != null) {
			g.drawImage(this.image, this.pos.ix(), this.pos.iy(), null);
		}
		if (this.border != null) {
			this.drawBorder(g);
		}

	}

	private void drawBorder(Graphics2D g) {
		g.setColor(this.border.color);

		Area a = new Area(new Rectangle(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy()));

		Area b = new Area(new Rectangle(this.pos.ix() + this.border.size, this.pos.iy() + this.border.size,
				this.size.ix() - this.border.size * 2, this.size.iy() - this.border.size * 2));

		a.subtract(b);

		g.fill(a);
	}

	public BorderData getBorder() {
		return this.border;
	}

	public Image getImage() {
		return this.image;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public Point getSize() {
		return this.size;
	}

	public void setBorder(BorderData border) {
		this.border = border;
	}

	public void setImage(Image image) {
		this.image = image;
		this.size.set(image.getWidth(null), image.getHeight(null));
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	@Override
	public void update(Input input) {
	}
}
