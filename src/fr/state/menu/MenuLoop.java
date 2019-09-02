package fr.state.menu;

public class MenuLoop implements Runnable {

	private Thread loop;

	private MenuState state;

	public MenuLoop(MenuState state) {
		this.loop = new Thread(this);
		this.state = state;
	}

	@Override
	public void run() {
		while (Thread.currentThread().isAlive()) {
			try {
				Thread.sleep(32);
			} catch (InterruptedException e) {
			}

			this.state.getInput();

			this.state.update();

			this.state.getStatePanel().repaint();
		}
	}

	public void start() {
		this.loop.start();
	}

	public void stop() {
		this.loop.interrupt();
	}
}
