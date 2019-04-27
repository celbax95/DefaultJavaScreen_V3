Instructions :

ScreenApp
Application a lancer.

/*
	init : Creation des SCItem de l'appli
	appLoop : Affichage des SCItem dans l'ordre, time : temps entre deux affichages en secondes
	getScreenTitle : Return le nom de la fenetre
*/
public ScreenApp init(int scrWidth, int scrHeight);
public void appLoop(Graphics2D g, double time);
public String getScreenTitle();

SCItem extends Runnable
Objets utilises dans la ScreenApp

/*
	Dans l'appli, stocker les SCI dans une LinkedList, ajouter une methode getSCIFromType(String) pour
	recuperer les SCItem depuis leur type
	
	Cela permet a chaque SCItem de recuperer tous les objets de l'appli au besoin
*/

/*
	run : Gestion de l'objet, mouvement et interaction, sauf l'affichage
	Attention au zones critiques
*/

/*
	getSCItemType : renvoie une String contenant le nom de l'objet
	draw : Dessine l'objet sur g
	start : a utiliser juste apres la creation de l'objet (Lancement du Thread de l'objet)
	getAppli : renvoie la ScreenApp dans laquelle est l'Item
*/
public String getSCItemType();
public void draw(Graphics2D g);
public void start();
public Appli getAppli();