package fr.repainter;

import javax.swing.JPanel;

import fr.statepanel.Repainter;

public class DefautRepainter implements Repainter {

	private static final int DEFAULT_RATE = 17;

	private Integer rate;

	private JPanel panel;

	private Thread myThread;

	private Boolean repaint;

	public DefautRepainter() {
		super();
		this.myThread = new Thread();
		this.rate = DEFAULT_RATE;
		this.panel = null;
	}

	public DefautRepainter(JPanel requestor) {
		this();
		this.panel = requestor;
	}

	public DefautRepainter(JPanel requestor, int rate) {
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

		synchronized (this.repaint) {
			this.repaint.notifyAll();
		}
	}

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			while (this.rate < 0 && !this.repaint) {
				synchronized (this.repaint) {
					try {
						this.repaint.wait();
					} catch (InterruptedException e) {
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
				} catch (InterruptedException e) {
				}
			}
		}

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
