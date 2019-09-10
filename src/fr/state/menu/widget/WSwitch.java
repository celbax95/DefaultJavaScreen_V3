package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WSwitch implements Widget {
	private Point pos;

	private DrawElement on, pressedOn;
	private DrawElement off, pressedOff;

	private DrawElement currentDE;

	private AABB hitbox;

	private boolean active;

	private boolean pressed;

	private MenuPage page;

	public WSwitch(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.on = null;
		this.pressedOn = null;

		this.off = null;
		this.pressedOff = null;

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.active = false;

		this.on = null;

		this.pressedOn = null;

		this.off = null;

		this.pressedOff = null;

		this.currentDE = null;
	}

	public abstract void actionOff();

	public abstract void actionOn();

	@Override
	public void draw(Graphics2D g) {
		if (this.currentDE != null) {
			this.currentDE.draw(g, this.pos);
		}
	}

	/**
	 * @return the hitbox
	 */
	public AABB getHitbox() {
		return this.hitbox;
	}

	/**
	 * @return the off
	 */
	public DrawElement getOffDrawElement() {
		return this.off;
	}

	/**
	 * @return the on
	 */
	public DrawElement getOnDrawElement() {
		return this.on;
	}

	/**
	 * @return the page
	 */
	public MenuPage getPage() {
		return this.page;
	}

	/**
	 * @return the pos
	 */
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the pressedOff
	 */
	public DrawElement getPressedOffDrawElement() {
		return this.pressedOff;
	}

	/**
	 * @return the pressedOn
	 */
	public DrawElement getPressedOnDrawElement() {
		return this.pressedOn;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.currentDE == null ? new Point() : this.currentDE.getSize();
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return this.pressed;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			this.updateCurrentDE();
			if (this.active) {
				this.actionOff();
			} else {
				this.actionOn();
			}
		}
	}

	/**
	 * @param hitbox the hitbox to set
	 */
	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setHitboxFromDrawElement() {
		if (this.currentDE == null)
			return;

		this.hitbox.min(this.pos.clone().add(this.currentDE.getPos()));
		this.hitbox.max(this.pos.clone().add(this.currentDE.getPos()).add(this.currentDE.getSize()));
	}

	/**
	 * @param off the off to set
	 */
	public void setOffDrawElement(DrawElement off) {
		this.off = off;

		if (!this.active) {
			this.currentDE = off;
		}
	}

	/**
	 * @param on the on to set
	 */
	public void setOnDrawElement(DrawElement on) {
		this.on = on;

		if (this.active) {
			this.currentDE = on;
		}
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(MenuPage page) {
		this.page = page;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	/**
	 * @param pressed the pressed to set
	 */
	public void setPressed(boolean pressed) {
		if (pressed != this.pressed) {
			this.pressed = pressed;
			this.updateCurrentDE();
		}
	}

	/**
	 * @param pressedOff the pressedOff to set
	 */
	public void setPressedOffDrawElement(DrawElement pressedOff) {
		this.pressedOff = pressedOff;
	}

	/**
	 * @param pressedOn the pressedOn to set
	 */
	public void setPressedOnDrawElement(DrawElement pressedOn) {
		this.pressedOn = pressedOn;
	}

	@Override
	public void update(Input input) {
		for (MouseEvent e : input.mouseEvents) {
			switch (e.id) {
			case MouseEvent.LEFT_PRESSED:
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setPressed(true);
				}
				continue;
			case MouseEvent.LEFT_RELEASED:
				this.setPressed(false);
				if (Collider.AABBvsPoint(this.hitbox, e.pos) && this.pressed) {
					this.setActive(!this.active);
				}
				continue;
			}
		}
	}

	private void updateCurrentDE() {
		if (this.active) {
			if (!this.pressed || this.pressedOn == null) {
				this.currentDE = this.on;
			} else {
				this.currentDE = this.pressedOn;
			}
		} else {
			if (!this.pressed || this.pressedOff == null) {
				this.currentDE = this.off;
			} else {
				this.currentDE = this.pressedOff;
			}
		}
	}
}
