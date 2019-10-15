package fr.state.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class MenuState implements IAppState {

	private Menu m;

	private StatePanel sp;

	private Input input;

	private MenuLoop loop;

	private final int GRAY = 40;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	@Override
	public void draw(Graphics2D g, double dt) {
		this.m.draw(g);
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

		ImageManager.getInstance().removeAll();

		this.m = new Menu(this);
		this.m.applyDefautPage();

		this.sp.setBackground(this.BACKGROUND);

		this.input = new Input(this.sp.getWinData());

		this.sp.addKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.addKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.addMouseListener(this.input.getMouseEventListener());
		this.sp.addMouseListener(this.input.getMouseMirrorListener());

		this.loop = new MenuLoop(this);
		this.loop.start();
	}

	@Override
	public void stop() {
		this.sp.removeKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.removeKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.removeMouseListener(this.input.getMouseEventListener());
		this.sp.removeMouseListener(this.input.getMouseMirrorListener());
		this.m = null;
		this.sp = null;
		this.input = null;
		this.loop.stop();
		this.loop = null;
	}

	public void update() {
		this.m.update(this.input);
	}
}
