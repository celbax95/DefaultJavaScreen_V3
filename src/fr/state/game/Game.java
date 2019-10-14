package fr.state.game;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public class Game {

	private GameState gameState;

	private Player player;

	public Game(GameState gameState) {
		this.gameState = gameState;
		this.player = new Player();
		this.player.setPos(new Point(200, 200));
	}

	public void draw(Graphics2D g) {
		this.player.draw(g);
	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void setMenuState(GameState gameState) {
		this.gameState = gameState;
	}

	public void update(Input input) {
		this.player.update(input);
	}
}
