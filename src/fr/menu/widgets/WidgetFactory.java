package fr.menu.widgets;

public class WidgetFactory {

	/**
	 * static Singleton instance.
	 */
	private static volatile WidgetFactory instance;

	/**
	 * Private constructor for singleton.
	 */
	private WidgetFactory() {
	}

	/**
	 * Return a singleton instance of WidgetFactory.
	 */
	public static WidgetFactory getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (WidgetFactory.class) {
				if (instance == null) {
					instance = new WidgetFactory();
				}
			}
		}
		return instance;
	}
}
