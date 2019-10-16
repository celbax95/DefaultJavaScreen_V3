package fr.inputs.mouse;

import java.io.Serializable;

import fr.util.point.Point;

public class MouseEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int LEFT_PRESSED = 0;
	public static final int LEFT_RELEASED = 1;
	public static final int MIDDLE_PRESSED = 2;
	public static final int MIDDLE_RELEASED = 3;

	public static final int RIGHT_PRESSED = 4;
	public static final int RIGHT_RELEASED = 5;

	public static final int MOVE = 6;
	public static final int WHEEL_UP = 7;
	public static final int WHEEL_DOWN = 8;
	public int id;
	public Point pos;

	public MouseEvent(int id, Point pos) {
		super();
		this.id = id;
		this.pos = pos;
	}
}
