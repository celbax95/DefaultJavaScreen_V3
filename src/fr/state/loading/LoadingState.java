package fr.state.loading;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;

import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.logger.Logger;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class LoadingState implements IAppState {

	private StatePanel sp;

	private Input input;

	private LoadingLoop loop;

	private final int GRAY = 40;

	private Map<String, Object> initData;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	public LoadingState() {
		this.initData = null;
	}

	@Override
	public void draw(Graphics2D g) {
	}

	public void getInput() {
		this.input.getInput();
	}

	@Override
	public String getName() {
		return "loading";
	}

	public StatePanel getStatePanel() {
		return this.sp;
	}

	@Override
	public void setInitData(Map<String, Object> data) {
		this.initData = data;
	}

	@Override
	public void start(StatePanel panel) {

		if (this.initData == null) {
			Logger.err("Les donnees d'initialisation n'ont pas ete affectees");
			System.exit(0);
		}

		this.sp = panel;

		ImageManager.getInstance().removeAll();

		this.sp.setBackground(/* this.BACKGROUND */Color.pink);

		this.input = new Input(this.sp.getWinData());

		this.loop = new LoadingLoop(this);
		this.loop.start();
	}

	@Override
	public void stop() {
		this.sp.removeKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.removeKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.removeMouseListener(this.input.getMouseEventListener());
		this.sp.removeMouseListener(this.input.getMouseMirrorListener());
		this.sp = null;
		this.input = null;
		this.loop.stop();
		this.loop = null;
	}

	@Override
	public void update() {
	}
}
