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
	private final static int MIN_SCROLL = 0;
	private final static Color ADVANCED_DRAW_COLOR = new Color(0, 255, 0);
	private final static int PADDING_SLIDER = 4;
	private final static Color TRANSLUCENT = new Color(0, 0, 0, 0);

	private Point pos, size;

	private int paddingTop, paddingBottom, paddingSide;

	private Color scrollBarColor;

	private int scrollPoint;

	private int scrollStep;

	private int maxScroll;

	private List<ScrollWidget> widgets;

	private boolean visible;

	private AABB hitbox;

	private WVSlider scrollBar;

	private boolean scrollBarVisible;

	private boolean leftSide;

	private MenuPage page;

	private DrawElement slider;

	private boolean drawAdvanced;

	public WScroller(MenuPage page) {
		this.pos = new Point();
		this.size = new Point();
		this.scrollPoint = 0;
		this.scrollStep = 0;
		this.maxScroll = 0;
		this.widgets = new ArrayList<>();
		this.visible = true;
		this.scrollBarVisible = true;
		this.leftSide = false;
		this.scrollBarColor = null;
		this.page = page;

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.scrollBar = new WVSlider(page) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				WScroller.this.changeByBar(value);
				System.out.println(value);
			}
		};

		this.slider = this.getDefaultSlider();

		this.initScrollBar();
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

		if (this.drawAdvanced) {
			g.setColor(ADVANCED_DRAW_COLOR);
			g.drawRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
		}

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

	public DERectangle getDefaultSlider() {
		DERectangle rect = new DERectangle();
		rect.setColor(Color.WHITE);
		rect.setSize(new Point(BAR_WIDTH, 50));
		return rect;
	}

	/**
	 * @return the maxScroll
	 */
	public int getMaxScroll() {
		return this.maxScroll;
	}

	public int getPaddingBottom() {
		return this.paddingBottom;
	}

	public int getPaddingSide() {
		return this.paddingSide;
	}

	public int getPaddingTop() {
		return this.paddingTop;
	}

	/**
	 * @return the pos
	 */
	@Override
	public Point getPos() {
		return this.pos;
	}

	public Color getScrollBarColor() {
		return this.scrollBarColor;
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

	private void initScrollBar() {
		DERectangle eBar = new DERectangle();

		eBar.setColor(this.scrollBarColor == null ? TRANSLUCENT : this.scrollBarColor);
		eBar.setSize(new Point((this.scrollBarColor == null ? 0 : this.slider.getSize().x + PADDING_SLIDER) + 2,
				(this.size != null ? this.size.y - this.paddingTop - this.paddingBottom : 0) + 2));
		this.scrollBar.setBar(eBar);

		this.scrollBar.setSlider(this.slider);

		if (!this.leftSide) {
			this.scrollBar.setPos(new Point(this.pos.x + this.size.x - this.slider.getSize().x - this.paddingSide
					- (this.scrollBarColor == null ? 0 : PADDING_SLIDER), this.pos.y + this.paddingTop - 1));
		} else {
			this.scrollBar.setPos(new Point(this.pos.x + this.paddingSide, this.pos.y + this.paddingTop - 1));
		}
		this.scrollBar.setScope(this.maxScroll);
		this.scrollBar.setHitboxFromDrawElement();

		this.scrollBar.setVisible(this.scrollBarVisible);
		this.scrollChanged();
	}

	public boolean isDrawAdvanced() {
		return this.drawAdvanced;
	}

	public boolean isLeftSide() {
		return this.leftSide;
	}

	public boolean isScrollBarVisible() {
		return this.scrollBarVisible;
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

	public void setDrawAdvanced(boolean drawAdvanced) {
		this.drawAdvanced = drawAdvanced;
	}

	/**
	 * @param maxScroll the maxScroll to set
	 */
	public void setMaxScroll(int maxScroll) {
		this.maxScroll = maxScroll;
		this.initScrollBar();
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
		this.initScrollBar();
	}

	public void setPaddingSide(int paddingSide) {
		this.paddingSide = paddingSide;
		this.initScrollBar();
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
		this.initScrollBar();
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

		this.initScrollBar();
	}

	/**
	 * Mettre a null pour desafficher
	 */
	public void setScrollBarColor(Color scrollBarColor) {
		this.scrollBarColor = scrollBarColor;
		this.initScrollBar();
	}

	public void setScrollBarVisible(boolean scrollBarVisible) {
		this.scrollBarVisible = scrollBarVisible;
		this.initScrollBar();
	}

	/**
	 * @param scrollPoint the scrollPoint to set
	 */
	public void setScrollPoint(int scrollPoint) {
		this.scrollPoint = scrollPoint;
		this.scrollBar.setValue(scrollPoint);
		this.scrollChanged();
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

		this.initScrollBar();
	}

	public void setSlider(DrawElement e) {
		if (e != null) {
			this.slider = e;
		}
		this.initScrollBar();
	}

	public void setSliderLeftSide(boolean leftSide) {
		this.leftSide = leftSide;
		this.initScrollBar();
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
		if (!this.visible)
			return;

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

	public static int getBarWidth() {
		return BAR_WIDTH;
	}
}
