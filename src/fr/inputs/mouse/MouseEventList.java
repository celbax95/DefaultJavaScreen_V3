package fr.inputs.mouse;

import java.awt.event.MouseWheelEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import fr.util.point.Point;
import fr.window.WinData;

/**
 * Gestionnaire de souris
 *
 * @author Loic.MACE
 */
public class MouseEventList implements Mouse, Serializable {

	private static final long serialVersionUID = 1L;

	private Point pos;

	private WinData winData;

	private List<MouseEvent> events;

	/**
	 * Private constructor for singleton.
	 */
	public MouseEventList(WinData w) {
		this.pos = new Point();

		this.winData = w;

		this.events = new Vector<>();
	}

	private void addEvent(int id) {
		this.events.add(new MouseEvent(id, new Point(this.pos)));
	}

	public List<MouseEvent> getAndResetEvents() {
		List<MouseEvent> tmp = this.events;
		this.events = new Vector<>();
		return tmp;
	}

	public List<MouseEvent> getEvents() {
		return this.events;
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		this.pos.set(new Point(e.getX(), e.getY()).mult(this.winData.getInvWindowRatio()));
		this.addEvent(MouseEvent.MOVE);
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

		this.addEvent(MouseEvent.MOVE);
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			this.addEvent(MouseEvent.LEFT_PRESSED);
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			this.addEvent(MouseEvent.MIDDLE_PRESSED);
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			this.addEvent(MouseEvent.RIGHT_PRESSED);
			break;
		}
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			this.addEvent(MouseEvent.LEFT_RELEASED);
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			this.addEvent(MouseEvent.MIDDLE_RELEASED);
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			this.addEvent(MouseEvent.RIGHT_RELEASED);
			break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			this.addEvent(MouseEvent.WHEEL_UP);
		} else if (e.getWheelRotation() > 0) {
			this.addEvent(MouseEvent.WHEEL_DOWN);
		}
	}
}
