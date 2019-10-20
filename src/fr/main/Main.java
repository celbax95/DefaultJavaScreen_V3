package fr.main;

import java.util.Locale;

import fr.server.Linker;
import fr.server.Searcher;

public class Main {
	public static void main(String[] args) {
		Linker l = new Linker("230.0.0.0", 10000);
		l.start();
		Searcher s = new Searcher("230.0.0.0", 10000, 10001);
		s.start();
		Locale.setDefault(Locale.ENGLISH);
		// new ConfInitializer().start();
	}
}