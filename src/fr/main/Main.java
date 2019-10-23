package fr.main;

import java.awt.Color;
import java.util.Locale;

import fr.server.HubHoster;
import fr.server.HubJoiner;
import fr.server.Linker;
import fr.server.Searcher;
import fr.server.ServerData;

public class Main {
	public static void main(String[] args) {

		int serverIndex = 0;

		int id = 4;

		if (id == 1 || id == 0) {
			HubHoster hh = new HubHoster(0, "playerhpost", Color.green, 4, ServerData.getGroup(serverIndex),
					ServerData.getPort(serverIndex));
			Linker l = new Linker(0, ServerData.getGroup(serverIndex), ServerData.getPort(serverIndex));
			l.start();
			hh.start();
		}

		if (id == 2 || id == 0) {
			HubJoiner hj = new HubJoiner("red", Color.red, ServerData.getGroup(serverIndex),
					ServerData.getPort(serverIndex));
			Searcher s = new Searcher(hj, ServerData.getGroup(serverIndex), ServerData.getPort(serverIndex));
			s.start();
			hj.start();
		}

		if (id == 3 || id == 0) {
			HubJoiner hj2 = new HubJoiner("blue", Color.blue, ServerData.getGroup(serverIndex),
					ServerData.getPort(serverIndex));
			Searcher s2 = new Searcher(hj2, ServerData.getGroup(serverIndex), ServerData.getPort(serverIndex));
			s2.start();
			hj2.start();
		}

		if (id == 4 || id == 0) {
			HubJoiner hj3 = new HubJoiner("pink", Color.blue, ServerData.getGroup(serverIndex),
					ServerData.getPort(serverIndex));
			Searcher s3 = new Searcher(hj3, ServerData.getGroup(serverIndex), ServerData.getPort(serverIndex));
			s3.start();
			hj3.start();
		}

		Locale.setDefault(Locale.ENGLISH);
		// new ConfInitializer().start();
	}
}