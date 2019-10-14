package fr.state.game;

import java.awt.Graphics2D;

import fr.inputs.Input;

public class Game {

	private GameState gameState;

	public Game(GameState gameState) {
		this.gameState = gameState;
	}

	public void draw(Graphics2D g) {

	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void setMenuState(GameState gameState) {
		this.gameState = gameState;
	}

	public void update(Input input) {

	}
}
