package fr.imagesmanager;

public class ImageLoader {

	public ImageLoader() {

	}

	public void loadAll() {
		ImageManager im = ImageManager.getInstance();

		im.add("menuMain/background", "/resources/menu/menuMain/background.png");
		im.add("menuMain/title", "/resources/menu/menuMain/title.png");
	}
}
