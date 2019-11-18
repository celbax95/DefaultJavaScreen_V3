package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PDataFactory;
import fr.util.point.Point;

public class MyPlayer implements Serializable, Player {

	private static final long serialVersionUID = 1L;

	private static final Color COLOR = Color.RED;

	private static final int ACCEL = 100, MAX_SPEED = 1000;

	private final int id;

	private Point pos, size;

	private Point forces;

	private Color color;

	private Multiplayer multiplayer;
	private PDataFactory pDataFactory;

	public MyPlayer(int id, Multiplayer multiplayer) {
		this.id = id;
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();
		this.color = COLOR;
		this.multiplayer = multiplayer;
		this.pDataFactory = multiplayer.getPDataFactory();
	}

	@Override
	public void addForce(Point f) {
		this.forces.add(f);
	}

	@Override
	public void applyForces(double dt) {

		if (this.forces.x == 0 && this.forces.y == 0)
			return;

		// speed
		int tmpSpeed = (int) Math.round(this.forces.length());
		if (tmpSpeed > MAX_SPEED) {
			this.forces.mult((double) MAX_SPEED / tmpSpeed);
		}

		// move
		this.setPos(this.pos.add(this.forces.mult(dt)));
	}

	@Override
	public void draw(Graphics2D g, double dt) {
		g.setColor(this.color);
		Point dtPos = this.forces.clone().mult(dt).add(this.pos);
//		g.fillRect(dtPos.ix(), dtPos.iy(), this.size.ix(), this.size.iy());

		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	@Override
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

	@Override
	public Point getPos() {
		return this.pos;
	}

	@Override
	public Point getSize() {
		return this.size;
	}

	@Override
	public void resetForces() {
		this.forces.set(0, 0);
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void setPos(Point pos) {
		this.pos.set(pos);
		this.multiplayer.send(this.pDataFactory.createMove(this.id, pos));
	}

	@Override
	public void setSize(Point size) {
		this.size = size;
	}

	@Override
	public void update(Input input) {
		Point move = this.getMoveFromInput(input.keyboardKeys.get(90), input.keyboardKeys.get(83),
				input.keyboardKeys.get(81), input.keyboardKeys.get(68));

		move.mult(ACCEL);

		this.addForce(move);
	}
}
