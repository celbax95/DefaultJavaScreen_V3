package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public class WScroller implements Widget {

	private class ScrollWidget implements Widget {
		private Point originalPos;
		private Widget w;

		public ScrollWidget(Widget w) {
			this.w = w;
			this.originalPos = w.getPos().clone();
		}

		@Override
		public void draw(Graphics2D g) {
			this.w.draw(g);
		}

		/**
		 * @return the originalPos
		 */
		public Point getOriginalPos() {
			return this.originalPos;
		}

		@Override
		public Point getPos() {
			return this.w.getPos();
		}

		@Override
		public boolean isVisible() {
			return this.w.isVisible();
		}

		/**
		 * @param originalPos the originalPos to set
		 */
		public void setOriginalPos(Point originalPos) {
			this.originalPos = originalPos;
		}

		@Override
		public void setPos(Point pos) {
			this.w.setPos(pos);
		}

		public void setScrollPoint(Point scrollPoint) {
			this.w.setPos(this.originalPos.clone().sub(scrollPoint));
		}

		@Override
		public void setVisible(boolean visible) {
			this.w.setVisible(visible);
		}

		@Override
		public void update(Input input) {
			this.w.update(input);
		}
	}

	private final static int BAR_WIDTH = 20;
	private final static int VERTICAL_PADDING = 3;
	private final static int RIGHT_PADDING = 0;
	private final static int MIN_SCROLL = 0;
	private Point pos, size;

	private int scrollPoint;

	private int scrollStep;

	private int maxScroll;

	private List<ScrollWidget> widgets;

	private boolean visible;

	private AABB hitbox;

	private WVSlider scrollBar;

	private MenuPage page;

	public WScroller(MenuPage page) {
		this.pos = new Point();
		this.size = new Point();
		this.scrollPoint = 0;
		this.scrollStep = 0;
		this.maxScroll = 0;
		this.widgets = new ArrayList<>();
		this.visible = true;
		this.page = page;

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.scrollBar = new WVSlider(page) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				WScroller.this.changeByBar(value);
			}
		};

		this.initSlider();
	}

	public void add(Widget w) {
		if (w != null && !this.widgets.contains(w)) {
			Point wPos = w.getPos().clone();
			wPos.add(this.pos);
			w.setPos(wPos);
			this.widgets.add(new ScrollWidget(w));
		}
	}

	private void changeByBar(int value) {
		this.scrollPoint = value;
		this.scrollChanged();
	}

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		g.setColor(new Color(0, 255, 0, 30));
		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());

		Shape oldClip = g.getClip();

		g.clip(new Rectangle(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy()));

		for (Widget widget : this.widgets) {
			widget.draw(g);
		}

		g.setClip(oldClip);
		this.drawBar(g);
	}

	private void drawBar(Graphics2D g) {
		this.scrollBar.draw(g);
	}

	/**
	 * @return the maxScroll
	 */
	public int getMaxScroll() {
		return this.maxScroll;
	}

	/**
	 * @return the pos
	 */
	@Override
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the scrollPoint
	 */
	public int getScrollPoint() {
		return this.scrollPoint;
	}

	/**
	 * @return the scrollStep
	 */
	public int getScrollStep() {
		return this.scrollStep;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.size;
	}

	private void initSlider() {
		DERectangle eBar = new DERectangle();

		eBar.setColor(Color.WHITE);
		eBar.setSize(new Point(0, this.size != null ? this.size.y - VERTICAL_PADDING * 2 : 0));
		this.scrollBar.setBar(eBar);

		DERectangle eSlider = new DERectangle();

		eSlider.setColor(Color.RED);
		eSlider.setSize(new Point(BAR_WIDTH, 50));
		this.scrollBar.setSlider(eSlider);

		this.scrollBar.setPos(
				new Point(this.pos.x + this.size.x - BAR_WIDTH / 2 - RIGHT_PADDING, this.pos.y + VERTICAL_PADDING));
		this.scrollBar.setScope(this.maxScroll);
		this.scrollBar.setHitboxFromDrawElement();
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	public void remove(Widget w) {
		ScrollWidget sw = new ScrollWidget(w);
		if (this.widgets.contains(sw)) {
			this.widgets.remove(sw);
		}
	}

	private void scrollChanged() {
		this.scrollBar.setValue(this.scrollPoint);
		for (ScrollWidget widget : this.widgets) {
			widget.setScrollPoint(new Point(0, this.scrollPoint));
		}
	}

	/**
	 * @param maxScroll the maxScroll to set
	 */
	public void setMaxScroll(int maxScroll) {
		this.maxScroll = maxScroll;
	}

	/**
	 * @param pos the pos to set
	 */
	@Override
	public void setPos(Point pos) {
		Point vectToNewPos = pos.clone().sub(this.pos);
		this.pos.set(pos);

		for (ScrollWidget sw : this.widgets) {
			sw.setOriginalPos(sw.getOriginalPos().add(vectToNewPos));
			sw.setPos(sw.getPos().add(vectToNewPos));
		}

		this.initSlider();
	}

	/**
	 * @param scrollPoint the scrollPoint to set
	 */
	public void setScrollPoint(int scrollPoint) {
		this.scrollPoint = scrollPoint;
	}

	/**
	 * @param scrollStep the scrollStep to set
	 */
	public void setScrollStep(int scrollStep) {
		this.scrollStep = scrollStep;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size;
		this.hitbox.max(this.pos.clone().add(size));

		this.initSlider();
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
		this.scrollBar.update(input);

		if (Collider.AABBvsPoint(this.hitbox, input.mousePos)) {
			for (Widget widget : this.widgets) {
				widget.update(input);
			}
			int oldScroll = this.scrollPoint;
			for (MouseEvent e : input.mouseEvents) {
				switch (e.id) {
				case MouseEvent.WHEEL_DOWN:
					oldScroll = this.scrollPoint;
					if (this.scrollPoint + this.scrollStep <= this.maxScroll) {
						this.scrollPoint += this.scrollStep;
					} else {
						this.scrollPoint = this.maxScroll;
					}
					if (oldScroll != this.scrollPoint) {
						this.scrollChanged();
					}
					continue;
				case MouseEvent.WHEEL_UP:
					oldScroll = this.scrollPoint;
					if (this.scrollPoint - this.scrollStep >= WScroller.MIN_SCROLL) {
						this.scrollPoint -= this.scrollStep;
					} else {
						this.scrollPoint = WScroller.MIN_SCROLL;
					}
					if (oldScroll != this.scrollPoint) {
						this.scrollChanged();
					}
					continue;
				}
			}
		}
	}
}
