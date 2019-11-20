package fr.state.game.elements.players;

import fr.inputs.Input;

public class DummyPlayer extends Player {

	private static final long serialVersionUID = 1L;

	public DummyPlayer(int id, double sizeUnit) {
		super(id, sizeUnit);
	}

	@Override
	public void update(Input input, double dt) {
	}
}
