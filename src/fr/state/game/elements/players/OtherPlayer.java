package fr.state.game.elements.players;

import fr.inputs.Input;

public class OtherPlayer extends Player {

	private static final long serialVersionUID = 1L;

	private final int id;

	public OtherPlayer(int id, double sizeUnit) {
		super(sizeUnit);
		this.id = id;
	}

	@Override
	public void update(Input input, double dt) {
	}
}
