package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PDataFactory;
import fr.state.game.elements.collider.AABB;
import fr.state.game.elements.collider.Shape;
import fr.state.game.elements.onscreen.GOTag;
import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public class InputReactivePlayer extends Player {

	private static final long serialVersionUID = 1L;

	private static double ACCEL = 18; // tiles / secondes
	private static double JUMP_FORCE = 20;

	private static double TIME_TO_REJUMP = 300; // ms

	private static double DRAG = 10;

	private static int MAX_JUMPS = 2;

	private double nextJump;

	private int availableJumps;

	private Multiplayer multiplayer;
	private PDataFactory pDataFactory;

	public InputReactivePlayer(int id, Multiplayer multiplayer, double sizeUnit) {
		super(id, sizeUnit);
		this.addTags(GOTag.GRAVITY);
		this.multiplayer = multiplayer;
		this.pDataFactory = multiplayer.getPDataFactory();
		this.resetJumps();
	}

	private Point getMoveFromInput(boolean left, boolean right, boolean jump, double dt) {
		Point move = new Point(0, 0);

		final double scaling = this.getScaling();

		if (left ^ right) {
			move.x((left ? -1 : 1) * ACCEL * dt * scaling);
		}

		if (jump && this.availableJumps > 0) {
			long time = System.currentTimeMillis();
			if (time >= this.nextJump) {
				if (this.velocity.y > 0) {
					this.velocity.y = 0;
				}
				move.y(-JUMP_FORCE * scaling);
				this.availableJumps--;
				this.nextJump = time + TIME_TO_REJUMP;
			}
		}
		return move;
	}

	@Override
	public Shape getShape() {
		AABB hb = new AABB(this.pos, this.pos, this.pos.clone().add(this.size));

		return hb;
	}

	@Override
	public void interractWith(GameObject other) {
		if (other.hasTag(GOTag.GRIP_SURFACE)) {

			Point relativeVelocity = this.velocity.clone().sub(other.getVelocity());

			if (this.pos.y + this.size.y > other.getPos().y
					&& this.pos.y + this.size.y < other.getPos().y + other.getSize().y && relativeVelocity.y > 0) {
				this.resetJumps();
			}
		}
	}

	@Override
	public boolean isAffectedBy(GameObject other) {
		return false == other.hasTag(GOTag.PLAYER);
	}

	@Override
	public void move(double dt) {
		super.move(dt);
		if (this.velocity.equals(new Point()) == false) {
			this.multiplayer.send(this.pDataFactory.createMove(this.id, this.pos));
		}
	}

	private void resetJumps() {
		this.availableJumps = MAX_JUMPS;
		this.nextJump = 0;
	}

	@Override
	public void setPos(Point pos) {
		super.setPos(pos);
		this.multiplayer.send(this.pDataFactory.createMove(this.id, this.pos));
	}

	@Override
	public void update(Input input, double dt) {
		super.update(input, dt);

		Point move = this.getMoveFromInput(input.keyboardKeys.get(81), input.keyboardKeys.get(68),
				input.keyboardKeys.get(32), dt);

		this.addForces(move);

		Point dragInv = this.velocity.clone().trigNorm().mult(DRAG * dt * this.sizeUnit);
		Point drag = new Point(-dragInv.x, -dragInv.y);

		if (Math.abs(this.velocity.x) + Math.abs(drag.x) <= 0) {
			this.velocity.x = 0;
			drag.x = 0;
		}
		if (Math.abs(this.velocity.y) + Math.abs(drag.y) <= 0) {
			this.velocity.y = 0;
			drag.y = 0;
		}

		this.addForces(drag);
	}
}
