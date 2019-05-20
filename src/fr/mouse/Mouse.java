package fr.mouse;

import fr.util.point.Point;

public interface Mouse {
	Object getMovedSignal();

	Point getPos();

	Object getPressedSignal();

	Object getReleasedSignal();

	int getWheelDown();

	Object getWheelSignal();

	int getWheelUp();

	void interruptThreads();

	boolean isLeftClickPressed();

	boolean isMiddleClickPressed();

	boolean isMoving();

	boolean isPressed();

	boolean isRightClickPressed();
}
