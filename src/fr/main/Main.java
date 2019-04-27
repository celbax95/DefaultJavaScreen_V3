package fr.main;

import fr.panelstates.MenuState;
import fr.repainter.DefaultRepainter;
import fr.statepanel.AppStateManager;
import fr.statepanel.StatePanel;
import fr.window.Window;

public class Main {

	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;

	private static final int MARGE_BOTTOM = 35;
	private static final int MARGE_RIGHT = 6;
	private static final int MARGE_TOTAL = 2;

	public static void main(String[] args) {

		AppStateManager stator = AppStateManager.getInstance();
		stator.addState(new MenuState());

		DefaultRepainter repainter = new DefaultRepainter();

		StatePanel mainPanel = StatePanel.getInstance();
		mainPanel.init(WIDTH, HEIGHT, repainter);

		stator.setStatable(mainPanel);

		Window screen = Window.getInstance();
		screen.init(mainPanel, MARGE_RIGHT, MARGE_BOTTOM, MARGE_TOTAL);

		stator.applyState("menu");

		screen.start();
	}
}