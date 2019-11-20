package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PDataFactory;
import fr.util.point.Point;

public class InputReactivePlayer extends Player {

	private static final long serialVersionUID = 1L;

	private static double ACCEL = 4;

	private Multiplayer multiplayer;
	private PDataFactory pDataFactory;

	public InputReactivePlayer(int id, Multiplayer multiplayer, double sizeUnit) {
		super(id, sizeUnit);
		this.multiplayer = multiplayer;
		this.pDataFactory = multiplayer.getPDataFactory();
	}

	@Override
	public void applyForces(double dt) {
		if (this.forces.equals(new Point()) == false) {
			super.applyForces(dt);

			this.multiplayer.send(this.pDataFactory.createMove(this.id, this.pos));
		}
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
	public void update(Input input, double dt) {
		Point move = this.getMoveFromInput(input.keyboardKeys.get(90), input.keyboardKeys.get(83),
				input.keyboardKeys.get(81), input.keyboardKeys.get(68));

		move.mult(InputReactivePlayer.ACCEL * this.getScaling());

		this.addForces(move);
	}
}
