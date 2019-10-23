package fr.main;

import java.util.Locale;

import fr.init.ConfInitializer;

public class Main {
	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		new ConfInitializer().start();
	}
}