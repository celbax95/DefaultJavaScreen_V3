package fr.state.game;

public class GameLoop implements Runnable {

	private static int MS_TO_SLEEP = 2; // > 0

	private static double getDt(double t) {
		return 1000 / t;
	}

	private final double fps;
	private final double updates;

	private final double dtFps;
	private final double dtUpdates;

	private double accuFps;
	private double accuUpdate;

	private double lastUpdate;

	double lastFrame;

	private Thread loop;

	private GameState state;

	public GameLoop(GameState state) {
		this.fps = 60;
		this.updates = 30;
		this.dtFps = getDt(this.fps);
		this.accuFps = 0;
		this.dtUpdates = getDt(this.updates);
		this.accuUpdate = 0;
		this.lastUpdate = this.time();
		this.loop = new Thread(this);
		this.state = state;
		this.lastFrame = this.time();
	}

	public double getDtUpdate() {
		return this.updates / 1000.;
	}

	@Override
	public void run() {

		double currentTime = this.time();

		double elapsed = currentTime - this.lastFrame;

		while (!this.loop.isInterrupted()) {
			synchronized (this) {
				try {
					// init
					currentTime = this.time();

					elapsed = currentTime - this.lastFrame;

					this.accuUpdate += elapsed;
					this.accuFps += elapsed;

					if (this.accuFps < this.dtFps && this.accuUpdate < this.dtUpdates) {

						double sleepFps = this.dtFps - this.accuFps;
						double sleepUpdate = this.dtUpdates - this.accuUpdate;

						long sleepTime = (long) Math.floor(Math.min(sleepFps, sleepUpdate));

						if (sleepTime > MS_TO_SLEEP) {
							Thread.sleep(sleepTime);
							continue;
						}
					}

					this.lastFrame = currentTime;

					while (this.accuUpdate > this.dtUpdates) {
						this.state.getInput();
						this.state.update((currentTime - this.lastUpdate) / 1000);
						this.accuUpdate -= this.dtUpdates;
						this.lastUpdate = currentTime;
					}

					if (this.accuFps > this.dtFps) {
						this.state.setDt((currentTime - this.lastUpdate) / 1000);
						this.state.getStatePanel().repaint();
						this.accuFps -= this.dtFps;
					}

				} catch (Exception e) {
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

	public long time() {
		return System.currentTimeMillis();
	}
}
