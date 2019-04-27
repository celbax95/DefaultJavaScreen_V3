package fr.util.time;

public class Timer {

	private double lastTick;
	private long sec;

	public Timer() {
		sec = System.nanoTime();
	}

	public double lastTick() {
		return lastTick;
	}

	public double tick() {
		long time = System.nanoTime();
		long tmp = System.nanoTime() - sec;
		sec = time;
		lastTick = (double) tmp / 1000000000;
		return lastTick;
	}
}
