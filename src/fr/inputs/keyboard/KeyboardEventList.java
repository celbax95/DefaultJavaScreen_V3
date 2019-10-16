package fr.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Clavier
 */
public class KeyboardEventList implements Keyboard, Serializable {

	private static final long serialVersionUID = 1L;

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

	private char getValidKeyChar(char keyChar) {
		return Character.isDefined(keyChar) ? keyChar : Character.MIN_VALUE;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		this.events.add(new KeyboardEvent(arg0.getKeyCode(), this.getValidKeyChar(arg0.getKeyChar()),
				arg0.isShiftDown(), arg0.isControlDown(), arg0.isAltDown(), true));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		this.events.add(new KeyboardEvent(arg0.getKeyCode(), this.getValidKeyChar(arg0.getKeyChar()),
				arg0.isShiftDown(), arg0.isControlDown(), arg0.isAltDown(), false));
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void reset() {
		this.events = new Vector<>();
	}
}
