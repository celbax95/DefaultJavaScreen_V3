package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.point.Point;

public class WLabel implements Widget {
	private Point pos;

	private TextData textData;

	private MenuPage page;

	public WLabel(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.textData = null;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(this.textData.color);
		g.setFont(this.textData.font);
		g.drawString(this.textData.text, this.pos.ix(), this.pos.iy());
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public TextData getTextData() {
		return this.textData;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public void setTextData(TextData textData) {
		this.textData = textData;
	}

	@Override
	public void update(Input input) {
	}
}
