package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.state.game.elements.collider.Shape;
import fr.state.game.elements.onscreen.GOTag;
import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public abstract class Player extends GameObject {

	private static final long serialVersionUID = 1L;

	protected static final double MAX_HORIZONTAL_SPEED = 6;

	protected static final double MAX_DOWN_SPEED = 10;

	protected static final double MAX_UP_SPEED = 20;

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
	public void applyForces() {
		super.applyForces();

		final double scaling = this.getScaling();
		final double maxHorizontalSpeed = MAX_HORIZONTAL_SPEED * scaling;
		final double maxDownSpeed = MAX_DOWN_SPEED * scaling;
		final double maxUpSpeed = MAX_UP_SPEED * scaling;

		if (Math.abs(this.velocity.x) > maxHorizontalSpeed) {
			this.velocity.x = Math.signum(this.velocity.x) * maxHorizontalSpeed;
		}

		if (this.velocity.y > maxDownSpeed) {
			this.velocity.y = maxDownSpeed;
		} else if (this.velocity.y < -maxUpSpeed) {
			this.velocity.y = -maxUpSpeed;
		}
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
		// infinite maxSpeed
		this.setMaxSpeed(-1);
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}