package fr.menu;

/**
 * Classe servant a la creation d'une page
 *
 * @author Loic.MACE
 *
 */
public class PageCreator {

	/**
	 * static Singleton instance.
	 */
	private static volatile PageCreator instance;

	/**
	 * Private constructor for singleton.
	 */
	private PageCreator() {
	}

	/**
	 * Return a singleton instance of PageCreator.
	 */
	public static PageCreator getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (PageCreator.class) {
				if (instance == null) {
					instance = new PageCreator();
				}
			}
		}
		return instance;
	}
}
