package fr.mouse;

import fr.util.point.Point;

public class MouseManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile MouseManager instance;

	private Object mousePressed;

	private Object mouseReleased;

	private Object mouseMoved;

	private Point mousePos;

	private Point lastPressedPos;

	private Point lastReleasedPos;

	private Boolean[] buttons;

	/**
	 * Private constructor for singleton.
	 */
	private MouseManager() {
		this.mousePressed = new Object();
		this.mouseReleased = new Object();
		this.mouseMoved = new Object();

		this.buttons = new Boolean[3];
	}

	public Boolean[] getButtons() {
		return this.buttons;
	}

	public Point getLastPressedPos() {
		return this.lastPressedPos;
	}

	public Point getLastReleasedPos() {
		return this.lastReleasedPos;
	}

	public Object getMouseMoved() {
		return this.mouseMoved;
	}

	public Point getMousePos() {
		return this.mousePos;
	}

	public Object getMousePressed() {
		return this.mousePressed;
	}

	public Object getMouseRealeased() {
		return this.mouseReleased;
	}

	public void setButtons(Boolean[] buttons) {
		this.buttons = buttons;
	}

	public void setLastClickPos(Point lastClickPos) {
		this.lastPressedPos = lastClickPos;
	}

	public void setLastPressedPos(Point lastPressedPos) {
		this.lastPressedPos = lastPressedPos;
	}

	public void setLastReleasedPos(Point lastReleasedPos) {
		this.lastReleasedPos = lastReleasedPos;
	}

	public void setMousePos(Point mousePos) {
		this.mousePos = mousePos;
		synchronized (this.mouseMoved) {
			this.mouseMoved.notifyAll();
		}
	}

	/**
	 * Return a singleton instance of MouseManager.
	 */
	public static MouseManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (MouseManager.class) {
				if (instance == null) {
					instance = new MouseManager();
				}
			}
		}
		return instance;
	}

}
