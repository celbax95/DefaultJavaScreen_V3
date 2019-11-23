package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.state.game.elements.collider.Shape;
import fr.state.game.elements.onscreen.GOTag;
import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public abstract class Player extends GameObject {

	private static final long serialVersionUID = 1L;

	protected static final double MAX_SPEED = 6;

	private static final Color DEFAULT_COLOR = Color.RED;

	protected final int id;

	protected Color color;

	protected Player(int id, double sizeUnit) {
		super(sizeUnit);
		this.id = id;
		this.init();
	}

	protected Player(int id, Point pos, Point size, double sizeUnit, double scale) {
		super(pos, size, sizeUnit, scale);
		this.id = id;
		this.init();
	}

	@Override
	public void draw(Graphics2D g, double dt) {
		g.setColor(this.color);
		Point dtPos = this.getInterpolatedPos(dt);

		g.fillRect(dtPos.ix(), dtPos.iy(), this.size.ix(), this.size.iy());
	}

	public Color getColor() {
		return this.color;
	}

	public int getPlayerId() {
		return this.id;
	}

	@Override
	public Shape getShape() {
		return null;
	}

	private void init() {
		this.color = DEFAULT_COLOR;
		this.addTags(GOTag.PLAYER);
		this.setMaxSpeed(MAX_SPEED * this.sizeUnit);
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}