package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.InetAddress;
import java.net.UnknownHostException;

import fr.inputs.Input;
import fr.state.game.server.Multiplayer;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class GameState implements IAppState {

	private Game game;

	private StatePanel sp;

	private Input input;

	private GameLoop loop;

	private final int GRAY = 40;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	@Override
	public void draw(Graphics2D g, double dt) {
		this.game.draw(g, dt);
	}

	public void getInput() {
		this.input.getInput();
	}

	@Override
	public String getName() {
		return "game";
	}

	public StatePanel getStatePanel() {
		return this.sp;
	}

	@Override
	public void start(StatePanel panel) {
		this.sp = panel;

		int[] playersID = new int[] { 0, 1 };

		InetAddress groupIP = null;
		try {
			groupIP = InetAddress.getByName("230.0.0.0");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Multiplayer multiplayer = new Multiplayer(groupIP, 10000, playersID);

		this.game = new Game(this, multiplayer, 0, playersID);
		multiplayer.setPDataProcessor(this.game);

		// ImageManager.getInstance().removeAll();

		this.sp.setBackground(this.BACKGROUND);

		this.input = new Input(this.sp.getWinData());

		this.sp.addKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.addKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.addMouseListener(this.input.getMouseEventListener());
		this.sp.addMouseListener(this.input.getMouseMirrorListener());

		multiplayer.receive();

		this.loop = new GameLoop(this);
		this.loop.start();
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
		this.loop.stop();
		this.loop = null;
	}

	public void update(double dt) {
		this.game.resetForces();
		this.game.update(this.input, dt);
	}
}
