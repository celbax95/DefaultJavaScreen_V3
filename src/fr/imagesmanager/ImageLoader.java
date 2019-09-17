package fr.imagesmanager;

import fr.logger.Logger;

public class ImageLoader {

	ImageManager manager = ImageManager.getInstance();

	public ImageLoader() {

	}

	public void load(String name, String path) {
		if (!this.manager.contains(name)) {
			Logger.inf("Chargement de l'image \"" + name + "\" -> \"" + path + "\"");
			this.manager.add(name, path);
		}
	}

	public void load(String[] names, String[] paths) {
		assert names.length == paths.length;

		int nb = names.length;

		for (int i = 0; i < nb; i++) {
			this.load(names[i], paths[i]);
		}
	}

	public void loadAll() {
		Logger.inf("Chargement de toutes les images");

		// menuMain
		this.manager.add("menuMain/background", "/resources/menu/menuMain/background.png");
		this.manager.add("menuMain/title", "/resources/menu/menuMain/title.png");
		this.manager.add("menuMain/exitStd", "/resources/menu/menuMain/exitStd.png");
		this.manager.add("menuMain/exitPressed", "/resources/menu/menuMain/exitPressed.png");
		this.manager.add("menuMain/hostStd", "/resources/menu/menuMain/hostStd.png");
		this.manager.add("menuMain/hostPressed", "/resources/menu/menuMain/hostPressed.png");
		this.manager.add("menuMain/joinStd", "/resources/menu/menuMain/joinStd.png");
		this.manager.add("menuMain/joinPressed", "/resources/menu/menuMain/joinPressed.png");
		this.manager.add("menuMain/settingsStd", "/resources/menu/menuMain/settingsStd.png");
		this.manager.add("menuMain/settingsPressed", "/resources/menu/menuMain/settingsPressed.png");

		// menuOption
		this.manager.add("menuSettings/title", "/resources/menu/menuSettings/title.png");
		this.manager.add("menuSettings/graphStd", "/resources/menu/menuSettings/graphStd.png");
		this.manager.add("menuSettings/graphPressed", "/resources/menu/menuSettings/graphPressed.png");
		this.manager.add("menuSettings/controlsStd", "/resources/menu/menuSettings/controlsStd.png");
		this.manager.add("menuSettings/controlsPressed", "/resources/menu/menuSettings/controlsPressed.png");
		this.manager.add("menuSettings/profileStd", "/resources/menu/menuSettings/profileStd.png");
		this.manager.add("menuSettings/profilePressed", "/resources/menu/menuSettings/profilePressed.png");
		this.manager.add("menuSettings/gameoptStd", "/resources/menu/menuSettings/gameoptStd.png");
		this.manager.add("menuSettings/gameoptPressed", "/resources/menu/menuSettings/gameoptPressed.png");
		this.manager.add("menuSettings/backStd", "/resources/menu/menuSettings/backStd.png");
		this.manager.add("menuSettings/backPressed", "/resources/menu/menuSettings/backPressed.png");
	}
}
