package fr.repainter;

import javax.swing.JPanel;

import fr.sigmanager.SignalManager;
import fr.sigmanager.ThreadManager;
import fr.statepanel.Repainter;

/**
 * Repainter à deux etats, repeint un JPanel sur demande ou toutes les x
 * millisecondes
 *
 * @author Loic.MACE
 *
 */
public class DefaultRepainter implements Repainter {

	private static final int DEFAULT_RATE = 17;

	// Utilise comme signal, rythme de repaint
	private Integer rate;

	// Panel a repaint
	private JPanel panel;

	// Thread de la classe
	private final Thread myThread;

	// Doit-on repeindre en continue
	private boolean repaint;

	// Signal pour repaint sur demande
	private final Object waiter;

	private final String repainterThreadName = "repainter";

	private final String repaintSignalName = "repaint";

	public DefaultRepainter() {
		super();
		this.myThread = new Thread(this);
		ThreadManager tm = ThreadManager.getInstance();
		tm.add(this.repainterThreadName, this.myThread);
		tm.lock(this.repainterThreadName);

		this.rate = DEFAULT_RATE;
		this.panel = null;
		this.repaint = true;
		this.waiter = SignalManager.getInstance().addSignal(this.repaintSignalName);
	}

	public DefaultRepainter(JPanel requestor) {
		this();
		this.panel = requestor;
	}

	public DefaultRepainter(JPanel requestor, int rate) {
		this();
		this.rate = rate;
		this.panel = requestor;
	}

	@Override
	public void interrupt() {
		ThreadManager.getInstance().remove(this.repainterThreadName);
	}

	@Override
	public void repaint() {
		this.repaint = true;

		synchronized (this.waiter) {
			this.waiter.notifyAll();
		}
	}

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			while (this.rate < 0 && !this.repaint) {
				synchronized (this.waiter) {
					try {
						this.waiter.wait();
					} catch (final InterruptedException e) {
						return;
					}
				}
			}

			this.panel.repaint();

			int tmpRate = 0;
			synchronized (this.rate) {
				tmpRate = this.rate;
			}
			if (tmpRate < 0) {
				this.repaint = false;
			} else if (tmpRate > 0) {
				try {
					Thread.sleep(tmpRate);
				} catch (final InterruptedException e) {
				}
			}
		}

	}

	@Override
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void setRate(int rate) {
		synchronized (this.rate) {
			this.rate = rate;
		}
	}

	@Override
	public void start() {
		this.myThread.start();
	}
}
