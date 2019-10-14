package fr.state.game;

public class GameLoop implements Runnable {

	private Thread loop;

	private GameState state;

	public GameLoop(GameState state) {
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
					e.printStackTrace();
					Thread.currentThread().interrupt();
					System.exit(0);
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
