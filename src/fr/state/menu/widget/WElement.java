package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.point.Point;

public class WElement implements Widget {
	private DrawElement drawElement;

	private MenuPage page;

	public WElement(MenuPage p) {
		this.page = p;
		this.drawElement = null;
	}

	@Override
	public void draw(Graphics2D g) {
		this.drawElement.draw(g, new Point());
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
		return this.drawElement.getPos();
	}

	public Point getSize() {
		return this.drawElement.getSize();
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setDrawElement(DrawElement drawElement) {
		this.drawElement = drawElement;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.drawElement.setPos(pos);
	}

	@Override
	public void update(Input input) {
	}
}
