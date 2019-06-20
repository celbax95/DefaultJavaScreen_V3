package fr.sigmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire de thread
 *
 * @author Loïc
 *
 */
public class ThreadManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile ThreadManager instance;

	// Map : key = threadName / value = thread
	private Map<String, Thread> threads;

	// Map : key = threadName / value = signalName
	private Map<String, String> linkedSignals;

	// Threads verouilles
	private List<String> lockedThreads;

	/**
	 * Private constructor for singleton.
	 */
	private ThreadManager() {
		this.threads = new HashMap<>();
		this.linkedSignals = new HashMap<>();
		this.lockedThreads = new ArrayList<>();
	}

	/**
	 * Ajoute un thread au gestionnaire
	 *
	 * @param threadName : nom du thread
	 * @param newThread  : le thread a ajouter
	 */
	public void add(String threadName, Thread newThread) {
		synchronized (this.threads) {
			if (!this.threads.containsKey(threadName)) {
				this.threads.put(threadName, newThread);
			} else
				throw new RuntimeException("Le thread " + threadName + " existe deja");
		}
	}

	/**
	 * Lie un thread a un signal
	 *
	 * @param threadName : nom du thread
	 * @param sigName    : nom du signal a lie
	 */
	protected void addLinkedSignal(String threadName, String sigName) {
		synchronized (this.threads) {
			if (this.threads.containsKey(threadName)) {
				synchronized (this.linkedSignals) {

					if (!this.linkedSignals.containsKey(threadName)) {

						this.linkedSignals.put(threadName, sigName);

					} else
						throw new RuntimeException(
								"Le signal " + sigName + " a deja ete ajoute au thread " + threadName);
				}
			} else
				throw new RuntimeException("Le thread " + threadName + " n'existe pas");
		}
	}

	/**
	 * Test si le manager contien un thread
	 *
	 * @param threadName : nom du thread
	 * @return true si le gestionnaire contien le thread, false sinon
	 */
	public boolean contains(String threadName) {
		return this.contains(threadName);
	}

	/**
	 * Recupere le nom de chaque thread
	 *
	 * @return la liste du nom des threads
	 */
	public List<String> getThreadsName() {
		return new ArrayList<>(this.threads.keySet());
	}

	/**
	 * Test si le gestionnaire est vide
	 *
	 * @return true si le getsionnaire est vide, false sinon
	 */
	public boolean isEmpty() {
		return this.threads.isEmpty();
	}

	/**
	 * Test si un thread est verouille
	 *
	 * @param threadName : nom du thread
	 *
	 * @return true si le thread est verouille
	 */
	public boolean isLocked(String threadName) {
		return this.lockedThreads.contains(threadName);
	}

	/**
	 * Verouille un thread, il ne sera pas supprime par un reset
	 *
	 * @param threadName : thread a verouiller
	 */
	public void lock(String threadName) {
		synchronized (this.threads) {
			if (this.threads.containsKey(threadName)) {
				synchronized (this.lockedThreads) {
					if (!this.lockedThreads.contains(threadName)) {

						this.lockedThreads.add(threadName);

					} else
						throw new RuntimeException("Le thread " + threadName + " est deja verouille");
				}
			} else
				throw new RuntimeException("Le thread " + threadName + " n'existe pas");
		}
	}

	/**
	 * Arrete et supprime un thread du gestionnaire
	 *
	 * @param threadName : nom du thread
	 */
	public void remove(String threadName) {
		synchronized (this.threads) {
			if (this.threads.containsKey(threadName)) {
				Thread toRemove = this.threads.get(threadName);

				toRemove.interrupt();

				this.threads.remove(threadName);

				if (this.isLocked(threadName)) {
					this.unlock(threadName);
				}

				synchronized (this.linkedSignals) {
					if (this.linkedSignals.containsKey(threadName)) {
						String linkedSignal = this.linkedSignals.get(threadName);
						SignalManager.getInstance().removeLinkedThread(linkedSignal, threadName);
					}
				}

			} else
				throw new RuntimeException("Le thread " + threadName + " n'existe pas");
		}
	}

	/**
	 * Supprime le lien a un thread
	 *
	 * @param threadName : nom du thread
	 */
	protected void removeLinkedSignal(String threadName) {
		synchronized (this.linkedSignals) {

			if (this.linkedSignals.containsKey(threadName)) {
				this.linkedSignals.remove(threadName);

			} else
				throw new RuntimeException("Le thread " + threadName + " n'a pas de signal lie");
		}
	}

	/**
	 * Supprime tous les threads a moins qu'il soit verouille
	 */
	public void reset() {
		synchronized (this.threads) {
			synchronized (this.lockedThreads) {
				for (String threadName : this.threads.keySet()) {
					if (!this.lockedThreads.contains(threadName)) {
						this.remove(threadName);
					}
				}
			}
		}
	}

	/**
	 * Deverouille un thread, il sera supprime lors d'un reset
	 *
	 * @param threadName : nom du thread
	 */
	public void unlock(String threadName) {
		synchronized (this.lockedThreads) {
			if (this.lockedThreads.contains(threadName)) {

				this.lockedThreads.remove(threadName);

			} else
				throw new RuntimeException("Le thread " + threadName + " n'est pas verouille");
		}
	}

	/**
	 * Return a singleton instance of ThreadManager.
	 */
	public static ThreadManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (ThreadManager.class) {
				if (instance == null) {
					instance = new ThreadManager();
				}
			}
		}
		return instance;
	}
}
