package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class GameState implements IAppState {

	private Game game;

	private StatePanel sp;

	private Input input;

	private final int GRAY = 40;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	@Override
	public void draw(Graphics2D g) {
		this.game.draw(g);
	}

	public void getInput() {
		this.input.getInput();
	}

	@Override
	public String getName() {
		return "menu";
	}

	public StatePanel getStatePanel() {
		return this.sp;
	}

	@Override
	public void start(StatePanel panel) {
		this.sp = panel;

		this.game = new Game(this);

		// ImageManager.getInstance().removeAll();

		this.sp.setBackground(this.BACKGROUND);

		this.input = new Input(this.sp.getWinData());

		this.sp.addKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.addKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.addMouseListener(this.input.getMouseEventListener());
		this.sp.addMouseListener(this.input.getMouseMirrorListener());
	}

	@Override
	public void stop() {
		this.sp.removeKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.removeKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.removeMouseListener(this.input.getMouseEventListener());
		this.sp.removeMouseListener(this.input.getMouseMirrorListener());
		this.game = null;
		this.sp = null;
		this.input = null;
	}

	public void update() {
		this.game.update(this.input);
	}
}
