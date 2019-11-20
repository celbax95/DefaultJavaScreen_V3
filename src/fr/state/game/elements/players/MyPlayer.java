package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PDataFactory;
import fr.util.point.Point;

public class MyPlayer extends Player {

	private static final long serialVersionUID = 1L;

	private static double ACCEL = 4;

	private final int id;

	private Multiplayer multiplayer;
	private PDataFactory pDataFactory;

	public MyPlayer(int id, Multiplayer multiplayer, double sizeUnit) {
		super(sizeUnit);
		this.id = id;
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
	public void setPos(Point pos) {
		super.setPos(pos);
		this.multiplayer.send(this.pDataFactory.createMove(this.id, pos));
	}

	@Override
	public void update(Input input, double dt) {
		Point move = this.getMoveFromInput(input.keyboardKeys.get(90), input.keyboardKeys.get(83),
				input.keyboardKeys.get(81), input.keyboardKeys.get(68));

		move.mult(MyPlayer.ACCEL * this.sizeUnit);

		this.addForces(move);
	}
}
