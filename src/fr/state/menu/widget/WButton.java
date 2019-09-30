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

public abstract class WButton implements Widget {
	private Point pos;

	private AABB hitbox;

	private DrawElement stdDrawElement;

	private DrawElement pressedDrawElement;

	private DrawElement cantPressDrawElement;

	private DrawElement currentDE;

	private boolean pressed;

	private boolean mouseOn;

	private boolean canPressed;

	private boolean visible;

	private MenuPage page;

	public WButton(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.canPressed = true;

		this.visible = true;

		this.stdDrawElement = null;

		this.pressedDrawElement = null;

		this.pressed = false;

		this.mouseOn = false;

		this.currentDE = null;
	}

	public WButton(WButton other) {
		this(other == null ? null : other.page);
		if (other == null)
			return;
		this.setPos(other.pos);
		AABB hb = new AABB(this.pos, new Point(), new Point());
		hb.min(new Point(other.hitbox.min()));
		hb.max(new Point(other.hitbox.max()));

		this.setHitbox(hb);

		this.setStdDrawElement(other.stdDrawElement.clone());
		this.setPressedDrawElement(other.pressedDrawElement.clone());
		this.setCanPressed(other.canPressed);
		this.setPage(other.page);
	}

	public abstract void action();

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.currentDE != null) {
			this.currentDE.draw(g, this.pos);
		} else {
			Logger.err("Un " + this.getClass().getSimpleName() + " n'a pas de drawElement");
		}
	}

	public DrawElement getCantPressDrawElement() {
		return this.cantPressDrawElement;
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

	/**
	 * @return the mouseOn
	 */
	public boolean isMouseOn() {
		return this.mouseOn;
	}

	public boolean isPressed() {
		return this.pressed;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	public void setCanPressed(boolean canPressed) {
		if (canPressed != this.canPressed) {
			this.canPressed = canPressed;
			this.setPressed(false);
			this.setCurrentDE();
		}
	}

	public void setCantPressDrawElement(DrawElement cantPressDrawElement) {
		this.cantPressDrawElement = cantPressDrawElement;
	}

	private void setCurrentDE() {
		if (!this.canPressed && this.cantPressDrawElement != null) {
			this.currentDE = this.cantPressDrawElement;
			return;
		}

		// Changement du drawElement courant

		if (this.pressedDrawElement == null)
			return;

		this.currentDE = this.pressed && this.mouseOn ? this.pressedDrawElement : this.stdDrawElement;
	}

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

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	public void setPressed(boolean pressed) {
		if (pressed != this.pressed) {
			this.pressed = pressed;
			this.setCurrentDE();
		}
	}

	/**
	 * @param pressedDrawElement the pressedDrawElement to set
	 */
	public void setPressedDrawElement(DrawElement pressedDrawElement) {
		if (pressedDrawElement != null) {
			this.pressedDrawElement = pressedDrawElement.clone();
			this.pressedDrawElement.lock();
		}
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setStdDrawElement(DrawElement drawElement) {
		if (drawElement != null) {
			this.stdDrawElement = drawElement.clone();
			this.stdDrawElement.lock();

			if (this.currentDE == null) {
				this.currentDE = this.stdDrawElement;
			}
		}
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
		if (!this.canPressed || !this.visible)
			return;

		for (MouseEvent e : input.mouseEvents) {
			switch (e.id) {
			case MouseEvent.LEFT_PRESSED:
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setPressed(true);
				}
				continue;
			case MouseEvent.LEFT_RELEASED:
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					if (this.pressed) {
						this.action();
					}
				}
				this.setPressed(false);
				continue;
			}
		}
		this.setMouseOn(Collider.AABBvsPoint(this.hitbox, input.mousePos));
	}
}
