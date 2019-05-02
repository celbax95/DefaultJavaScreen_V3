package fr.xmlreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

// TODO doc
public class XMLReaderXPath {

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

	public String getAttribute(Object node, String attributeName) {
		return ((Element) node).getAttribute(attributeName);
	}

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

	public Object getNodeByAttribute(Object node, String nodeName, String attribute, String value) {
		try {
			return this.xpath.evaluate("//" + nodeName + "@[" + attribute + "=" + value + "]", node,
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public NodeList getNodeList(Object node, String name) {
		try {
			return (NodeList) this.xpath.evaluate("//" + name, node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public String[] getNodesAttribute(Object node, String nodeName, String attributeName) {
		NodeList nl = this.getNodeList(node, nodeName);

		List<String> nll = new ArrayList<>();

		for (int i = 0, c = nl.getLength(); i < c; i++) {
			String attr = this.getAttribute(nl.item(i), attributeName);
			if (attr != null) {
				nll.add(attr);
			}
		}

		return nll.toArray(new String[nll.size()]);
	}

	public Object getParentNode(Object node) {
		return ((Node) node).getParentNode();
	}

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

	public String getText(Object node) {
		return ((Node) node).getTextContent();
	}

	public XPath getXpath() {
		return this.xpath;
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

	public static void main(String[] args) {
		// XMLReaderXPath x = XMLReaderXPath.getInstance();
	}
}
