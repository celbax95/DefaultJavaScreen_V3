package fr.statepanel;

public interface HardwareListner {
	void setAllListnersEnabeled(boolean activation);

	/**
	 * Gere l'etat d'activation du clavier
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setKeyboardEnabeled(boolean activation);

	/**
	 * Gere l'etat d'activation des clics souris
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseClicksEnabeled(boolean activation);

	/**
	 * Gere l'etat d'activation des mouvements de la souris
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseMovesEnabeled(boolean activation);

	/**
	 * Gere l'etat d'activation de la molette
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseWheelEnabeled(boolean activation);
}
