package fr.statepanel;

/**
 * Classe abstraite representant un etat
 *
 * @author Loic.MACE
 *
 */
public abstract class AppState implements IAppState {

	// Si le state est utilise
	public boolean active;

	// Classe servant a repaint l'etat
	public Repainter repainter;

	public AppState() {
	}

	@Override
	public void setActive(boolean active) throws RuntimeException {
		this.active = active;
		if (this.active) {
			if (this.repainter == null)
				throw new IllegalStateException(
						"Utilisez la methode setRepainter pour initialiser le repainter");
			this.repainter.setRate(this.getRepaintRate());
			this.repainter.start();
		} else {
			this.repainter.interrupt();
			this.repainter = null;
		}
	}

	@Override
	public void setRepainter(Repainter repainter) {
		this.repainter = repainter;
	}
}
