package fr.xmlreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO doc
public class XMLReader {

	private static XMLReader instance;

	static {
		instance = new XMLReader();
	}

	private DocumentBuilder builder;

	private XPath xpath;

	private Map<String, String> files;

	public XMLReader() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			// e.printStackTrace();
		}

		this.setXpath(XPathFactory.newInstance().newXPath());

		this.setFiles(new HashMap<>());
	}

	public void addFile(String name, String path) throws FileNotFoundException {
		if (new File(path).exists()) {
			this.files.put(name, path);
		} else
			throw new FileNotFoundException();
	}

	public void clearFiles() {
		this.files.clear();
	}

	public Map<String, String> getFiles() {
		return this.files;
	}

	public XPath getXpath() {
		return this.xpath;
	}

	public void removeFile(String name) {
		this.files.remove(name);
	}

	public void removePath(String path) {
		try {
			final Iterator<String> keys = this.files.keySet().iterator();
			final Iterator<String> values = this.files.values().iterator();
			while (true) {
				if (values.next().equals(path)) {
					this.files.remove(keys.next());
					break;
				}
			}
		} catch (final Exception e) {
		}
	}

	public void setFiles(Map<String, String> files) {
		this.files = files;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

	@SuppressWarnings("unused")
	private Element getRoot(String fileName) {
		try {
			final File fileXML = new File("fileName");

			if (!fileXML.exists())

				return null;
			Document xml;
			xml = this.builder.parse(fileXML);
			return xml.getDocumentElement();

		} catch (final Exception e) {
			System.err.println("Erreur lors du chargement du fichier\n" + e.getMessage());
			return null;
		}
	}

	public static XMLReader getInstance() {
		return instance;
	}
}
