package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PDataFactory;
import fr.state.game.elements.collider.AABB;
import fr.state.game.elements.collider.Shape;
import fr.util.point.Point;

public class InputReactivePlayer extends Player {

	private static final long serialVersionUID = 1L;

	private static double ACCEL = 1;

	private static double DRAG = 0.5;

	private Multiplayer multiplayer;
	private PDataFactory pDataFactory;

	public InputReactivePlayer(int id, Multiplayer multiplayer, double sizeUnit) {
		super(id, sizeUnit);
		this.multiplayer = multiplayer;
		this.pDataFactory = multiplayer.getPDataFactory();
	}

	private Point getMoveFromInput(boolean up, boolean down, boolean left, boolean right) {
		Point move = new Point(0, 0);

		if (up ^ down) {
			move.y(up ? -1 : 1);
		}

		if (left ^ right) {
			move.x(left ? -1 : 1);
		}

		return move.trigNorm();
	}

	@Override
	public Shape getShape() {
		AABB hb = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		return hb;
	}

	@Override
	public void move(double dt) {
		super.move(dt);
		if (this.velocity.equals(new Point()) == false) {
			this.multiplayer.send(this.pDataFactory.createMove(this.id, this.pos));
		}
	}

	@Override
	public void update(Input input, double dt) {
		super.update(input, dt);

		Point move = this.getMoveFromInput(input.keyboardKeys.get(90), input.keyboardKeys.get(83),
				input.keyboardKeys.get(81), input.keyboardKeys.get(68));

		move.mult(InputReactivePlayer.ACCEL * this.getScaling());

		this.addForces(move);

		Point dragInv = this.velocity.clone().trigNorm().mult(DRAG * this.sizeUnit);
		Point drag = new Point(-dragInv.x, -dragInv.y);

		if (Math.abs(this.velocity.x) - Math.abs(drag.x) <= 0) {
			this.velocity.x = 0;
			drag.x = 0;
		}
		if (Math.abs(this.velocity.y) - Math.abs(drag.y) <= 0) {
			this.velocity.y = 0;
			drag.y = 0;
		}

		this.addForces(drag);
	}
}
