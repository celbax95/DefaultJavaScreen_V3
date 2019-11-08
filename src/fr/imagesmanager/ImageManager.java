package fr.imagesmanager;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import fr.logger.Logger;

public class ImageManager {
	/**
	 * static Singleton instance.
	 */
	private static volatile ImageManager instance;

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

	private Map<String, Image> images;

	/**
	 * Private constructor for singleton.
	 */
	private ImageManager() {
		this.images = new HashMap<>();
	}

	public void add(String name, String path) {
		File file = new File(path.substring(1));

		// On regarde si un fichier d'image externe existe
		if (file != null && file.exists()) {
			// Si il existe on prend l'image depuis celui-ci
			this.images.put(name, new ImageIcon(file.getAbsolutePath()).getImage());
		} else {
			// Si il n'existe pas on prend l'image par defaut
			InputStream is = this.getClass().getResourceAsStream(path);

			Image img = null;
			try {
				img = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (img != null) {
				// Ajout
				this.images.put(name, img);
			} else {
				System.err.println("Ajout de l'image impossible :\n\tL'image \"" + path
						+ "\" n'existe pas ou n'est pas un fichier .xml");
			}
		}
	}

	public boolean contains(String name) {
		return this.images.containsKey(name);
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

	public void removeAll() {
		Logger.inf("Toutes les images ont été déchargées.");
		this.images.clear();
	}

	/**
	 * Supprime toutes les images dont le nom contient node
	 *
	 * @param node : par exemple "stateName/other" supprimera stateName/other/\*
	 */
	public void removeStartWith(String node) {
		this.images.keySet().removeIf((k) -> k.indexOf(node) == 0);
	}
}
