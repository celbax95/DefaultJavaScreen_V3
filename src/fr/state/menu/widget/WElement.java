package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.logger.Logger;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.point.Point;

public class WElement implements Widget {
	private DrawElement drawElement;

	private Point pos;

	private boolean visible;

	private MenuPage page;

	public WElement(MenuPage p) {
		this.page = p;
		this.pos = new Point();
		this.drawElement = null;
		this.visible = true;
	}

	public WElement(WElement other) {
		this(other == null ? null : other.page);
		if (other == null)
			return;
		this.setDrawElement(other.drawElement.clone());
		this.setPos(other.pos);
		this.setPage(other.page);
	}

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.drawElement == null) {
			Logger.err("Un " + this.getClass().getSimpleName() + " utilise n'a pas de de drawElement");
			return;
		}
		this.drawElement.draw(g, this.pos);
	}

	/**
	 * @return the drawElement
	 */
	public DrawElement getDrawElement() {
		return this.drawElement;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public Point getSize() {
		return this.drawElement.getSize();
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setDrawElement(DrawElement drawElement) {
		if (drawElement != null) {
			drawElement.lock();
		}
		this.drawElement = drawElement;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
	}
}
