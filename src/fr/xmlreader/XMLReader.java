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

// TODO doc
public class XMLReader {

	/**
	 * static Singleton instance.
	 */
	private static volatile XMLReader instance;

	private DocumentBuilder builder;

	private XPath xpath;

	/**
	 * Private constructor for singleton.
	 */
	public XMLReader() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			// e.printStackTrace();
		}

		this.setXpath(XPathFactory.newInstance().newXPath());
	}

	public Object getAttribute(Node n, String a) {
		return ((Element) n).getAttribute(a);
	}

	public Node getNode(Node n, String name) {
		return this.getNode(n, name, 1);
	}

	public Node getNode(Node n, String name, int nb) {
		try {
			return (Node) this.xpath.evaluate("//" + name + "[" + nb + "]", n, XPathConstants.NODE);
		} catch (final XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public Node getNodeByAttribute(Node n, String nodeName, String attribute, String value) {
		try {
			return (Node) this.xpath.evaluate("//" + nodeName + "@[" + attribute + "=" + value + "]", n,
					XPathConstants.NODE);
		} catch (final XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public NodeList getNodeList(Node n, String name) {
		try {
			return (NodeList) this.xpath.evaluate("//" + name, n, XPathConstants.NODESET);
		} catch (final XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public Node getParentNode(Node n) {
		return n.getParentNode();
	}

	@SuppressWarnings("unused")
	private Node getRoot(String fileName) {
		try {
<<<<<<< HEAD
			final File fileXML = new File("fileName");
=======
			final File fileXML = new File(fileName);
>>>>>>> Avancement XMLReader

			if (!fileXML.exists())
				return null;

			final Document xml = this.builder.parse(fileXML);
			return xml.getDocumentElement();

		} catch (final Exception e) {
			System.err.println("Erreur lors du chargement du fichier\n" + e.getMessage());
			return null;
		}
	}

	public String getText(Node n) {
		return n.getTextContent();
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
	public static XMLReader getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (XMLReader.class) {
				if (instance == null) {
					instance = new XMLReader();
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		final XMLReader x = XMLReader.getInstance();

		final Node root = x.getRoot("test.xml");

		System.out.println(root);

		final Node n = x.getNode(root, "param", 1);

		System.out.println(n.getNodeName());
	}
}
