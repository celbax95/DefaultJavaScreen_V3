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
	private Point size;

	private DrawElement on, pressedOn;
	private DrawElement off, pressedOff;

	private AABB hitbox;

	private boolean active;

	private boolean pressed;

	private MenuPage page;

	public WSwitch(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.on = null;
		this.pressedOn = null;

		this.off = null;
		this.pressedOff = null;

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.active = false;
	}

	public abstract void actionOff();

	public abstract void actionOn();

	@Override
	public void draw(Graphics2D g) {
		if (!this.active) {
			if (!this.pressed || this.pressedOn == null) {
				if (this.on != null) {
					this.on.draw(g);
				}
			} else {
				this.pressedOn.draw(g);
			}
		} else {
			if (!this.pressed || this.pressedOff == null) {
				if (this.off != null) {
					this.off.draw(g);
				}
			} else {
				this.pressedOff.draw(g);
			}
		}
	}

	@Override
	public void update(Input input) {

		boolean needHitboxCalc = false;

		for (MouseEvent e : input.mouseEvents) {
			// on button
			if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
				switch (e.id) {
				case MouseEvent.LEFT_RELEASED:
					if (this.active) {
						this.actionOff();
					} else {
						this.actionOn();
					}
					this.active = !this.active;
					needHitboxCalc = true;
					break;
				}
			}
		}

		boolean pressed = input.mouseButtons.get(Input.MOUSE_LEFT)
				&& Collider.AABBvsPoint(this.hitbox, input.mousePos);

		if (this.pressed != pressed || needHitboxCalc) {
			this.updateHitbox();
		}
	}

	private void updateHitbox() {
		if (!this.active) {
			if (!this.pressed || this.pressedOn == null) {
				if (this.on != null) {
					this.hitbox.min(this.on.getPos());
					this.hitbox.max(this.on.getSize());
				}
			} else {
				this.hitbox.min(this.pressedOn.getPos());
				this.hitbox.max(this.pressedOn.getSize());
			}
		} else {
			if (!this.pressed || this.pressedOff == null) {
				if (this.off != null) {
					this.hitbox.min(this.off.getPos());
					this.hitbox.max(this.off.getSize());
				}
			} else {
				this.hitbox.min(this.pressedOff.getPos());
				this.hitbox.max(this.pressedOff.getSize());
			}
		}
	}
}
