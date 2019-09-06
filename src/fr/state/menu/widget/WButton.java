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

	private Point size;

	private AABB hitbox;

	private DrawElement drawElement;

	private DrawElement pressedDrawElement;

	private boolean pressed;

	private boolean canPressed;

	private MenuPage page;

	public WButton(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.size = new Point();

		this.hitbox = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		this.canPressed = true;

		this.drawElement = null;

		this.pressedDrawElement = null;
	}

	public abstract void action();

	@Override
	public void draw(Graphics2D g) {
		if (!this.pressed || this.pressedDrawElement == null) {
			this.drawElement.draw(g);
		} else {
			this.pressedDrawElement.draw(g);
		}
	}

	/**
	 * @return the drawElement
	 */
	public DrawElement getDrawElement() {
		return this.drawElement;
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
		return this.size;
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

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setDrawElement(DrawElement drawElement) {
		this.drawElement = drawElement;
		this.setSize(drawElement.getSize());
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
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

	/**
	 * @param pressedDrawElement the pressedDrawElement to set
	 */
	public void setPressedDrawElement(DrawElement pressedDrawElement) {
		this.pressedDrawElement = pressedDrawElement;
	}

	public void setSize(Point size) {
		this.size.set(size);
		this.hitbox.min(this.pos);
		this.hitbox.max(this.pos.clone().add(size));
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

		this.pressed = input.mouseButtons.get(Input.MOUSE_LEFT)
				&& Collider.AABBvsPoint(this.hitbox, input.mousePos);
	}
}
