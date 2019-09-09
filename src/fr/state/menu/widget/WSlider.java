package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WSlider implements Widget {

	private Point pos;

	private AABB hitbox;

	private Point barPos, sliderPos;

	private int minX, maxX;

	private DrawElement bar;

	private DrawElement slider;

	private int scope;
	private int value; // [0 ; scope]

	private int inputXClamped;

	private boolean pressed;

	private MenuPage page;

	public WSlider(MenuPage p) {
		this.pos = new Point();

		this.scope = 0;

		this.pressed = false;

		this.inputXClamped = 0;

		// Positions par defaut
		this.barPos = null;

		this.sliderPos = null;

		this.page = null;
	}

	private Point calcBarPos() {

	}

	private Point calcSliderPos() {

	}

	private void changeValue(int inputXClamped) {

	}

	private int clampX(int x) {

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
	 * @param bar the bar to set
	 */
	public void setBar(DrawElement bar) {
		this.bar = bar;

		if (this.slider == null) {
			this.barPos = new Point();

			this.minX = this.pos.ix() + bar.getPos().ix();
			this.maxX = this.pos.ix() + bar.getPos().ix() + bar.getSize().ix();
		} else {
			int dy = (this.slider.getSize().iy() - bar.getSize().iy()) / 2;

			this.barPos = this.pos.clone();

			this.pos.y -= dy;
			this.barPos.y += dy;

			this.minX = this.pos.ix() + bar.getPos().ix();
			this.maxX = this.pos.ix() + bar.getPos().ix() + bar.getSize().ix() - this.slider.getSize().ix();
		}
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

	public void setSlider(DrawElement slider) {
		this.slider = slider;

		if (this.bar == null) {
			this.sliderPos = new Point();
		} else {
			this.setBar(this.bar);

			this.sliderPos = this.barPos.clone();



			this.sliderPos.y -=
		}
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
