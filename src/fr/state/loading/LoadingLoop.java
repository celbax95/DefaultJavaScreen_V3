package fr.state.loading;

public class LoadingLoop implements Runnable {

	private Thread loop;

	private LoadingState state;

	public LoadingLoop(LoadingState state) {
		this.loop = new Thread(this);
		this.loop.setName("LoadingLoop/loop");
		this.state = state;
	}

	@Override
	public void run() {
		while (!this.loop.isInterrupted()) {
			synchronized (this) {
				try {
					if (this.loop.isInterrupted()) {
						break;
					}

					this.state.update();

					if (this.state != null && this.state.getStatePanel() != null) {
						this.state.getStatePanel().repaint();
					}

					Thread.sleep(32);
				} catch (InterruptedException | NullPointerException e) {
					// e.printStackTrace();
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
