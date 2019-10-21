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

		HubHoster hh = new HubHoster(0, 3, "230.0.0.0", 10000, 10001);
		HubJoiner hj = new HubJoiner("coucou", Color.red, "230.0.0.0", 10001, 10000);

		Linker l = new Linker(0, "230.0.0.1", 10002);
		Searcher s = new Searcher(hj, "230.0.0.1", 10002, 10003);

		l.start();
		s.start();
		hh.start();
		hj.start();

		Locale.setDefault(Locale.ENGLISH);
		// new ConfInitializer().start();
	}
}