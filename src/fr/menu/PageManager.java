package fr.menu;

/**
 * Classe de gestion des pages du menu
 *
 * @author Loic.MACE
 *
 */
public class PageManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile PageManager instance;

	/**
	 * Private constructor for singleton.
	 */
	private PageManager() {
	}

	/**
	 * Return a singleton instance of PageManager.
	 */
	public static PageManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (PageManager.class) {
				if (instance == null) {
					instance = new PageManager();
				}
			}
		}
		return instance;
	}
}
