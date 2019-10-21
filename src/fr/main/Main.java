package fr.main;

import java.awt.Color;
import java.util.Locale;

import fr.server.HubHoster;
import fr.server.HubJoiner;
import fr.server.Linker;
import fr.server.Searcher;

public class Main {
	public static void main(String[] args) {
//		Linker l = new Linker("230.0.0.0", 10000);
//		l.start();
//		Searcher s = new Searcher("230.0.0.0", 10000, 10001);
//		s.start();

		HubHoster hh = new HubHoster(0, "playerhpost", Color.green, 3, "230.0.0.0", 10000, 10001);
		Linker l = new Linker(0, "230.0.0.1", 10002);
		l.start();
		hh.start();

		HubJoiner hj = new HubJoiner("red", Color.red, "230.0.0.0", 10001, 10000);
		Searcher s = new Searcher(hj, "230.0.0.1", 10002, 10003);
		s.start();
		hj.start();

		HubJoiner hj2 = new HubJoiner("blue", Color.blue, "230.0.0.0", 10001, 10000);
		Searcher s2 = new Searcher(hj, "230.0.0.1", 10002, 10003);
		s2.start();
		hj2.start();

		Locale.setDefault(Locale.ENGLISH);
		// new ConfInitializer().start();
	}
}