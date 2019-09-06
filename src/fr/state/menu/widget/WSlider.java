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
	private Point size;

	private AABB hitbox;

	// La position de la barre est calculee par cette classe
	// Seule sa taille importe
	private DrawElement bar;

	// La position du slider est calculee par cette classe
	// Seule sa taille importe
	private DrawElement slider;

	private int scope; // [1 : 100]
	private int value; // [0 ; scope]

	private int inputXClamped;

	private boolean pressed;

	private MenuPage page;

	public WSlider(MenuPage p) {
		this.pos = new Point();
		this.size = new Point();

		this.scope = 0;

		this.pressed = false;

		this.inputXClamped = 0;

		this.page = null;
	}

	private Point calcBarHitboxSize() {
		this.bar.setPos(this.calcBarPos());

		return new Point(this.size.x - this.slider.getSize().x, this.bar.getSize().y);
	}

	private Point calcBarPos() {
		if (this.slider != null)
			return new Point(this.pos.x + this.slider.getSize().x / 2,
					this.pos.y + (this.size.y - this.bar.getSize().y) / 2);
		else
			return new Point(this.pos.x, this.pos.y + (this.size.y - this.bar.getSize().y) / 2);
	}

	public AABB calcHitbox() {
		double sliderH = this.slider.getSize().y;

		double sliderY = this.pos.y + (this.size.y - sliderH) / 2;

		return new AABB(this.pos, new Point(this.pos.x, sliderY),
				new Point(this.pos.x + this.size.x, sliderY + sliderH));
	}

	private Point calcSliderPos() {
		return new Point(this.inputXClamped - this.slider.getSize().x / 2,
				this.pos.x + (this.size.y - this.slider.getSize().y) / 2);
	}

	private void changeValue(int inputXClamped) {
		int x = inputXClamped - this.bar.getPos().ix();

		int barW = this.size.ix() - this.slider.getSize().iy();

		this.value = x * this.scope / barW;

		this.valueChanged(this.value);
	}

	private int clampX(int x) {
		int sliderWidth = this.slider.getSize().iy();

		int barX = this.pos.ix() + sliderWidth / 2;

		int barW = this.size.ix() - sliderWidth;

		return (int) Util.clamp(x, barX, barX + barW);
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.bar == null || this.slider == null)
			return;

		this.bar.draw(g);

		this.slider.setPos(this.calcSliderPos());
		this.slider.draw(g);
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
	 * @return the inputXClamped
	 */
	public int getInputXClamped() {
		return this.inputXClamped;
	}

	/**
	 * @return the page
	 */
	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the scope
	 */
	public int getScope() {
		return this.scope;
	}

	public DrawElement getSlider() {
		return this.slider;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
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
		Point expectedSize = this.calcBarHitboxSize();

		if (!bar.getSize().equals(expectedSize)) {
			System.err.println("Attention : La largeur de la barre n'est pas valide.\n"
					+ "Cela peut mener a des problemes d'affichage.\n"
					+ "Taille de la barre / taille attendue : " + bar.getSize().ix() + " / "
					+ expectedSize.iy());
		}

		bar.setPos(this.calcBarPos());

		this.bar = bar;
	}

	/**
	 * @param inputXClamped the inputXClamped to set
	 */
	public void setInputXClamped(int inputXClamped) {
		this.inputXClamped = inputXClamped;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos = pos;

		if (this.bar != null) {
			this.bar.setPos(this.calcBarPos());
		}

		if (this.slider != null) {
			this.slider.setPos(this.calcSliderPos());
		}
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

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size;
	}

	public void setSlider(DrawElement slider) {
		slider.setPos(this.calcSliderPos());

		this.slider = slider;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public void update(Input input) {
		Integer inputX = null;

		for (MouseEvent e : input.mouseEvents) {

			// out of
			switch (e.id) {
			case MouseEvent.LEFT_RELEASED:
				this.pressed = false;
				break;
			case MouseEvent.MOVE:
				if (this.pressed) {
				}
				break;
			default:
				break;
			}

			// on
			if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
				switch (e.id) {
				case MouseEvent.LEFT_PRESSED:
					this.pressed = true;
					inputX = e.pos.ix();
					break;
				default:
					break;
				}
			}
		}
		if (inputX != null) {
			this.inputXClamped = this.clampX(inputX);
			this.changeValue(this.inputXClamped);
		}
	}

	public abstract void valueChanged(int value);
}
