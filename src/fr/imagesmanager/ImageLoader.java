package fr.imagesmanager;

import fr.logger.Logger;

public class ImageLoader {

	public ImageLoader() {

	}

	public void loadAll() {
		ImageManager im = ImageManager.getInstance();

		Logger.inf("Chargement de toutes les images");

		im.add("menuMain/background", "/resources/menu/menuMain/background.png");
		im.add("menuMain/title", "/resources/menu/menuMain/title.png");
		im.add("menuMain/exitStd", "/resources/menu/menuMain/exitStd.png");
		im.add("menuMain/exitPressed", "/resources/menu/menuMain/exitPressed.png");
		im.add("menuMain/hostStd", "/resources/menu/menuMain/hostStd.png");
		im.add("menuMain/hostPressed", "/resources/menu/menuMain/hostPressed.png");
		im.add("menuMain/joinStd", "/resources/menu/menuMain/joinStd.png");
		im.add("menuMain/joinPressed", "/resources/menu/menuMain/joinPressed.png");
		im.add("menuMain/settingsStd", "/resources/menu/menuMain/settingsStd.png");
		im.add("menuMain/settingsPressed", "/resources/menu/menuMain/settingsPressed.png");
	}
}
