package fr.xmlreader;

public interface XMLReader {
	String getAttribute(Object node, String attributeName);

	Object getNodeByAttribute(Object node, String nodeName, String attribute, String value);

	String[] getNodesAttribute(Object node, String nodeName, String attributeName);

	Object getRoot(String fileName);
}
