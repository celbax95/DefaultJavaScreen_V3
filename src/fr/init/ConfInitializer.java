package fr.init;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.panelstates.MenuState;
import fr.repainter.DefaultRepainter;
import fr.statepanel.AppStateManager;
import fr.statepanel.StatePanel;
import fr.window.Window;
import fr.xmlmanager.XMLManagerDOM;

public class ConfInitializer {

	/**
	 * static Singleton instance.
	 */
	private static volatile ConfInitializer instance;

	private DatafilesManager dfm;

	/**
	 * Private constructor for singleton.
	 */
	private ConfInitializer() {
		this.dfm = DatafilesManager.getInstance();
	}

	/**
	 * Creation des elements a partir de la conf
	 */
	public void start() {

		this.initConfFiles();

		this.windowInitializers(this.getWinConf());

		this.windowStart(this.getWinConf());
	}

	/**
	 * Recuperation de la racine du fichier xml de configuration de la fenetre
	 */
	private Object getWinConf() {
		Object winConf = null;
		winConf = this.dfm.getDocument("winConf");

		return winConf;
	}

	/**
	 * Ajout des fichiers de configuration
	 */
	private void initConfFiles() {
		this.dfm.init(new XMLManagerDOM());
		this.dfm.addFile("winConf", "conf/winConf.xml");
	}

	/**
	 * Creation de l'ecran
	 */
	private void windowInitializers(Object configRoot) {

		XMLManager reader = this.dfm.getXmlReader();

		int width = (int) reader.getParam(configRoot, "width", 1366);

		int height = (int) reader.getParam(configRoot, "height", 768);

		final DefaultRepainter repainter = new DefaultRepainter();

		final StatePanel mainPanel = StatePanel.getInstance();
		mainPanel.init(width, height, repainter);

		final AppStateManager stator = AppStateManager.getInstance();
		stator.addState(new MenuState());
		stator.setStatable(mainPanel);
		stator.applyState("menu");
	}

	private void windowStart(Object configRoot) {

		XMLManager reader = this.dfm.getXmlReader();

		int marginBottom = (int) reader.getParam(configRoot, "marginBottom", 35);
		int marginRight = (int) reader.getParam(configRoot, "marginRight", 6);
		int margin = (int) reader.getParam(configRoot, "margin", 2);

		final Window screen = Window.getInstance();
		screen.init(StatePanel.getInstance(), marginRight, marginBottom, margin);

		screen.start();
	}

	/**
	 * Return a singleton instance of ConfInitializer.
	 */
	public static ConfInitializer getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (ConfInitializer.class) {
				if (instance == null) {
					instance = new ConfInitializer();
				}
			}
		}
		return instance;
	}

}
