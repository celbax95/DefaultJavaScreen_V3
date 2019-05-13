package fr.datafilesmanager;

import java.io.FileNotFoundException;

public interface XMLManager {
	String getAttribute(Object from, String attrName);

	Object getDocument(String filePath) throws FileNotFoundException;

	Object getNode(Object from, String nodeName);

	Object getNode(Object from, String nodeName, int nb);

	Object getNode(Object from, String nodeName, String attribute, String value);

	Object[] getNodes(Object from, String nodeName);

	Object getParam(Object from, String paramName, Object def);

	Object[] getParams(Object from);

	Object getRoot(Object document);

	void saveFile(Object doc);

	void setParam(Object doc, String paramName, String newValue);
}
