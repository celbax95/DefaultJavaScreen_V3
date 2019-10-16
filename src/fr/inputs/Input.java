package fr.inputs;

import java.io.Serializable;
import java.util.List;

import fr.inputs.keyboard.Keyboard;
import fr.inputs.keyboard.KeyboardEvent;
import fr.inputs.keyboard.KeyboardEventList;
import fr.inputs.keyboard.KeyboardMirror;
import fr.inputs.mouse.Mouse;
import fr.inputs.mouse.MouseEvent;
import fr.inputs.mouse.MouseEventList;
import fr.inputs.mouse.MouseMirror;
import fr.util.point.Point;
import fr.window.WinData;

public class Input implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MOUSE_LEFT = MouseMirror.LEFT;
	public static final int MOUSE_MIDDLE = MouseMirror.MIDDLE;
	public static final int MOUSE_RIGHT = MouseMirror.RIGHT;

	private KeyboardEventList keyboardEventHolder;
	private MouseEventList mouseEventHolder;

	public List<KeyboardEvent> keyboardEvents;

	public List<MouseEvent> mouseEvents;

	private KeyboardMirror keyboard;
	private MouseMirror mouse;

	public List<Boolean> keyboardKeys;

	public List<Boolean> mouseButtons;

	public Point mousePos;

	public int mouseWheel;

	public Input(WinData w) {
		this.keyboardEventHolder = new KeyboardEventList();
		this.mouseEventHolder = new MouseEventList(w);

		this.keyboardEvents = null;
		this.mouseEvents = null;

		this.keyboard = new KeyboardMirror();
		this.mouse = new MouseMirror(w);

		this.mousePos = new Point();

		this.keyboardKeys = null;
		this.mouseButtons = null;

		this.mouseWheel = 0;
	}

	public Input getInput() {
		this.keyboardEvents = this.keyboardEventHolder.getAndResetEvents();

		this.mouseEvents = this.mouseEventHolder.getAndResetEvents();
		this.keyboardKeys = this.keyboard.getKeys();
		this.mouseButtons = this.mouse.getButtons();

		this.mouseWheel = this.mouse.getDWheel();

		this.mousePos = this.mouse.getPos();

		return this;
	}

	public Keyboard getKeyboardEventListener() {
		return this.keyboardEventHolder;
	}

	public Keyboard getKeyboardMirrorListener() {
		return this.keyboard;
	}

	public Mouse getMouseEventListener() {
		return this.mouseEventHolder;
	}

	public Mouse getMouseMirrorListener() {
		return this.mouse;
	}
}
