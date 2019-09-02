package fr.imagesmanager;

public class ImageLoader {

	public ImageLoader() {

	}

	public void loadAll() {
		ImageManager im = ImageManager.getInstance();

		im.add("menuMain/random", "/resources/menu/menuMain/random.jpg");
		im.add("menuMain/background", "/resources/menu/menuMain/background.png");
	}
}
