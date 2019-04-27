package fr.screen.keyboard;

import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyBoard implements KeyListener {

	private static List<Integer> keys = new LinkedList<Integer>();
	private static boolean released = true;

	public void keyPressed(KeyEvent e) {
		if (!keys.contains(e.getKeyCode()))
			keys.add(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		keys.remove((Object) e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {
	}

	public static int getKey(int i) {
		return keys.get(i);
	}

	public static boolean isPressed() {
		released = false;
		return !(keys.isEmpty());
	}

	public static boolean isPressed(int key) {
		released = false;
		return keys.contains(key);
	}

	public static boolean tap() {
		if (!(keys.isEmpty()) && released)
			return true;
		else if ((keys.isEmpty()) && !released)
			released = true;
		return false;
	}
}
