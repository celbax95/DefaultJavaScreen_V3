package fr.state.menu.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WImageButton implements Widget {
	private Point pos;
	private Point size;

	private BorderData border;

	private Image image;

	private Image pressedImage;

	private AABB hitbox;

	private boolean pressed;

	private boolean canPressed;

	private MenuPage page;

	public WImageButton(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.border = null;

		this.canPressed = true;
	}

	public abstract void action();

	private void calcHitbox() {
		this.hitbox.min(this.pos);
		this.hitbox.max(this.pos.clone().add(this.size));
	}

	@Override
	public void draw(Graphics2D g) {

		this.drawButton(g);

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

	private void drawButton(Graphics2D g) {
		if (!this.pressed || this.pressedImage == null) {
			g.drawImage(this.image, this.pos.ix(), this.pos.iy(), null);
		} else {
			g.drawImage(this.pressedImage, this.pos.ix(), this.pos.iy(), null);
		}

	}

	public BorderData getBorder() {
		return this.border;
	}

	public AABB getHitbox() {
		return this.hitbox;
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

	public Image getPressedImage() {
		return this.pressedImage;
	}

	public Point getSize() {
		return this.size;
	}

	public boolean isCanPressed() {
		return this.canPressed;
	}

	public boolean isPressed() {
		return this.pressed;
	}

	public void setBorder(BorderData border) {
		this.border = border;
	}

	public void setCanPressed(boolean canPressed) {
		this.canPressed = canPressed;
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setImage(Image image) {
		this.image = image;
		int w = image.getWidth(null), h = image.getHeight(null);
		if (w > this.size.x) {
			this.size.x = w;
		}
		if (h > this.size.y) {
			this.size.y = h;
		}
		this.calcHitbox();
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
		this.calcHitbox();
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public void setPressedImage(Image pressedImage) {
		this.pressedImage = pressedImage;
		int w = this.image.getWidth(null), h = this.image.getHeight(null);
		if (w > this.size.x) {
			this.size.x = w;
		}
		if (h > this.size.y) {
			this.size.y = h;
		}
		this.calcHitbox();
	}

	public void setSize(Point size) {
		this.size.set(size);
		this.calcHitbox();
	}

	@Override
	public void update(Input input) {

		for (MouseEvent e : input.mouseEvents) {
			// on button
			if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
				switch (e.id) {
				case MouseEvent.LEFT_PRESSED:
					if (this.canPressed) {
						this.pressed = true;
					}
					break;
				case MouseEvent.LEFT_RELEASED:
					if (this.pressed) {
						this.action();
					}
					break;
				}
			}

			// out of button
			switch (e.id) {
			case MouseEvent.LEFT_RELEASED:
				this.pressed = false;
				break;
			}
		}

		this.pressed = input.mouseButtons.get(Input.MOUSE_LEFT)
				&& Collider.AABBvsPoint(this.hitbox, input.mousePos);
	}
}
