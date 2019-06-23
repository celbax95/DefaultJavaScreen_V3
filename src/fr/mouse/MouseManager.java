package fr.mouse;

import fr.init.ConfInitializer;
import fr.sigmanager.SignalManager;
import fr.sigmanager.ThreadManager;
import fr.util.point.Point;

/**
 * Gestionnaire de souris
 *
 * @author Loic.MACE
 */
public class MouseManager implements Mouse {

	private static final int TIME_MOVING = 120;

	/**
	 * static Singleton instance.
	 */
	private static volatile MouseManager instance;

	// Signal
	private Object pressedSignal;

	// Signal
	private Object releasedSignal;

	// Signal
	private Object movedSignal;

	// Signal
	private Object wheelSignal;

	private final String pressedSignalName = "mouse_pressed", releasedSignalName = "mouse_released",
			movedSignalName = "mouse_moved", wheelSignalName = "mouse_wheel";

	private Point pos;

	private boolean inWindow;

	private boolean[] buttons;

	private Integer lastButton;

	private boolean movingTesterRunning;

	private boolean moving;

	private int wheelUp;

	private int wheelDown;

	private final String movingTester = "mouse_movingtester";

	/**
	 * Private constructor for singleton.
	 */
	private MouseManager() {
		SignalManager sm = SignalManager.getInstance();

		this.pressedSignal = sm.addSignal(this.pressedSignalName);
		this.releasedSignal = sm.addSignal(this.releasedSignalName);
		this.movedSignal = sm.addSignal(this.movedSignalName);
		this.wheelSignal = sm.addSignal(this.wheelSignalName);

		this.pos = new Point();

		this.moving = false;
		this.movingTesterRunning = false;
		this.wheelUp = 0;
		this.wheelDown = 0;

		this.lastButton = 0;

		this.buttons = new boolean[3];
		for (int i = 0; i < this.buttons.length; i++) {
			this.buttons[i] = false;
		}

		this.startMovingTester();
	}

	/**
	 * Gere les actions a effectuer quand la souris rentre dans la fenetre
	 */
	protected void enteredWindow() {
		this.inWindow = true;
		this.startMovingTester();
	}

	/**
	 * Gere les actions a effectuer quand la souris sort dans la fenetre
	 */
	protected void exitedWindow() {
		this.inWindow = false;
	}

	@Override
	public boolean[] getButtons() {
		return new boolean[] { this.buttons[0], this.buttons[1], this.buttons[2] };
	}

	@Override
	public int getLastButton() {
		return this.lastButton;
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
		return this.wheelDown;
	}

	@Override
	public Object getWheelSignal() {
		return this.wheelSignal;
	}

	@Override
	public int getWheelUp() {
		return this.wheelUp * -1;
	}

	/**
	 * Arrete le Thread cree dans la methode isMovingTester
	 */
	private void interruptMovingTester() {
		ThreadManager.getInstance().remove(this.movingTester);
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

	/**
	 * Test si la souris est en train de bouger
	 *
	 * @see isMoving
	 */
	private void isMovingTester() {
		if (this.movingTesterRunning)
			return;
		this.movingTesterRunning = true;

		ThreadManager tm = ThreadManager.getInstance();

		Thread mT = new Thread(new Runnable() {
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
		});
		tm.add("mousemanager_movingtester", mT);
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

	/**
	 * Gere les actions a effectuer quand la souris bouge
	 *
	 * @param x : coordonnee de la souris
	 * @param y : coordonnee de la souris
	 */
	protected void moved(int x, int y) {
		this.pos = new Point(x, y);
		synchronized (this.movedSignal) {
			this.movedSignal.notifyAll();
		}
	}

	/**
	 * Gere les actions a effectuer quand un bouton de la souris est presse
	 *
	 * @param x      : coordonnee de la souris
	 * @param y      : coordonnee de la souris
	 * @param button : bouton presse
	 */
	protected void pressed(int x, int y, int button) {
		this.pos = new Point(x, y);

		if (button == 0)
			return;

		this.buttons[button - 1] = true;

		synchronized (this.lastButton) {
			this.lastButton = button - 1;
		}

		synchronized (this.pressedSignal) {
			this.pressedSignal.notifyAll();
		}
	}

	/**
	 * Gere les actions a effectuer quand un bouton de la souris est relache
	 *
	 * @param x      : coordonnee de la souris
	 * @param y      : coordonnee de la souris
	 * @param button : bouton relache
	 */
	protected void released(int x, int y, int button) {
		this.pos = new Point(x, y);

		if (button == 0)
			return;

		this.buttons[button - 1] = false;

		synchronized (this.lastButton) {
			this.lastButton = button - 1;
		}

		synchronized (this.releasedSignal) {
			this.releasedSignal.notifyAll();
		}
	}

	/**
	 * Lance le testeur servant a mettre a jour le statut 'en mouvement' de la
	 * souris
	 */
	private void startMovingTester() {
		this.isMovingTester();
	}

	/**
	 * Gere les actions a effectuer quand la molette de la souris est actionnee
	 *
	 * @param rotation : sens et cran de rotation [1 ; -1]
	 */
	protected void wheelMoved(int rotation) {
		if (rotation == 0) {
			this.wheelUp = 0;
			this.wheelDown = 0;
		} else if (rotation >= 0) {
			this.wheelUp = 0;
			this.wheelDown = rotation;
		} else {
			this.wheelDown = 0;
			this.wheelUp = rotation;
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

	/**
	 * Methode de test des evennements
	 */
	public static void main(String[] args) {

		ConfInitializer.getInstance().start();

		// Pression des boutons
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

		// Boutons relache
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

		// Molette actionnee
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
						System.out.print(" Up ");
					}
					if (mm.getWheelDown() == 1) {
						System.out.print(" Down ");
					}

					System.out.println("}");
				}
			}
		}).start();

		// Souris deplacee
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
