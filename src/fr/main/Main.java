package fr.main;

import fr.screen.MainJPanel;
import fr.screen.Screen;

public class Main {

	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;

	private static final int MARGE_BOTTOM = 35;
	private static final int MARGE_RIGHT = 6;
	private static final int MARGE_TOTAL = 2;

	public static void main(String[] args) {

		MainJPanel mainJpanel = MainJPanel.getInstance();
		mainJpanel.init(WIDTH, HEIGHT);

		Screen screen = Screen.getInstance();
		screen.init(mainJpanel, MARGE_RIGHT, MARGE_BOTTOM, MARGE_TOTAL);

		try {
			screen.start();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}