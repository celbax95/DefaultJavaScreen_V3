package fr.state.menu.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import fr.inputs.Input;
import fr.inputs.mouse.MouseEvent;
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

		@Override
		public Point getPos() {
			return this.w.getPos();
		}

		@Override
		public boolean isVisible() {
			return this.w.isVisible();
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

	private Point pos, size;

	private int scrollPoint;

	private int scrollStep;

	private int minScroll, maxScroll;

	private List<ScrollWidget> widgets;

	private boolean visible;

	private AABB hitbox;

	private DERectangle bar;

	public WScroller() {
		this.pos = new Point();
		this.size = new Point();
		this.scrollPoint = 0;
		this.scrollStep = 10;
		this.minScroll = 0;
		this.maxScroll = 500;
		this.widgets = new ArrayList<>();
		this.visible = true;

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.bar = new DERectangle();
		this.bar.setPos(new Point(-BAR_WIDTH, 0));
		this.bar.setSize(new Point(BAR_WIDTH, 60));
		this.bar.setColor(Color.red);
	}

	public void add(Widget w) {
		if (w != null && !this.widgets.contains(w)) {
			Point wPos = w.getPos().clone();
			wPos.add(this.pos);
			w.setPos(wPos);
			this.widgets.add(new ScrollWidget(w));
		}
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
		int maxY = this.size.iy() - this.bar.getSize().iy();

		int y = (int) ((double) this.scrollPoint / (double) this.maxScroll * maxY);

		this.bar.draw(g, new Point(this.pos.x + this.size.x, this.pos.y + y));
	}

	/**
	 * @return the maxScroll
	 */
	public int getMaxScroll() {
		return this.maxScroll;
	}

	/**
	 * @return the minScroll
	 */
	public int getMinScroll() {
		return this.minScroll;
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
	 * @param minScroll the minScroll to set
	 */
	public void setMinScroll(int minScroll) {
		this.minScroll = minScroll;
	}

	/**
	 * @param pos the pos to set
	 */
	@Override
	public void setPos(Point pos) {
		this.pos.set(pos);
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
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {

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
					if (this.scrollPoint - this.scrollStep >= this.minScroll) {
						this.scrollPoint -= this.scrollStep;
					} else {
						this.scrollPoint = this.minScroll;
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
