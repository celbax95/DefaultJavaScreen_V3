package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WButton implements Widget {
	private Point pos;

	private AABB hitbox;

	private DrawElement stdDrawElement;

	private DrawElement pressedDrawElement;

	private DrawElement currentDE;

	private boolean pressed;

	private boolean canPressed;

	private MenuPage page;

	public WButton(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.canPressed = true;

		this.stdDrawElement = null;

		this.pressedDrawElement = null;

		this.currentDE = null;
	}

	public abstract void action();

	@Override
	public void draw(Graphics2D g) {
		if (this.currentDE != null) {
			this.currentDE.draw(g, this.pos);
		}
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

	/**
	 * @return the pressedDrawElement
	 */
	public DrawElement getPressedDrawElement() {
		return this.pressedDrawElement;
	}

	public Point getSize() {
		return this.currentDE != null ? this.currentDE.getSize() : new Point();
	}

	/**
	 * @return the drawElement
	 */
	public DrawElement getStdDrawElement() {
		return this.stdDrawElement;
	}

	public boolean isCanPressed() {
		return this.canPressed;
	}

	public boolean isPressed() {
		return this.pressed;
	}

	public void setCanPressed(boolean canPressed) {
		this.canPressed = canPressed;
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setHitboxFromDrawElement() {
		this.hitbox.min(this.pos.clone().add(this.stdDrawElement.getPos()));
		this.hitbox
				.max(this.pos.clone().add(this.stdDrawElement.getPos()).add(this.stdDrawElement.getSize()));
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;

		// Changement du drawElement courant
		this.currentDE = pressed && this.pressedDrawElement != null ? this.pressedDrawElement
				: this.stdDrawElement;
	}

	/**
	 * @param pressedDrawElement the pressedDrawElement to set
	 */
	public void setPressedDrawElement(DrawElement pressedDrawElement) {
		this.pressedDrawElement = pressedDrawElement;
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setStdDrawElement(DrawElement drawElement) {
		this.stdDrawElement = drawElement;
	}

	@Override
	public void update(Input input) {

		for (MouseEvent e : input.mouseEvents) {
			// on button
			if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
				switch (e.id) {
				case MouseEvent.LEFT_RELEASED:
					if (this.pressed) {
						this.action();
					}
					break;
				}
			}
		}

		this.setPressed(input.mouseButtons.get(Input.MOUSE_LEFT)
				&& Collider.AABBvsPoint(this.hitbox, input.mousePos));
	}
}
