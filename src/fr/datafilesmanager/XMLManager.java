package fr.datafilesmanager;

import java.io.FileNotFoundException;

public interface XMLManager {
	/**
	 * Recupere un attribut depuis l'element 'doc' dans le xml
	 *
	 * @param doc      : element depuis lequel on cherche
	 * @param attrName : nom de l'attribut a chercher
	 * @return : la valeur de l'attribut
	 *
	 * @see getNode
	 */
	String getAttribute(Object doc, String attrName);

	/**
	 * Recupere un document XML depuis son chemin d'acces (path)
	 *
	 * @param filePath : chemin d'acces (path)
	 * @return le document recupere
	 * @throws FileNotFoundException : si le fichier n'existe pas
	 */
	Object getDocument(String filePath) throws FileNotFoundException;

	/**
	 * Recupere un noeud depuis l'element 'doc'
	 *
	 * @param doc      : element depuis lequel on cherche
	 * @param nodeName : nom du noeud a recuperer
	 * @return : le noeud recupere
	 */
	Object getNode(Object doc, String nodeName);

	/**
	 * Recupere un noeud depuis l'element 'doc'
	 *
	 * @param doc      : element depuis lequel on cherche
	 * @param nodeName : nom du noeud a recuperer
	 * @param nb       : numero du noeud (si plusieurs noeuds ont le meme nom)
	 * @return : le noeud recupere
	 */
	Object getNode(Object doc, String nodeName, int nb);

	/**
	 * Recupere un noeud depuis l'element 'doc' selon un attribut
	 *
	 * @param doc       : element depuis lequel on cherche
	 * @param nodeName  : nom du noeud a recuperer
	 * @param attribute : attribut a tester
	 * @param value     : valeur de l'attribut
	 * @return : le noeud recupere
	 */
	Object getNode(Object doc, String nodeName, String attribute, String value);

	/**
	 * Recupere tous les noeuds ayant pour nom 'nodeName'
	 *
	 * @param doc      : element depuis lequel on cherche
	 * @param nodeName : nom du noeud a recuperer
	 * @return : le noeud recupere
	 */
	Object[] getNodes(Object doc, String nodeName);

	/**
	 * Recupere un parametre du type < param nom="nom" type="type" value="value"/>
	 *
	 * @param doc       : element depuis lequel on cherche
	 * @param paramName : nom du parametre a recupere
	 * @param def       : valeur par defaut
	 * @return la valeur du parametre ou le parametre par defaut si le parametre
	 *         n'existe pas
	 */
	Object getParam(Object doc, String paramName, Object def);

	/**
	 * Recupere tous les parametres depuis l'element 'doc'
	 *
	 * @param doc : element depuis lequel on cherche
	 * @return : les parametres trouves
	 */
	Object[] getParams(Object doc);

	/**
	 * Recupere le noeud racine d'un document
	 *
	 * @param document : document depuis lequel on recupere la racine
	 * @return : le noeud racine du document
	 */
	Object getRoot(Object document);

	/**
	 * Sauvegarde un fichier precedement modifie
	 *
	 * @param doc : le fichier a sauvegarder
	 */
	void saveFile(Object doc);

	/**
	 * Change la valeur d'un parametre
	 *
	 * @param doc       : element depuis lequel on cherche le param
	 * @param paramName : nom du parametre
	 * @param newValue  : valeur a affecter au parametre
	 */
	void setParam(Object doc, String paramName, String newValue);
}
