package menu;

/**
 * Cette classe va chercher dans les fichiers de config le nom de toutes les
 * pages du menu
 *
 * @author Loic.MACE
 *
 */
public class PagesInitializer {

	/**
	 * static Singleton instance.
	 */
	private static volatile PagesInitializer instance;

	/**
	 * Private constructor for singleton.
	 */
	private PagesInitializer() {
	}

	/**
	 * Return a singleton instance of PagesInitializer.
	 */
	public static PagesInitializer getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (PagesInitializer.class) {
				if (instance == null) {
					instance = new PagesInitializer();
				}
			}
		}
		return instance;
	}
}
