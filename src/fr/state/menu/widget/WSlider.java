package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.Util;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WSlider implements Widget {

	private Point pos;

	private AABB hitbox;

	private DrawElement bar;

	private DrawElement slider;

	private int scope;
	private int value; // [0 ; scope]

	private int minX, maxX;

	// Position du slider
	private Point sliderPos;

	private boolean pressed;

	private boolean freeMove;

	private MenuPage page;

	public WSlider(MenuPage p) {
		this.pos = new Point();

		this.scope = 0;

		this.pressed = false;

		this.sliderPos = new Point();

		this.freeMove = false;

		this.page = null;
	}

	private void changeValue(int inputX) {

		int xposScope = this.maxX - this.minX;

		// Clamp the value between [minX ; maxX]
		double inputXClamped = Util.clamp(inputX, this.minX, this.maxX);

		// Pourcentage de remplissage
		double fillup = (inputXClamped - this.minX) / xposScope;

		// Valeur entiere uncluse dans [0 ; scope]
		this.value = (int) Math.round(fillup * this.scope);

		// Changement de la position du slider
		if (this.freeMove) {
			this.sliderPos.x = inputXClamped;
		} else {
			this.sliderPos.x = this.minX + Math.round(fillup * xposScope);
		}

		this.valueChanged(this.value);
	}

	@Override
	public void draw(Graphics2D g) {
		this.bar.draw(g, this.pos);

		this.slider.draw(g, this.pos.clone().add(this.sliderPos));
	}

	private void initDrawElements() {
		if (this.bar == null || this.slider == null)
			return;

		this.minX = this.pos.ix() + this.bar.getPos().ix();
		this.maxX = this.pos.ix() + this.bar.getPos().ix() + this.bar.getSize().ix()
				- this.slider.getSize().ix();

		this.sliderPos = new Point(this.minX, this.pos.iy() + this.bar.getPos().iy()
				- (this.slider.getSize().iy() - this.bar.getSize().iy()) / 2);
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return this.pressed;
	}

	/**
	 * @param bar the bar to set
	 */
	public void setBar(DrawElement bar) {
		this.bar = bar;

		if (this.slider != null) {
			this.initDrawElements();
		}
	}

	public void setHitboxFromDrawElement() {
		int minBY = 0, minSY = 0, maxBY = 0, maxSY = 0;

		Point min = null, max = null;

		minBY = this.pos.iy() + this.bar.getPos().iy();
		minSY = this.pos.iy() + this.sliderPos.iy() + this.slider.getPos().iy();

		min = new Point(this.minX, minBY <= minSY ? minBY : minSY);

		maxBY = minBY + this.bar.getSize().iy();
		maxSY = minSY + this.slider.getSize().iy();

		max = new Point(this.maxX, maxBY >= maxSY ? maxBY : maxSY);

		this.hitbox.min(min);
		this.hitbox.max(max);
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	/**
	 * @param pressed the pressed to set
	 */
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public void setSlider(DrawElement slider) {
		this.slider = slider;

		if (this.bar != null) {
			this.initDrawElements();
		}
	}

	@Override
	public void update(Input input) {
		for (MouseEvent e : input.mouseEvents) {
			switch (e.id) {
			case MouseEvent.LEFT_RELEASED:
				this.setPressed(false);
				continue;
			case MouseEvent.MOVE:
				if (this.pressed) {
					this.changeValue(e.pos.ix());
				}
				continue;
			case MouseEvent.LEFT_PRESSED:
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setPressed(true);
					this.changeValue(e.pos.ix());
				}
				continue;
			default:
				break;
			}
		}
	}

	public abstract void valueChanged(int value);
}
