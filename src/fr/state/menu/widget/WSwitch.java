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

public abstract class WSwitch implements Widget {
	private Point pos;
	private Point size;
	private Point halfSize;

	private TextData labelOn;
	private TextData labelOff;

	private BorderData border;

	private Color colorOn;
	private Color colorOff;

	private Color pressedColorOn;
	private Color pressedColorOff;

	private AABB hitbox;

	private boolean active;

	private boolean pressed;

	private MenuPage page;

	public WSwitch(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.colorOn = Color.BLACK;
		this.colorOff = Color.BLACK;

		this.pressedColorOn = null;
		this.pressedColorOff = null;

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.labelOn = null;
		this.labelOff = null;

		this.border = null;

		this.active = false;
	}

	public abstract void actionOff();

	public abstract void actionOn();

	@Override
	public void draw(Graphics2D g) {

		this.drawButton(g);

		this.drawBorder(g);

		this.drawLabel(g, this.active ? this.labelOn : this.labelOff);
	}

	private void drawBorder(Graphics2D g) {
		if (this.border == null)
			return;

		g.setColor(this.border.color);

		Area a = new Area(new Rectangle(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy()));

		Area b = new Area(new Rectangle(this.pos.ix() + this.border.size, this.pos.iy() + this.border.size,
				this.size.ix() - this.border.size * 2, this.size.iy() - this.border.size * 2));

		a.subtract(b);

		g.fill(a);
	}

	private void drawButton(Graphics2D g) {
		g.setColor(this.active
				? this.pressed && this.pressedColorOn != null ? this.pressedColorOn : this.colorOn
				: this.pressed && this.pressedColorOff != null ? this.pressedColorOff : this.colorOff);

		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	private void drawLabel(Graphics2D g, TextData l) {
		if (l == null)
			return;

		g.setFont(l.font);

		if (!this.pressed || l.secondColor == null) {
			g.setColor(l.color);
		} else {
			g.setColor(l.secondColor);
		}

		int width2 = g.getFontMetrics().stringWidth(l.text) / 2;

		g.drawString(l.text, this.pos.ix() + this.halfSize.ix() - width2,
				this.pos.iy() + this.halfSize.iy() + l.font.getSize() / 2);
	}

	public BorderData getBorder() {
		return this.border;
	}

	public Color getColorOff() {
		return this.colorOff;
	}

	public Color getColorOn() {
		return this.colorOn;
	}

	public AABB getHitbox() {
		return this.hitbox;
	}

	public TextData getLabelOff() {
		return this.labelOff;
	}

	public TextData getLabelOn() {
		return this.labelOn;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public Color getPressedColorOff() {
		return this.pressedColorOff;
	}

	public Color getPressedColorOn() {
		return this.pressedColorOn;
	}

	public Point getSize() {
		return this.size;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isPressed() {
		return this.pressed;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setBorder(BorderData border) {
		this.border = border;
	}

	public void setColorOff(Color colorOff) {
		this.colorOff = colorOff;
	}

	public void setColorOn(Color colorOn) {
		this.colorOn = colorOn;
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setLabelOff(TextData labelOff) {
		this.labelOff = labelOff;
	}

	public void setLabelOn(TextData labelOn) {
		this.labelOn = labelOn;
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

	public void setPressedColorOff(Color pressedColorOff) {
		this.pressedColorOff = pressedColorOff;
	}

	public void setPressedColorOn(Color pressedColorOn) {
		this.pressedColorOn = pressedColorOn;
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
					this.pressed = true;
					break;
				case MouseEvent.LEFT_RELEASED:
					if (this.active) {
						this.actionOff();
					} else {
						this.actionOn();
					}
					this.active = !this.active;
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
