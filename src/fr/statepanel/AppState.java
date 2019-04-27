package fr.statepanel;

public abstract class AppState implements IAppState {

	public boolean active;
	public Repainter repainter;

	public AppState() {
	}

	@Override
	public void setActive(boolean active) throws RuntimeException {
		this.active = active;
		if (this.active) {
			if (this.repainter == null)
				throw new IllegalStateException("Utilisez la methode setRepainter pour initialiser le repainter");
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
