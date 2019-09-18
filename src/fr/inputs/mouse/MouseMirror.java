package fr.inputs.mouse;

import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.Vector;

import fr.util.point.Point;
import fr.window.WinData;

/**
 * Gestionnaire de souris
 *
 * @author Loic.MACE
 */
public class MouseMirror implements Mouse {

	public static final int BUTTONS_AMOUNT = 3;

	public static final int LEFT = 0;
	public static final int MIDDLE = 1;
	public static final int RIGHT = 2;

	private WinData winData;

	private Point pos;

	private Vector<Boolean> mouseButtons;

	private int wheelMove;

	public MouseMirror(WinData w) {
		this.pos = new Point();

		this.mouseButtons = new Vector<>();
		this.mouseButtons.setSize(BUTTONS_AMOUNT);

		this.winData = w;

		for (int i = 0; i < BUTTONS_AMOUNT; i++) {
			this.mouseButtons.set(i, false);
		}

		this.wheelMove = 0;
	}

	public List<Boolean> getButtons() {
		return new Vector<>(this.mouseButtons);
	}

	public int getDWheel() {
		int tmp = this.wheelMove;

		this.wheelMove = 0;

		return tmp;
	}

	public Point getPos() {
		return new Point(this.pos);
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		this.pos.set(new Point(e.getX(), e.getY()).mult(this.winData.getInvWindowRatio()));
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		this.pos.set(new Point(e.getX(), e.getY()).mult(this.winData.getInvWindowRatio()));
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			this.mouseButtons.set(LEFT, true);
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			this.mouseButtons.set(MIDDLE, true);
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			this.mouseButtons.set(RIGHT, true);
			break;
		}
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			this.mouseButtons.set(LEFT, false);
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			this.mouseButtons.set(MIDDLE, false);
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			this.mouseButtons.set(RIGHT, false);
			break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			this.wheelMove--;
		} else if (e.getWheelRotation() > 0) {
			this.wheelMove++;
		}
	}
}
