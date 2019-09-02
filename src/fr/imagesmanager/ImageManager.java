package fr.imagesmanager;

import java.awt.Image;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ImageManager {
	/**
	 * static Singleton instance.
	 */
	private static volatile ImageManager instance;

	private Map<String, Image> images;

	/**
	 * Private constructor for singleton.
	 */
	private ImageManager() {
		this.images = new HashMap<>();
	}

	public void add(String name, String path) {
		URL url = Object.class.getResource(path);

		File file = null;
		try {
			file = Paths.get(url.toURI()).toFile();
		} catch (URISyntaxException e) {
		}

		if (file != null && file.exists()) {
			// Ajout
			this.images.put(name, new ImageIcon(url).getImage());
		} else {
			System.err.println("Ajout de l'image impossible :\n\tL'image \"" + url
					+ "\" n'existe pas ou n'est pas un fichier .xml");
		}
	}

	public Image get(String name) {
		if (this.images.containsKey(name))
			return this.images.get(name);
		else {
			System.err.println("L'image \"" + name + "\" est inconnue.");
		}
		return null;
	}

	public void remove(String name) {
		if (this.images.containsKey(name)) {
			this.images.remove(name);
		} else {
			System.err.println("L'image \"" + name + "\" est inconnue.");
		}
	}

	/**
	 * Return a singleton instance of ImageManager.
	 */
	public static ImageManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (ImageManager.class) {
				if (instance == null) {
					instance = new ImageManager();
				}
			}
		}
		return instance;
	}
}
