package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.logger.Logger;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.Util;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WVSlider implements Widget {

	private Point pos;

	private AABB hitbox;

	private DrawElement bar;

	private DrawElement slider;

	private int scope;
	private int value; // [0 ; scope]

	private boolean visible;

	private int minY, maxY;

	// Position du slider
	private Point sliderPos;

	private boolean pressed;

	private boolean freeMove;

	private MenuPage page;

	public WVSlider(MenuPage page) {
		super();
		this.pos = new Point();
		this.hitbox = new AABB(this.pos, this.pos, this.pos);
		this.bar = null;
		this.slider = null;
		this.scope = 0;
		this.value = 0;
		this.minY = 0;
		this.maxY = 0;
		this.sliderPos = new Point();
		this.pressed = false;
		this.freeMove = false;
		this.visible = true;
		this.page = page;
	}

	public WVSlider(WVSlider other) {
		this(other == null ? null : other.page);
		if (other == null)
			return;
		this.setPos(new Point(other.pos));

		AABB hb = new AABB(this.pos, new Point(), new Point());
		hb.min(new Point(other.hitbox.min()));
		hb.max(new Point(other.hitbox.max()));

		this.setHitbox(hb);

		this.setBar(other.bar == null ? null : other.bar.clone());
		this.setSlider(other.slider == null ? null : other.slider.clone());
		this.setScope(this.scope);
		this.setValue(0);
		this.setMinY(other.minY);
		this.setMaxY(other.maxY);
		this.setSliderPos(new Point(other.sliderPos.x, this.minY));
		this.setPressed(false);
		this.setFreeMove(other.freeMove);
		this.setPage(other.page);
	}

	private void changeValue(int inputY) {

		// Pour que la souris soit au centre du widget
		inputY -= this.slider.getSize().iy() / 2;

		int yposScope = this.maxY - this.minY;

		// Clamp the value between [minX ; maxX]
		double inputYClamped = Util.clamp(inputY, this.minY, this.maxY);

		// Pourcentage de remplissage
		double fillup = (inputYClamped - this.minY) / yposScope;

		// Valeur entiere uncluse dans [0 ; scope]
		int tmpValue = (int) Math.round(fillup * this.scope);

		// Changement de la position du slider
		if (this.freeMove) {
			this.sliderPos.y = inputYClamped;
		} else if (tmpValue != this.value) {
			this.sliderPos.y = this.minY + Math.round(tmpValue * yposScope / (double) this.scope);
		}

		if (tmpValue != this.value) {
			this.value = tmpValue;
			this.valueChanged(this.value, this.pressed);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.bar == null) {
			Logger.err("Un " + this.getClass().getSimpleName() + " utilise n'a pas de de drawElement \"bar\"");
		} else {
			this.bar.draw(g, this.pos);
		}

		if (this.slider == null) {
			Logger.err("Un " + this.getClass().getSimpleName() + " utilise n'a pas de de drawElement \"slider\"");
		} else {
			this.slider.draw(g, this.sliderPos);
		}
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
	@Override
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

		this.minY = this.pos.iy() + this.bar.getPos().iy();
		this.maxY = this.pos.iy() + this.bar.getPos().iy() + this.bar.getSize().iy() - this.slider.getSize().iy();

		this.sliderPos = new Point(
				this.pos.ix() + this.bar.getPos().ix() - (this.slider.getSize().ix() - this.bar.getSize().ix()) / 2,
				this.minY);

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

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @param bar the bar to set
	 */
	public void setBar(DrawElement bar) {
		if (bar != null) {
			this.bar = bar.clone();
			this.bar.lock();
		}

		if (this.bar != null && this.slider != null) {
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
		int minBX = 0, minSX = 0, maxBX = 0, maxSX = 0;

		Point min = null, max = null;

		minBX = this.pos.ix() + this.bar.getPos().ix();
		minSX = this.sliderPos.ix() + this.slider.getPos().ix();

		min = new Point(minBX <= minSX ? minBX : minSX, this.minY);

		maxBX = minBX + this.bar.getSize().ix();
		maxSX = minSX + this.slider.getSize().ix();

		max = new Point(maxBX >= maxSX ? maxBX : maxSX, this.maxY + this.slider.getSize().iy());

		this.hitbox.min(min);
		this.hitbox.max(max);
	}

	private void setMaxY(int maxX) {
		this.maxY = maxX;
	}

	private void setMinY(int minX) {
		this.minY = minX;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(MenuPage page) {
		this.page = page;
	}

	@Override
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
		if (slider != null) {
			this.slider = slider.clone();
			this.slider.lock();
		}

		if (this.bar != null && slider != null) {
			this.initDrawElements();
		}
	}

	private void setSliderPos(Point sliderPos) {
		this.sliderPos = sliderPos;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		if (value != this.value) {
			this.value = value;

			if (this.slider != null) {
				// moveSlider
				int yposScope = this.maxY - this.minY;
				this.sliderPos.y = this.minY + Math.round(value * yposScope / (double) this.scope);
			}
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
			case MouseEvent.LEFT_RELEASED:
				if (this.pressed) {
					this.setPressed(false);
					this.valueChanged(this.value, this.pressed);
				}
				continue;
			case MouseEvent.MOVE:
				if (this.pressed) {
					this.changeValue(e.pos.iy());
				}
				continue;
			case MouseEvent.LEFT_PRESSED:
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setPressed(true);
					this.changeValue(e.pos.iy());
				}
				continue;
			default:
				break;
			}
		}
	}

	public abstract void valueChanged(int value, boolean pressed);
}
