package fr.xmlreader;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.datafilesmanager.XMLReader;

// TODO doc
public class XMLReaderXPath implements XMLReader {

	/**
	 * static Singleton instance.
	 */
	private static volatile XMLReaderXPath instance;

	private DocumentBuilder builder;

	private XPath xpath;

	/**
	 * Private constructor for singleton.
	 */
	public XMLReaderXPath() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			// e.printStackTrace();
		}

		this.setXpath(XPathFactory.newInstance().newXPath());
	}

	private Object dynamicCast(String value, String type) {
		switch (type.toLowerCase()) {
		case "integer":
			return Integer.valueOf(value);
		case "number":
			return Double.valueOf(value);
		case "boolean":
			return Boolean.valueOf(value);
		case "string":
			return value;
		}
		return null;
	}

	@Override
	public String getAttribute(Object node, String attributeName) {
		return ((Element) node).getAttribute(attributeName);
	}

	@Override
	public Object getNode(Object node, String name) {
		return this.getNode(node, name, 1);
	}

	@Override
	public Object getNode(Object node, String name, int nb) {
		try {
			return this.xpath.evaluate("./" + name + "[" + nb + "]", node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object[] getNodes(Object from, String nodeName) {

		NodeList nl = null;
		try {
			nl = (NodeList) this.xpath.evaluate("./" + nodeName, from, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}

		return this.nodeListToObjectcArray(nl);
	}

	@Override
	public Object getParam(Object from, String paramName, Object def) {
		Element e = null;
		try {
			e = (Element) this.xpath.evaluate("./param[@name = '" + paramName + "']", from,
					XPathConstants.NODE);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}

		if (e == null)
			return def;

		return this.dynamicCast(e.getAttribute("value"), e.getAttribute("type"));
	}

	@Override
	public Object[] getParams(Object from) {

		NodeList nl = null;

		try {
			nl = (NodeList) this.xpath.evaluate("./param", from, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}

		Object[] oa = new Object[nl.getLength()];

		for (int i = 0, c = nl.getLength(); i < c; i++) {
			Element e = (Element) nl.item(i);

			oa[i] = this.dynamicCast(e.getAttribute("value"), e.getAttribute("type"));
		}

		return oa;
	}

	@Override
	public Object getRoot(String fileName) throws FileNotFoundException {
		File fileXML = new File(fileName);

		if (!fileXML.exists())
			throw new FileNotFoundException();

		try {
			Document xml = this.builder.parse(fileXML);
			return xml.getDocumentElement();
		} catch (final Exception e) {
		}
		return null;
	}

	public XPath getXpath() {
		return this.xpath;
	}

	private Object[] nodeListToObjectcArray(NodeList nl) {
		Object[] oa = new Object[nl.getLength()];

		for (int i = 0, c = nl.getLength(); i < c; i++) {
			oa[i] = nl.item(i);
		}

		return oa;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

	/**
	 * Return a singleton instance of XMLReader.
	 */
	public static XMLReaderXPath getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (XMLReaderXPath.class) {
				if (instance == null) {
					instance = new XMLReaderXPath();
				}
			}
		}
		return instance;
	}
}
