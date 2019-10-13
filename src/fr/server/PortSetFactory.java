package fr.server;

public class PortSetFactory {

	private final static int PORTSET_COUNT = 4;

	private final static int[][] PORTSETS = {
			{ 10004, 10003, 10001, 10002 },
			{ 10104, 10103, 10101, 10102 },
			{ 10204, 10203, 10201, 10202 },
			{ 10304, 10303, 10301, 10302 } };

	public static PortSet create(int index) {
		if (index < 0) {
			index = 0;
		}
		if (index >= PORTSET_COUNT) {
			index = PORTSET_COUNT - 1;
		}

		return create(PORTSETS[index]);
	}

	private static PortSet create(int[] tab) {
		return new PortSet(tab[0], tab[1], tab[2], tab[3]);
	}
}
