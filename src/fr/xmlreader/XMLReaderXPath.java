package fr.xmlreader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.NodeSet;

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

	@Override
	public String getAttribute(Object node, String attributeName) {
		return ((Element) node).getAttribute(attributeName);
	}

	@Override
	public Object getNode(Object node, String name) {
		return this.getNode(node, name, 1);
	}

	public Object getNode(Object node, String name, int nb) {
		try {
			return this.xpath.evaluate("//" + name + "[" + nb + "]", node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object[] getNodes(Object from) {

		Node n = (Node) from;

		return this.nodeListToObjectcArray(n.getChildNodes());
	}

	@Override
	public Object[] getNodes(Object from, String nodeName) {

		NodeSet nl = null;
		try {
			nl = (NodeSet) this.xpath.evaluate("//" + nodeName, from, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}

		return this.nodeListToObjectcArray(nl);
	}

	@Override
	public Object getParam(Object from, String paramName) {
		Element e = null;
		try {
			e = (Element) this.xpath.evaluate("//param[@name = '" + paramName + "']", from,
					XPathConstants.NODE);
		} catch (XPathExpressionException e1) {
		}

		return this.dynamicCast(e.getAttribute("value"), e.getAttribute("type"));
	}

	@Override
	public Object[] getParams(Object from) {

		NodeSet nl = null;

		try {
			nl = (NodeSet) this.xpath.evaluate("//param", from, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}

		Object[] oa = new Object[nl.getLength()];

		for (int i = 0, c = nl.getLength(); i < c; i++) {
			Element e = (Element) nl.item(i);

			oa[i] = this.dynamicCast(e.getAttribute("name"), e.getAttribute("type"));
		}

		return oa;
	}

	@Override
	public Object getRoot(String fileName) {
		try {
			File fileXML = new File(fileName);

			if (!fileXML.exists())
				return null;

			Document xml = this.builder.parse(fileXML);
			return xml.getDocumentElement();

		} catch (final Exception e) {
			System.err.println("Erreur lors du chargement du fichier\n" + e.getMessage());
			return null;
		}
	}

	public XPath getXpath() {
		return this.xpath;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

	private Object dynamicCast(String value, String type) {
		switch (type.toLowerCase()) {
		case "interger":
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

	private Object[] nodeListToObjectcArray(NodeList nl) {
		Object[] oa = new Object[nl.getLength()];

		for (int i = 0, c = nl.getLength(); i < c; i++) {
			oa[i] = nl.item(i);
		}

		return oa;
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

	public static void main(String[] args) {
		// XMLReaderXPath x = XMLReaderXPath.getInstance();
	}
}
