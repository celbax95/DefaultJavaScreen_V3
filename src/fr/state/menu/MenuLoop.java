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
		while (!this.loop.isInterrupted()) {
			synchronized (this) {
				try {
					this.state.getInput();

					this.state.update();

					this.state.getStatePanel().repaint();

					Thread.sleep(32);
				} catch (InterruptedException | NullPointerException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void start() {
		this.loop.start();
	}

	public void stop() {
		synchronized (this) {
			this.loop.interrupt();
		}
	}
}
