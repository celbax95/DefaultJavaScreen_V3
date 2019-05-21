package fr.xmlmanager;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.datafilesmanager.XMLManager;

// TODO doc
public class XMLManagerDOM implements XMLManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile XMLManagerDOM instance;

	// Utilise pour recuperer le fichier xml
	private DocumentBuilder builder;

	// Utilise pour la sauvegarde des fichiers xml
	private Transformer transformer;

	// Utilise pour la recherche dans le fichier xml
	private XPath xpath;

	/**
	 * Private constructor for singleton.
	 */
	public XMLManagerDOM() {
		try {
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.transformer = TransformerFactory.newInstance().newTransformer();
		} catch (Exception e) {
			// e.printStackTrace();
		}

		this.xpath = XPathFactory.newInstance().newXPath();
	}

	@Override
	public String getAttribute(Object doc, String attributeName) {
		return ((Element) doc).getAttribute(attributeName);
	}

	@Override
	public Document getDocument(String filePath) throws FileNotFoundException {
		File fileXML = new File(filePath);

		if (!fileXML.exists())
			throw new FileNotFoundException();

		try {
			Document xml = this.builder.parse(fileXML);
			xml.getDocumentElement().normalize();
			return xml;
		} catch (final Exception e) {
		}
		return null;
	}

	@Override
	public Object getNode(Object doc, String name) {
		return this.getNode(doc, name, 1);
	}

	@Override
	public Object getNode(Object doc, String nodeName, int nb) {
		try {
			return this.xpath.evaluate("./" + nodeName + "[" + nb + "]",
					((Document) doc).getDocumentElement(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getNode(Object doc, String nodeName, String attribute, String value) {
		if (nodeName == null) {
			nodeName = "*";
		}

		try {
			return this.xpath.evaluate("./" + nodeName + "[@" + attribute + " = \'" + value + "\']",
					((Document) doc).getDocumentElement(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object[] getNodes(Object doc, String nodeName) {

		NodeList nl = null;
		try {
			nl = (NodeList) this.xpath.evaluate("./" + nodeName, ((Document) doc).getDocumentElement(),
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
		}

		return this.nodeListToObjectcArray(nl);
	}

	@Override
	public Object getParam(Object doc, String paramName, Object def) {
		Element e = null;
		try {
			e = (Element) this.xpath.evaluate("./param[@name = '" + paramName + "']",
					((Document) doc).getDocumentElement(), XPathConstants.NODE);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}

		if (e == null)
			return def;

		return this.dynamicCast(e.getAttribute("value"), e.getAttribute("type"));
	}

	@Override
	public Object[] getParams(Object doc) {

		NodeList nl = null;

		try {
			nl = (NodeList) this.xpath.evaluate("./param", ((Document) doc).getDocumentElement(),
					XPathConstants.NODESET);
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
	public Object getRoot(Object document) {
		return ((Document) document).getDocumentElement();
	}

	/**
	 * Recupere l'objet XPath
	 *
	 * @return l'objet XPath
	 */
	public XPath getXpath() {
		return this.xpath;
	}

	@Override
	public void saveFile(Object doc) {

		Document document = (Document) doc;

		DOMSource source = new DOMSource(((Document) doc).getDocumentElement());
		StreamResult result = new StreamResult(new File(document.getDocumentURI().substring(6)));
		this.transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		try {
			this.transformer.transform(source, result);
		} catch (TransformerException e) {
			// e.printStackTrace();
		}
	}

	@Override
	public void setParam(Object doc, String paramName, String newValue) {
		((Node) this.getNode(doc, "param", "name", paramName)).getAttributes().getNamedItem("value")
				.setTextContent(newValue);
	}

	/**
	 * Cast dynamiquement la chaine 'value' dans le type 'type'
	 *
	 * @param value : la chaine a caster
	 * @param type  : le type dans lequel il faut caster
	 * @return : value caste dans le type 'type'
	 */
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

	/**
	 * Transform une NodeList en tableau d'objet
	 *
	 * @param nodeList : liste de noeud
	 * @return : le tableau de noeuds
	 */
	private Object[] nodeListToObjectcArray(NodeList nodeList) {
		Object[] oa = new Object[nodeList.getLength()];

		for (int i = 0, c = nodeList.getLength(); i < c; i++) {
			oa[i] = nodeList.item(i);
		}

		return oa;
	}

	/**
	 * Return a singleton instance of XMLReader.
	 */
	public static XMLManagerDOM getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (XMLManagerDOM.class) {
				if (instance == null) {
					instance = new XMLManagerDOM();
				}
			}
		}
		return instance;
	}
}
