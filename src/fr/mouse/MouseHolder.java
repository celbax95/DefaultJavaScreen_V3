package fr.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import fr.util.point.Point;

public class MouseHolder implements MouseInputListener, MouseMotionListener, MouseWheelListener {

	private static MouseManager mouseManager;

	static {
		mouseManager = MouseManager.getInstance();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseManager.setMousePos(new Point(e.getX(), e.getY()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseManager.setMousePos(new Point(e.getX(), e.getY()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseManager.moved(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseManager.pressed(e.getX(), e.getY(), e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseManager.released(e.getX(), e.getY(), e.getButton());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	}
}
