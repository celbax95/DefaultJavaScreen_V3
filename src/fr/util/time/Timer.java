package fr.util.time;

public class Timer {

	private double lastTick;
	private long sec;

	public Timer() {
		this.sec = System.nanoTime();
	}

	public double lastTick() {
		return this.lastTick;
	}

	public double tick() {
		final long time = System.nanoTime();
		final long tmp = System.nanoTime() - this.sec;
		this.sec = time;
		this.lastTick = (double) tmp / 1000000;
		return this.lastTick;
	}
}
