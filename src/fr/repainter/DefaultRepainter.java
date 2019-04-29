package fr.repainter;

import javax.swing.JPanel;

import fr.statepanel.Repainter;

public class DefaultRepainter implements Repainter {

	private static final int DEFAULT_RATE = 17;

	private Integer rate;

	private JPanel panel;

	private final Thread myThread;

	private boolean repaint;

	private final Object waiter;

	public DefaultRepainter() {
		super();
		this.myThread = new Thread(this);
		this.rate = DEFAULT_RATE;
		this.panel = null;
		this.repaint = true;
		this.waiter = new Object();
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
		this.myThread.interrupt();
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
