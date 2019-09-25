package fr.inputs.keyboard;

public class KeyboardEvent {

	public int key;

	public char keyChar;

	public boolean pressed;

	public boolean shift;
	public boolean ctrl;
	public boolean alt;

	public KeyboardEvent(int key, char keyChar, boolean shift, boolean ctrl, boolean alt, boolean pressed) {
		this.key = key;
		this.keyChar = keyChar;
		this.shift = shift;
		this.ctrl = ctrl;
		this.alt = alt;
		this.pressed = pressed;
	}
}
