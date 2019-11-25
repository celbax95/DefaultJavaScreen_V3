package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.state.game.elements.collider.AABB;
import fr.state.game.elements.collider.Shape;

public class DummyPlayer extends Player {

	private static final long serialVersionUID = 1L;

	public DummyPlayer(int id, double sizeUnit) {
		super(id, sizeUnit);
	}

	@Override
	public Shape getShape() {
		return new AABB(this.pos, this.pos, this.size);
	}

	@Override
	public void update(Input input, double dt) {
		super.update(input, dt);
	}
}
