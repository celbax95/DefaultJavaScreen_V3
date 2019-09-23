package fr.datafilesmanager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import fr.logger.Logger;
import fr.xmlmanager.XMLManagerDOM;

/**
 * Gestionnaire des fichiers de configuration
 *
 * @author Loic.MACE
 *
 */
public class DatafilesManager {

	private static boolean initialized = false;

	/**
	 * static Singleton instance.
	 */
	private static volatile DatafilesManager instance;

	/**
	 * Return a singleton instance of XMLReader.
	 */
	public static DatafilesManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (XMLManagerDOM.class) {
				if (instance == null) {
					instance = new DatafilesManager();
				}
			}
		}
		return instance;
	}

	// Accesseur aux fichiers de configuration
	private XMLManager xmlManager;

	// Fichiers sauvegardes
	private Map<String, String> files;

	private DatafilesManager() {
		this.files = new HashMap<>();
	}

	/**
	 * Ajoute un fichier au gestionnaire
	 *
	 * @param name : nom du fichier
	 * @param url  : chemin (path) du fichier
	 */
	public void addFile(String name, String path) {

		URL url = this.getClass().getResource(path);

		File file = null;
		try {
			file = Paths.get(url.toURI()).toFile();
		} catch (URISyntaxException e) {
		}

		if (file != null && file.exists() && this.getExtension(file).equals(".xml")) {
			this.files.put(name, file.getPath());
		} else {
			System.err.println("Ajout du fichier impossible :\n\tLe fichier \"" + url
					+ "\" n'existe pas ou n'est pas un fichier .xml");
		}

		if (this.xmlManager != null) {
			this.xmlManager.saveFile(this.getFile(name));
		} else {
			Logger.warn(
					"Pour une execution optimise, il est conseillé d'ajouter l'XML Manager avant d'ajouter les fichiers");
		}
	}

	/**
	 * Recupere l'extension d'un fichier
	 *
	 * @param file : fichier a analyser
	 *
	 * @return l'extension du fichier / une chaine vide si le fichier est invalide
	 */
	public String getExtension(File file) {

		if (file != null && file.exists()) {

			String fileName = file.getName();

			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";

	}

	/**
	 *
	 * Recupere un document a partir de la liste de fichiers sauvegardes
	 *
	 * @param name : nom du fichier
	 * @return : le document (racine) du fichier de configuration
	 */
	public Object getFile(String name) {
		try {
			return this.xmlManager.getDocument(this.files.get(name));
		} catch (Exception e) {
			System.err.println("La valeur \"" + name + "\" n'existe pas dans les fichiers connus.");
			System.exit(0);
		}
		return null; // Inaccessible
	}

	/**
	 * Recupere la totalitee des fichiers sauvegardes
	 *
	 * @return une Map qui lie le nom du fichier a son chemin (path) d'acces
	 */
	public Map<String, String> getFiles() {
		Map<String, String> m = new HashMap<>();

		m.putAll(this.files);

		return m;
	}

	/**
	 * Recupere le xmlReader permettant d'acceder aux informations des fichiers de
	 * configuration
	 *
	 * @return
	 * @throws RuntimeException
	 */
	public XMLManager getXmlManager() throws RuntimeException {
		if (!initialized)
			throw new RuntimeException("Appeler la methode init() pour initialiser DatafilesManager");
		return this.xmlManager;
	}

	/**
	 * Methode a appeler avant utilisation
	 *
	 * Affecte un Accesseur de XML au Gestionnaire
	 *
	 * @param xmlReader : accesseur de XML a affecter
	 */
	public void init(XMLManager xmlReader) {
		this.xmlManager = xmlReader;
		initialized = true;
	}
}
