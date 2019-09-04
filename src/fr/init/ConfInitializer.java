package fr.init;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.state.menu.MenuState;
import fr.statepanel.AppStateManager;
import fr.statepanel.StatePanel;
import fr.window.Window;
import fr.xmlmanager.XMLManagerDOM;

public class ConfInitializer {

	// Fichiers de configuration
	private DatafilesManager dfm;

	public ConfInitializer() {
		this.dfm = DatafilesManager.getInstance();
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
		this.dfm.addFile("winConf", "/conf/winConf.xml");
	}

	/**
	 * Creation des elements a partir de la conf
	 */
	public void start() {

		this.initConfFiles();

		StatePanel state = this.windowInitializers(this.getWinConf());

		this.windowStart(this.getWinConf(), state);
	}

	/**
	 * Creation de l'ecran
	 *
	 * @param configRoot : racine du fichier de configuration
	 */
	private StatePanel windowInitializers(Object configRoot) {

		XMLManager reader = this.dfm.getXmlReader();

		int width = (int) reader.getParam(configRoot, "width", 1366);

		int height = (int) reader.getParam(configRoot, "height", 768);

		final StatePanel mainPanel = new StatePanel();
		mainPanel.init(width, height);

		final AppStateManager stator = new AppStateManager();
		stator.addState(new MenuState());
		stator.setStatable(mainPanel);
		stator.applyState("menu");

		return mainPanel;
	}

	/**
	 * Lance l'affichage de la fenetre
	 *
	 * @param configRoot : racine du fichier de configuration
	 */
	private void windowStart(Object configRoot, StatePanel state) {

		XMLManager reader = this.dfm.getXmlReader();

		int marginBottom = (int) reader.getParam(configRoot, "marginBottom", 35);
		int marginRight = (int) reader.getParam(configRoot, "marginRight", 6);
		int margin = (int) reader.getParam(configRoot, "margin", 2);

		final Window screen = new Window();
		screen.init(state, marginRight, marginBottom, margin);

		screen.start();
	}
}
