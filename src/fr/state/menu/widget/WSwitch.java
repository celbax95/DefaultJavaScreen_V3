package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.logger.Logger;
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

	private boolean mouseOn;

	private boolean visible;

	private MenuPage page;

	private boolean enabled;

	public WSwitch(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.on = null;
		this.pressedOn = null;

		this.off = null;
		this.pressedOff = null;

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.active = false;

		this.pressed = false;

		this.mouseOn = false;

		this.visible = true;

		this.enabled = true;

		this.currentDE = null;
	}

	public WSwitch(WSwitch other) {
		this(other == null ? null : other.page);
		if (other == null)
			return;

		this.setPos(new Point(other.pos));
		this.setOnDrawElement(other.on == null ? null : other.on.clone());
		this.setPressedOnDrawElement(other.pressedOn == null ? null : other.pressedOn.clone());
		this.setOffDrawElement(other.off == null ? null : other.off.clone());
		this.setPressedOffDrawElement(other.pressedOff == null ? null : other.pressedOff.clone());

		AABB hb = new AABB(this.pos, new Point(), new Point());
		hb.min(new Point(other.hitbox.min()));
		hb.max(new Point(other.hitbox.max()));

		this.setEnabled(other.enabled);

		this.setActive(other.active);
		this.setPressed(false);
		this.setPage(other.page);
	}

	public abstract void actionOff();

	public abstract void actionOn();

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.currentDE != null) {
			this.currentDE.draw(g, this.pos);
		} else {
			Logger.err("Un " + this.getClass().getSimpleName() + " utilise n'a pas de de drawElement");
			return;
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
	@Override
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
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @return the mouseOn
	 */
	public boolean isMouseOn() {
		return this.mouseOn;
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return this.pressed;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;

			this.setCurrentDE();

			if (this.active) {
				this.actionOn();
			} else {
				this.actionOff();
			}
		}
	}

	private void setCurrentDE() {
		if (this.active) {
			if (this.pressed && this.mouseOn && this.pressedOn != null) {
				this.currentDE = this.pressedOn;
			} else {
				this.currentDE = this.on;
			}
		} else {
			if (this.pressed && this.mouseOn && this.pressedOff != null) {
				this.currentDE = this.pressedOff;
			} else {
				this.currentDE = this.off;
			}
		}
	}

	@SuppressWarnings("unused")
	private void setCurrentDE(DrawElement currentDE) {
		this.currentDE = currentDE;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.setPressed(false);
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
	 * @param mouseOn the mouseOn to set
	 */
	public void setMouseOn(boolean mouseOn) {
		if (mouseOn != this.mouseOn) {
			this.mouseOn = mouseOn;
			this.setCurrentDE();
		}
	}

	/**
	 * @param off the off to set
	 */
	public void setOffDrawElement(DrawElement off) {
		if (off != null) {
			this.off = off.clone();
			this.off.lock();

			if (!this.active) {
				this.currentDE = this.off;
			}
		}

	}

	/**
	 * @param on the on to set
	 */
	public void setOnDrawElement(DrawElement on) {
		if (on != null) {
			this.on = on.clone();
			this.on.lock();

			if (this.active) {
				this.currentDE = this.on;
			}
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
	@Override
	public void setPos(Point pos) {
		this.pos.set(pos);

	}

	/**
	 * @param pressed the pressed to set
	 */
	public void setPressed(boolean pressed) {
		if (pressed != this.pressed) {
			this.pressed = pressed;
			this.setCurrentDE();
		}
	}

	/**
	 * @param pressedOff the pressedOff to set
	 */
	public void setPressedOffDrawElement(DrawElement pressedOff) {
		if (pressedOff != null) {
			this.pressedOff = pressedOff.clone();
			this.pressedOff.lock();
		}
	}

	/**
	 * @param pressedOn the pressedOn to set
	 */
	public void setPressedOnDrawElement(DrawElement pressedOn) {
		if (pressedOn != null) {
			this.pressedOn = pressedOn.clone();
			this.pressedOn.lock();
		}
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
		if (!this.visible)
			return;

		for (MouseEvent e : input.mouseEvents) {
			switch (e.id) {
			case MouseEvent.LEFT_PRESSED:
				if (this.enabled && Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setPressed(true);
				}
				continue;
			case MouseEvent.LEFT_RELEASED:
				if (this.enabled && Collider.AABBvsPoint(this.hitbox, e.pos) && this.pressed) {
					this.setActive(!this.active);
				}
				this.setPressed(false);
				continue;
			}
		}
		this.setMouseOn(Collider.AABBvsPoint(this.hitbox, input.mousePos));
	}
}
