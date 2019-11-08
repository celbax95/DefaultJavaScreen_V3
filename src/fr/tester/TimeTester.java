package fr.tester;

public class TimeTester {

	private static long startTime = System.nanoTime();

	public static long elapsed() {
		return System.nanoTime() - startTime;
	}

	public static double nanoToMilli(long nano) {
		return Math.round(nano / 4D) / 2D;
	}

	public static void start() {
		startTime = System.nanoTime();
	}

	public static void tick() {
		System.out.println("----\n  " + nanoToMilli(elapsed()) + "\n----");
	}

	public TimeTester() {
	}
}
