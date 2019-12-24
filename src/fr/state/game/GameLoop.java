package fr.state.game;

public class GameLoop implements Runnable {

	private static int MS_TO_SLEEP = 2; // > 0

	private static boolean displayMeanUpdate = false;

	private static boolean displayMeanDraw = false;

	private static int meanPrecision = 20;

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
	private double lastDraw;

	double lastFrame;
	private Thread loop;

	private GameState state;
	private double meanUpdate;
	private double meanDraw;

	public GameLoop(GameState state) {
		this.fps = 60;
		this.updates = 60;
		this.dtFps = getDt(this.fps);
		this.accuFps = 0;
		this.dtUpdates = getDt(this.updates);
		this.accuUpdate = 0;
		this.lastUpdate = this.time();
		this.loop = new Thread(this);
		this.state = state;
		this.lastFrame = this.time();
		this.lastUpdate = this.time();
		this.lastDraw = this.time();
		this.meanDraw = 0;
		this.meanUpdate = 0;
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
						double dtms = currentTime - this.lastUpdate;
						this.state.update(dtms / 1000);
						this.accuUpdate -= this.dtUpdates;
						this.lastUpdate = currentTime;

						if (displayMeanUpdate) {
							this.meanUpdate = (this.meanUpdate * (meanPrecision - 1) + dtms) / meanPrecision;
							System.out.println("  " + 1000 / this.meanUpdate + " updates / sec");
						}
					}

					if (this.accuFps > this.dtFps) {
						if (displayMeanDraw) {
							double dtDrawMs = currentTime - this.lastDraw;
							this.meanDraw = (this.meanDraw * (meanPrecision - 1) + dtDrawMs) / meanPrecision;
							System.out.println("  " + 1000 / this.meanDraw + " draw / sec");
							this.lastDraw = currentTime;
						}

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
		return System.nanoTime() / 1000000;
	}
}
