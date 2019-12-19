package fr.state.game.elements.players;

import fr.inputs.Input;
import fr.state.game.elements.collider.AABB;
import fr.state.game.elements.collider.Shape;
import fr.state.game.elements.onscreen.GOTag;
import fr.state.game.elements.onscreen.GameObject;

public class DummyPlayer extends Player {

	private static final long serialVersionUID = 1L;

	public DummyPlayer(int id, double sizeUnit) {
		super(id, sizeUnit);
	}

	@Override
	public Shape getShape() {
		return new AABB(this.pos, this.pos, this.pos.clone().add(this.size));
	}

	@Override
	public boolean isAffectedBy(GameObject other) {
		return false == other.hasTag(GOTag.PLAYER);
	}

	@Override
	public void update(Input input, double dt) {
		super.update(input, dt);
	}
}
