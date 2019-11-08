package fr.tester;

public class TimeTester {

	private static long startTime = System.nanoTime();
	private static int id = 0;

	public static long elapsed() {
		return System.nanoTime() - startTime;
	}

	public static double nanoToMilli(long nano) {
		return Math.round(nano / 10000D) / 100D;
	}

	public static void start() {
		startTime = System.nanoTime();
		id = 0;
	}

	public static void tick() {
		System.out.println("-- " + id++ + " --\n  " + nanoToMilli(elapsed()) + "\n");
		startTime = System.nanoTime();
	}

	public TimeTester() {
	}
}
