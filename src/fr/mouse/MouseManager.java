package fr.mouse;

import fr.util.point.Point;

public class MouseManager {

	private static final int TIME_MOVING = 120;
	private static final int TIME_WHEEL_RESET = 50;

	/**
	 * static Singleton instance.
	 */
	private static volatile MouseManager instance;

	private Object pressedSignal;

	private Object releasedSignal;

	private Object movedSignal;

	private Object wheelSignal;

	private Point pos;

	private boolean inWindow;

	private boolean[] buttons;

	private Thread mT;

	private boolean movingTesterRunning;

	private boolean moving;

	private int wheelUp;

	private int wheelDown;

	/**
	 * Private constructor for singleton.
	 */
	private MouseManager() {
		this.pressedSignal = new Object();
		this.releasedSignal = new Object();
		this.movedSignal = new Object();
		this.wheelSignal = new Object();

		this.pos = new Point();

		this.moving = false;
		this.movingTesterRunning = false;
		this.wheelUp = 0;
		this.wheelDown = 0;

		this.buttons = new boolean[4];
		for (int i = 0; i < this.buttons.length; i++) {
			this.buttons[i] = false;
		}

		this.startMovingTester();
	}

	public boolean[] getButtons() {
		return this.buttons;
	}

	public Object getMovedSignal() {
		return this.movedSignal;
	}

	public Point getPos() {
		return this.pos;
	}

	public Object getPressedSignal() {
		return this.pressedSignal;
	}

	public Object getReleasedSignal() {
		return this.releasedSignal;
	}

	public int getWheelDown() {
		return this.wheelDown;
	}

	public Object getWheelSignal() {
		return this.wheelSignal;
	}

	public int getWheelUp() {
		return this.wheelUp;
	}

	public void interruptMovingTester() {
		try {
			this.mT.interrupt();
		} catch (Exception e) {
		}
	}

	public boolean isMoving() {
		return this.moving;
	}

	public void isMovingTester() {
		if (this.movingTesterRunning)
			return;
		this.movingTesterRunning = true;

		(this.mT = new Thread(new Runnable() {
			@Override
			public void run() {
				Point pos = MouseManager.this.getPos();

				while (!Thread.currentThread().isInterrupted()) {
					try {
						synchronized (MouseManager.this.movedSignal) {
							MouseManager.this.movedSignal.wait();
						}
						MouseManager.this.moving = true;
						do {
							pos = MouseManager.this.getPos();
							Thread.sleep(TIME_MOVING);
							if (!MouseManager.this.inWindow) {
								MouseManager.this.movingTesterRunning = false;
								return;
							}
						} while (!pos.equals(MouseManager.this.pos));
						MouseManager.this.moving = false;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		})).start();
	}

	public boolean isPressed() {
		for (Boolean b : this.buttons) {
			if (b)
				return true;
		}
		return false;
	}

	public void moved(int x, int y) {
		this.pos = new Point(x, y);
		synchronized (this.movedSignal) {
			this.movedSignal.notifyAll();
		}
	}

	public void startMovingTester() {
		this.isMovingTester();
	}

	protected void enteredWindow() {
		this.inWindow = true;
		this.startMovingTester();
	}

	protected void exitedWindow() {
		this.inWindow = false;
	}

	protected void pressed(int x, int y, int button) {
		this.pos = new Point(x, y);

		this.buttons[button] = true;

		synchronized (this.pressedSignal) {
			this.pressedSignal.notifyAll();
		}
	}

	protected void released(int x, int y, int button) {
		this.pos = new Point(x, y);

		this.buttons[button] = false;

		synchronized (this.releasedSignal) {
			this.releasedSignal.notifyAll();
		}
	}

	protected void wheelMoved(int rotation) {
		if (rotation == 0) {
			this.wheelUp = 0;
			this.wheelDown = 0;
		} else if (rotation >= 0) {
			this.wheelDown = 0;
			this.wheelUp = rotation;
		} else {
			this.wheelUp = 0;
			this.wheelDown = rotation;
		}
		synchronized (this.wheelSignal) {
			this.wheelSignal.notifyAll();
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
