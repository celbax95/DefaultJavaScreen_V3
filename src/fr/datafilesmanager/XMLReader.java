package fr.datafilesmanager;

import java.io.FileNotFoundException;

public interface XMLReader {
	String getAttribute(Object from, String attrName);

	Object getNode(Object from, String nodeName);

	Object getNode(Object from, String nodeName, int nb);

	Object[] getNodes(Object from, String nodeName);

	Object getParam(Object from, String paramName, Object def);

	Object[] getParams(Object from);

	Object getRoot(String fileName) throws FileNotFoundException;
}
