package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public class Player {

	private static final Color COLOR = Color.RED;

	private static final int ACCEL = 10, MAX_SPEED = 300;

	private Point pos, size;

	private Point forces;

	private Color color;

	public Player() {
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();

		this.color = COLOR;
	}

	private void applyForces() {

		// speed
		int tmpSpeed = (int) Math.round(this.forces.length());
		if (tmpSpeed > MAX_SPEED) {
			this.forces.mult((double) MAX_SPEED / tmpSpeed);
		}

		// move
		this.pos.add(this.forces);

		// reset
		this.forces.set(0, 0);
	}

	public void draw(Graphics2D g) {
		g.setColor(this.color);
		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	private Point getMoveFromInput(boolean up, boolean down, boolean left, boolean right) {
		Point move = new Point(0, 0);

		if (up ^ down) {
			move.y(up ? -ACCEL : ACCEL);
		}

		if (left ^ right) {
			move.x(left ? -ACCEL : ACCEL);
		}

		return move;
	}

	/**
	 * @return the pos
	 */
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.size;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size;
	}

	public void update(Input input) {
		Point move = this.getMoveFromInput(input.keyboardKeys.get(90), input.keyboardKeys.get(83),
				input.keyboardKeys.get(81), input.keyboardKeys.get(68));

		move.mult(ACCEL);

		this.forces.add(move);

		this.applyForces();
	}
}
