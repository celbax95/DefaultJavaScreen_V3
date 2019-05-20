package fr.mouse;

import fr.init.ConfInitializer;
import fr.util.point.Point;

public class MouseManager implements Mouse {

	private static final int TIME_MOVING = 120;

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

		this.buttons = new boolean[3];
		for (int i = 0; i < this.buttons.length; i++) {
			this.buttons[i] = false;
		}

		this.startMovingTester();
	}

	@Override
	public Object getMovedSignal() {
		return this.movedSignal;
	}

	@Override
	public Point getPos() {
		return this.pos;
	}

	@Override
	public Object getPressedSignal() {
		return this.pressedSignal;
	}

	@Override
	public Object getReleasedSignal() {
		return this.releasedSignal;
	}

	@Override
	public int getWheelDown() {
		return this.wheelDown * -1;
	}

	@Override
	public Object getWheelSignal() {
		return this.wheelSignal;
	}

	@Override
	public int getWheelUp() {
		return this.wheelUp;
	}

	@Override
	public void interruptThreads() {
		this.interruptMovingTester();
	}

	@Override
	public boolean isLeftClickPressed() {
		return this.buttons[0];
	}

	@Override
	public boolean isMiddleClickPressed() {
		return this.buttons[1];
	}

	@Override
	public boolean isMoving() {
		return this.moving;
	}

	@Override
	public boolean isPressed() {
		for (Boolean b : this.buttons) {
			if (b)
				return true;
		}
		return false;
	}

	@Override
	public boolean isRightClickPressed() {
		return this.buttons[2];
	}

	private void interruptMovingTester() {
		try {
			this.mT.interrupt();
		} catch (Exception e) {
		}
	}

	private void isMovingTester() {
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

	private void startMovingTester() {
		this.isMovingTester();
	}

	protected void enteredWindow() {
		this.inWindow = true;
		this.startMovingTester();
	}

	protected void exitedWindow() {
		this.inWindow = false;
	}

	protected void moved(int x, int y) {
		this.pos = new Point(x, y);
		synchronized (this.movedSignal) {
			this.movedSignal.notifyAll();
		}
	}

	protected void pressed(int x, int y, int button) {
		this.pos = new Point(x, y);

		if (button == 0)
			return;

		this.buttons[button - 1] = true;

		synchronized (this.pressedSignal) {
			this.pressedSignal.notifyAll();
		}
	}

	protected void released(int x, int y, int button) {
		this.pos = new Point(x, y);

		if (button == 0)
			return;

		this.buttons[button - 1] = false;

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

	public static void main(String[] args) {

		ConfInitializer.getInstance().start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				MouseManager mm = MouseManager.getInstance();

				Object s = mm.getPressedSignal();
				while (true) {

					synchronized (s) {
						try {
							s.wait();
						} catch (InterruptedException e) {
						}
					}

					System.out.print("Pressed : {");

					if (mm.isLeftClickPressed()) {
						System.out.print(" Left ");
					}
					if (mm.isMiddleClickPressed()) {
						System.out.print("Middle ");
					}
					if (mm.isRightClickPressed()) {
						System.out.print("Right ");
					}

					System.out.println("}");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				MouseManager mm = MouseManager.getInstance();

				Object s = mm.getReleasedSignal();
				while (true) {

					synchronized (s) {
						try {
							s.wait();
						} catch (InterruptedException e) {
						}
					}

					System.out.print("Pressed : {");

					if (mm.isLeftClickPressed()) {
						System.out.print(" Left ");
					}
					if (mm.isMiddleClickPressed()) {
						System.out.print("Middle ");
					}
					if (mm.isRightClickPressed()) {
						System.out.print("Right ");
					}

					System.out.println("}");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				MouseManager mm = MouseManager.getInstance();

				Object s = mm.getWheelSignal();
				while (true) {

					synchronized (s) {
						try {
							s.wait();
						} catch (InterruptedException e) {
						}
					}

					System.out.print("Wheel : {");

					if (mm.getWheelUp() == 1) {
						System.out.print(" Down ");
					}
					if (mm.getWheelDown() == 1) {
						System.out.print(" Up ");
					}

					System.out.println("}");
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				MouseManager mm = MouseManager.getInstance();

				Object s = mm.getMovedSignal();
				while (true) {

					synchronized (s) {
						try {
							s.wait();
						} catch (InterruptedException e) {
						}
					}

					Point p = mm.getPos();

					System.out.println("\t\t\tPosition : { " + p.x() + " : " + p.y() + "}");
				}
			}
		}).start();
	}
}
