package fr.statepanel;

public class StateRequest extends Exception {

	private static final long serialVersionUID = 1L;

	private final String state;

	public StateRequest(String newState) {
		super("Request de changement de state vers : " + newState);
		this.state = newState;
	}

	public String getState() {
		return this.state;
	}
}
