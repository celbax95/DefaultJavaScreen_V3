package fr.main;

import fr.appli.appli.Appli;
import fr.screen.Screen;

public class Main {

	public static final int HEIGHT = 768;
	private static final int MARGE_H = 35;
	private static final int MARGE_T = 2;
	private static final int MARGE_W = 6;

	public static final int WIDTH = 1366;

	Screen scr;

	public static void main(String[] args) {
		Screen.create(new Appli(), WIDTH, HEIGHT, MARGE_W, MARGE_H, MARGE_T);
	}
}