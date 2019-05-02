package fr.datamanager;

import java.util.HashMap;
import java.util.Map;

import fr.xmlreader.XMLReader;
import fr.xmlreader.XMLReaderXPath;

public class DataManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile DataManager instance;

	private XMLReader xml;

	private Map<String, String> files;

	private DataManager() {
		this.files = new HashMap<>();
	}

	public void addFile(String name, String path) {
		this.files.put(name, path);
	}

	public Object dynamicCast(String value, String type) {
		switch (type.toLowerCase()) {
		case "int":
			return (int) Integer.valueOf(value);
		case "number":
			return (double) Double.valueOf(value);
		case "string":
			return value;
		}
		return null;
	}

	public Map<String, String> getFiles() {
		Map<String, String> m = new HashMap<>();

		m.putAll(this.files);

		return m;
	}

	public Object getParam(String fileName, String paramName) {

		Object root = this.xml.getRoot(this.files.get(fileName));

		Object param = this.xml.getNodeByAttribute(root, "param", "name", paramName);

		String aType = this.xml.getAttribute(param, "type");

		String aValue = this.xml.getAttribute(param, "value");

		return this.dynamicCast(aValue, aType);
	}

	public String[] getParamNames(String fileName, String NodeName, String attributeName, String attributeValue) {

		Object root = this.xml.getRoot(this.files.get(fileName));

		return this.xml.getNodesAttribute(root, "param", "nom");
	}

	public String[][] getParams(String fileName, String nodeName) {
		Object root = this.xml.getRoot(this.files.get(fileName));

		this.xml.get
	}

	public void init(XMLReader xml) {
		this.xml = xml;
	}

	public void removeFile(String name) {
		this.files.remove(name);
	}

	/**
	 * Return a singleton instance of XMLReader.
	 */
	public static DataManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (XMLReaderXPath.class) {
				if (instance == null) {
					instance = new DataManager();
				}
			}
		}
		return instance;
	}
}
