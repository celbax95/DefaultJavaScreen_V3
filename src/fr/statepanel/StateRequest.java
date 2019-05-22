package fr.statepanel;

/**
 * Requete de changement d'etat
 *
 * @author Loic.MACE
 *
 */
public class StateRequest extends Exception {

	private static final long serialVersionUID = 1L;

	private final String state;

	/**
	 * Constructeur
	 *
	 * @param newState : etat de substitution
	 */
	public StateRequest(String newState) {
		super("Request de changement de state vers : " + newState);
		this.state = newState;
	}

	/**
	 * Recupere l'etat de substitution
	 * 
	 * @return l'etat de substitution
	 */
	public String getState() {
		return this.state;
	}
}
