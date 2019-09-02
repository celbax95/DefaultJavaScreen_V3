package fr.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

/**
 * Clavier
 */
public class KeyboardEventList implements Keyboard {

	private List<KeyboardEvent> events;

	public KeyboardEventList() {
		this.reset();
	}

	public List<KeyboardEvent> getAndResetEvents() {
		List<KeyboardEvent> tmp = this.events;
		this.events = new Vector<>();
		return tmp;
	}

	public List<KeyboardEvent> getEvents() {
		return this.events;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		this.events.add(new KeyboardEvent(arg0.getKeyCode(), true));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		this.events.add(new KeyboardEvent(arg0.getKeyCode(), false));
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void reset() {
		this.events = new Vector<>();
	}
}
