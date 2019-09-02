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

public abstract class WButton implements Widget {
	private Point pos;
	private Point size;
	private Point halfSize;

	private TextData label;

	private BorderData border;

	private Color color;

	private Color pressedColor;

	private AABB hitbox;

	private boolean pressed;

	private boolean canPressed;

	private MenuPage page;

	public WButton(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.color = Color.BLACK;
		this.pressedColor = Color.BLACK;

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.label = null;

		this.border = null;

		this.canPressed = true;
	}

	public abstract void action();

	@Override
	public void draw(Graphics2D g) {

		this.drawButton(g);

		if (this.border != null) {
			this.drawBorder(g);
		}

		if (this.label != null) {
			this.drawLabel(g);
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
		if (!this.pressed || this.pressedColor == null) {
			g.setColor(this.color);
		} else {
			g.setColor(this.pressedColor);
		}

		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	private void drawLabel(Graphics2D g) {
		g.setFont(this.label.font);

		if (!this.pressed || this.label.secondColor == null) {
			g.setColor(this.label.color);
		} else {
			g.setColor(this.label.secondColor);
		}

		int width2 = g.getFontMetrics().stringWidth(this.label.text) / 2;

		g.drawString(this.label.text, this.pos.ix() + this.halfSize.ix() - width2,
				this.pos.iy() + this.halfSize.iy() + this.label.font.getSize() / 2);
	}

	public BorderData getBorder() {
		return this.border;
	}

	public Color getColor() {
		return this.color;
	}

	public AABB getHitbox() {
		return this.hitbox;
	}

	public TextData getLabel() {
		return this.label;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public Color getPressedColor() {
		return this.pressedColor;
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

	public void setColor(Color color) {
		this.color = color;
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setLabel(TextData label) {
		this.label = label;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
		this.hitbox.min(pos);
		this.hitbox.max(pos.clone().add(this.size));
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public void setPressedColor(Color pressedColor) {
		this.pressedColor = pressedColor;
	}

	public void setSize(Point size) {
		this.size.set(size);
		this.halfSize = size.clone().div(2);
		this.hitbox.min(this.pos);
		this.hitbox.max(this.pos.clone().add(size));
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
