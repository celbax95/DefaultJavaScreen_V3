package fr.datafilesmanager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.xmlmanager.XMLManagerDOM;

public class DatafilesManager {

	private static boolean initialized = false;

	/**
	 * static Singleton instance.
	 */
	private static volatile DatafilesManager instance;

	private XMLManager xmlReader;

	private Map<String, String> files;

	private DatafilesManager() {
		this.files = new HashMap<>();
	}

	public void addFile(String name, String path) {

		File file = new File(path);

		if (file.exists() && this.getExtension(file).equals(".xml")) {
			this.files.put(name, path);
		} else {
			System.err.println("Ajout du fichier impossible :\n\tLe fichier \"" + path
					+ "\"n'existe pas ou n'est pas un fichier .xml");
		}
	}

	public Object getDocument(String name) {
		try {
			return this.xmlReader.getDocument(this.files.get(name));
		} catch (Exception e) {
			System.err.println("La valeur \"" + name + "\" n'existe pas dans les fichiers connus.");
			System.exit(0);
		}
		return null; // Inaccessible
	}

	public String getExtension(File file) {

		if (file != null && file.exists()) {

			String fileName = file.getName();

			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";

	}

	public Map<String, String> getFiles() {
		Map<String, String> m = new HashMap<>();

		m.putAll(this.files);

		return m;
	}

	public XMLManager getXmlReader() throws RuntimeException {
		if (!initialized)
			throw new RuntimeException("Appeler la methode init() pour initialiser DatafilesManager");
		return this.xmlReader;
	}

	public void init(XMLManager xmlReader) {
		this.xmlReader = xmlReader;
		initialized = true;
	}

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
}
