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

	public WSlider(MenuPage page) {
		super();
		this.pos = new Point();
		this.hitbox = new AABB(this.pos, this.pos, this.pos);
		this.bar = null;
		this.slider = null;
		this.scope = 0;
		this.value = 0;
		this.minX = 0;
		this.maxX = 0;
		this.sliderPos = new Point();
		this.pressed = false;
		this.freeMove = false;
		this.page = page;
	}

	private void changeValue(int inputX) {

		// Pour que la souris soit au centre du widget
		inputX -= this.slider.getSize().ix() / 2;

		int xposScope = this.maxX - this.minX;

		// Clamp the value between [minX ; maxX]
		double inputXClamped = Util.clamp(inputX, this.minX, this.maxX);

		// Pourcentage de remplissage
		double fillup = (inputXClamped - this.minX) / xposScope;

		// Valeur entiere uncluse dans [0 ; scope]
		int tmpValue = (int) Math.round(fillup * this.scope);

		// Changement de la position du slider
		if (this.freeMove) {
			this.sliderPos.x = inputXClamped;
		} else if (tmpValue != this.value) {
			this.sliderPos.x = this.minX + Math.round(tmpValue * xposScope / (double) this.scope);
		}

		if (tmpValue != this.value) {
			this.value = tmpValue;
			this.valueChanged(this.value);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		this.bar.draw(g, this.pos);

		this.slider.draw(g, this.sliderPos);
	}

	/**
	 * @return the bar
	 */
	public DrawElement getBar() {
		return this.bar;
	}

	/**
	 * @return the hitbox
	 */
	public AABB getHitbox() {
		return this.hitbox;
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
	 * @return the scope
	 */
	public int getScope() {
		return this.scope;
	}

	/**
	 * @return the slider
	 */
	public DrawElement getSlider() {
		return this.slider;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
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
	 * @return the freeMove
	 */
	public boolean isFreeMove() {
		return this.freeMove;
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

	/**
	 * @param freeMove the freeMove to set
	 */
	public void setFreeMove(boolean freeMove) {
		this.freeMove = freeMove;
	}

	/**
	 * @param hitbox the hitbox to set
	 */
	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setHitboxFromDrawElement() {
		int minBY = 0, minSY = 0, maxBY = 0, maxSY = 0;

		Point min = null, max = null;

		minBY = this.pos.iy() + this.bar.getPos().iy();
		minSY = this.pos.iy() + this.sliderPos.iy() + this.slider.getPos().iy();

		min = new Point(this.minX, minBY <= minSY ? minBY : minSY);

		maxBY = minBY + this.bar.getSize().iy();
		maxSY = minSY + this.slider.getSize().iy();

		max = new Point(this.maxX + this.slider.getSize().ix(), maxBY >= maxSY ? maxBY : maxSY);

		this.hitbox.min(min);
		this.hitbox.max(max);
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);

		this.initDrawElements();
	}

	/**
	 * @param pressed the pressed to set
	 */
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(int scope) {
		this.scope = scope;
	}

	public void setSlider(DrawElement slider) {
		this.slider = slider;

		if (this.bar != null) {
			this.initDrawElements();
		}
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
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
