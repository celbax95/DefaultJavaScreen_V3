package fr.xmlreader;

public interface XMLReader {
	String getAttribute(Object from, String attrName);

	Object getNode(Object from, String nodeName);

	Object[] getNodes(Object from);

	Object[] getNodes(Object from, String nodeName);

	Object getParam(Object from, String paramName);

	Object getParams(Object from);

	Object getRoot(String fileName);
}
