package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WSlider implements Widget {

	private Point pos;
	private Point size;

	private Point barPos;
	private Point barSize;
	private double barHeightScale;

	private Point slidePos;
	private Point slideSize;
	private int slideWidth;

	private BorderData border;

	private Color barColor;

	private Color slideColor;

	private Color pressedSlideColor;

	private AABB hitbox;

	private int clicNumber; // [1 : 100]
	private int clic;

	private boolean pressed;

	private MenuPage page;

	public WSlider(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();
		this.barPos = new Point();
		this.barSize = new Point();
		this.slidePos = new Point();
		this.slideSize = new Point();

		this.calcBar();
		this.calcSlide();

		this.slideWidth = 20;

		this.barHeightScale = 1 / 3.;

		this.barColor = Color.BLACK;
		this.slideColor = Color.BLACK;
		this.pressedSlideColor = null;

		this.clicNumber = 100;
		this.clic = 0;

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.border = null;

		this.pressed = false;
	}

	private void calcBar() {
		int barHeight = (int) (this.size.y * this.barHeightScale);

		this.barPos.set(
				new Point(this.pos.x + this.slideWidth / 2, this.pos.y + this.size.y / 2 - barHeight / 2));
		this.barSize.set(new Point(this.size.x - this.slideWidth, barHeight));
	}

	private void calcSlide() {
		this.slidePos.set(new Point(this.pos.x - this.slideWidth / 2, this.pos.y));
		this.slideSize.set(new Point(this.slideWidth, this.size.y));
		this.moveSlidePos();
	}

	@Override
	public void draw(Graphics2D g) {

		this.drawBorder(g);

		g.setColor(this.barColor);
		g.fillRect(this.barPos.ix(), this.barPos.iy(), this.barSize.ix(), this.barSize.iy());

		if (this.pressed && this.pressedSlideColor != null) {
			g.setColor(this.pressedSlideColor);
		} else {
			g.setColor(this.slideColor);
		}
		g.fillRect(this.slidePos.ix(), this.slidePos.iy(), this.slideSize.ix(), this.slideSize.iy());
	}

	private void drawBorder(Graphics2D g) {
		g.setColor(this.border.color);

		Area a = new Area(new Rectangle(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy()));

		Area b = new Area(new Rectangle(this.pos.ix() + this.border.size, this.pos.iy() + this.border.size,
				this.size.ix() - this.border.size * 2, this.size.iy() - this.border.size * 2));

		a.subtract(b);

		g.fill(a);
	}

	public Color getBarColor() {
		return this.barColor;
	}

	public double getBarHeightScale() {
		return this.barHeightScale;
	}

	public Point getBarPos() {
		return this.barPos;
	}

	public Point getBarSize() {
		return this.barSize;
	}

	public BorderData getBorder() {
		return this.border;
	}

	public int getClic() {
		return this.clic;
	}

	public int getClicNumber() {
		return this.clicNumber;
	}

	public AABB getHitbox() {
		return this.hitbox;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public Color getPressedSlideColor() {
		return this.pressedSlideColor;
	}

	public Point getSize() {
		return this.size;
	}

	public Color getSlideColor() {
		return this.slideColor;
	}

	public Point getSlidePos() {
		return this.slidePos;
	}

	public Point getSlideSize() {
		return this.slideSize;
	}

	public int getSlideWidth() {
		return this.slideWidth;
	}

	public boolean isPressed() {
		return this.pressed;
	}

	private void moveSlide(Point p) {

		p.x = clamp(p.x, this.barPos.x, this.barPos.x + this.barSize.x) - this.barPos.x;

		int last = this.clic;

		if (p.x == 0) {
			this.clic = 0;
		} else if (p.x == this.barSize.x) {
			this.clic = this.clicNumber;
		} else {
			this.clic = (int) Math.round(p.x * this.clicNumber / this.barSize.x);
		}

		if (this.clic != last) {
			this.valueChanged(this.clic);
		}
	}

	private void moveSlidePos() {
		this.slidePos.x = this.pos.x + this.clic * (this.size.x - this.slideWidth) / this.clicNumber;
	}

	public void setBarColor(Color barColor) {
		this.barColor = barColor;
	}

	public void setBarHeightScale(double barHeightScale) {
		this.barHeightScale = barHeightScale;
		this.calcBar();
	}

	public void setBorder(BorderData border) {
		this.border = border;
	}

	public void setClic(int clic) {
		this.clic = clic;
	}

	public void setClicNumber(int clicNumber) {
		if (clicNumber <= 0) {
			clicNumber = 1;
		}
		this.clicNumber = clicNumber;
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos = pos;
		this.hitbox.min(pos);
		this.hitbox.max(pos.clone().add(this.size));
		this.calcBar();
		this.calcSlide();
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public void setPressedSlideColor(Color pressedSlideColor) {
		this.pressedSlideColor = pressedSlideColor;
	}

	public void setSize(Point size) {
		this.size = size;
		this.hitbox.min(this.pos);
		this.hitbox.max(this.pos.clone().add(this.size));
		this.calcBar();
		this.calcSlide();
	}

	public void setSlideColor(Color slideColor) {
		this.slideColor = slideColor;
	}

	public void setSlideWidth(int slideWidth) {
		this.slideWidth = slideWidth;
		this.calcBar();
		this.calcSlide();
	}

	@Override
	public void update(Input input) {

		for (MouseEvent e : input.mouseEvents) {

			// out of
			switch (e.id) {
			case MouseEvent.LEFT_RELEASED:
				this.pressed = false;
				break;
			case MouseEvent.MOVE:
				if (this.pressed) {
					this.moveSlide(e.pos);
				}
				break;
			default:
				break;
			}

			// on
			if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
				switch (e.id) {
				case MouseEvent.LEFT_PRESSED:
					this.pressed = true;
					this.moveSlide(e.pos);
					break;
				default:
					break;
				}
			}
		}

		// Other
		this.moveSlidePos();
	}

	public abstract void valueChanged(int value);

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}
}
