package fr.sigmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire de signal
 *
 * @author Loïc
 *
 */
public class SignalManager {

	/**
	 * static Singleton instance.
	 */
	private static volatile SignalManager instance;

	// Map : key = signalName / value = signal
	private Map<String, Object> signals;

	// Map : key = signalName / value = liste des threads lies
	private Map<String, List<String>> linkedThreads;

	// Signaux verouilles
	private List<String> lockedSignals;

	/**
	 * Private constructor for singleton.
	 */
	private SignalManager() {
		this.signals = new HashMap<>();
		this.linkedThreads = new HashMap<>();
		this.lockedSignals = new ArrayList<>();
	}

	/**
	 * Lie un thread a un signal
	 *
	 * @param sigName    : nom du signal
	 * @param threadName : nom du thread a lie
	 */
	public void addLinkedThread(String sigName, String threadName) {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName)) {
				synchronized (this.linkedThreads) {

					ThreadManager threadManager = ThreadManager.getInstance();

					if (threadManager.contains(threadName)) {

						this.linkedThreads.get(sigName).add(threadName);
						threadManager.addLinkedSignal(threadName, sigName);

					} else
						throw new RuntimeException("Le thread n'existe pas");

				}
			} else
				throw new RuntimeException("Le signal n'existe pas");
		}
	}

	/**
	 * Ajoute un signal
	 *
	 * @param sigName : nom du signal
	 * @throws RuntimeException
	 */
	public Object addSignal(String sigName) throws RuntimeException {
		synchronized (this.signals) {

			if (!this.signals.containsKey(sigName)) {

				Object signal = new Object();

				this.signals.put(sigName, signal);

				synchronized (this.linkedThreads) {
					this.linkedThreads.put(sigName, new ArrayList<>());
				}

				return signal;
			} else
				throw new RuntimeException("Le signal existe deja");
		}
	}

	/**
	 * recupere un signal
	 *
	 * @param sigName : nom du signal
	 * @return : le signal recupere
	 * @throws RuntimeException
	 */
	public Object get(String sigName) throws RuntimeException {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName))

				return this.signals.get(sigName);

			else
				throw new RuntimeException("Le signal n'existe pas");
		}
	}

	/**
	 * Recupere le nom des thread lies au signal
	 *
	 * @param sigName : nom du signal
	 * @return la liste de nom des threads lies
	 * @throws RuntimeException
	 */
	public List<String> getLinkedThreadsName(String sigName) throws RuntimeException {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName)) {
				synchronized (this.linkedThreads) {
					if (this.linkedThreads.containsKey(sigName))
						return this.linkedThreads.get(sigName);
					else
						throw new RuntimeException("Le signal n'a aucun threads lie");
				}
			} else
				throw new RuntimeException("Le signal n'existe pas");
		}
	}

	/**
	 * Test le sttatut "vide" de la map
	 */
	public boolean isEmpty() {
		return this.signals.isEmpty();
	}

	/**
	 * Test si un signal est verouille
	 *
	 * @param sigName : nom du signal
	 *
	 * @return true si le signal est verouille
	 */
	public boolean isLocked(String sigName) {
		synchronized (this.lockedSignals) {
			return this.lockedSignals.contains(sigName);
		}
	}

	/**
	 * Verouille un signal, il ne sera pas supprime par un reset
	 *
	 * @param sigName : signal a verouiller
	 */
	public void lock(String sigName) {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName)) {
				synchronized (this.lockedSignals) {
					if (!this.lockedSignals.contains(sigName)) {

						this.lockedSignals.add(sigName);

					} else
						throw new RuntimeException("Le signal " + sigName + " est deja verouille");
				}
			} else
				throw new RuntimeException("Le signal " + sigName + " n'existe pas");
		}
	}

	/**
	 * Supprime le signal et les threads associes
	 *
	 * @param sigName : nom du signal
	 * @throws RuntimeException : le signal n'existe pas
	 */
	public void remove(String sigName) throws RuntimeException {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName)) {

				ThreadManager threadManager = ThreadManager.getInstance();

				for (String threadName : this.linkedThreads.get(sigName)) {
					this.removeLinkedThread(sigName, threadName);

					threadManager.remove(threadName);
				}

				this.signals.remove(sigName);
			} else
				throw new RuntimeException("Le signal n'existe pas");
		}
	}

	/**
	 * Supprime le lien entre un signal et un thread
	 *
	 * @param sigName    : nom du signal
	 * @param threadName : nom du thread lie
	 */
	public void removeLinkedThread(String sigName, String threadName) {
		synchronized (this.signals) {
			if (this.signals.containsKey(sigName)) {
				synchronized (this.linkedThreads) {
					if (this.linkedThreads.get(sigName).contains(threadName)) {

						this.linkedThreads.get(sigName).remove(threadName);
						ThreadManager.getInstance().removeLinkedSignal(threadName);

					} else
						throw new RuntimeException("Le thread n'est pas associe au signal");
				}
			} else
				throw new RuntimeException("Le signal n'existe pas");
		}
	}

	/**
	 * Supprime tous les signaux et les threads associes a moins que le signal soit
	 * verouille
	 */
	public void reset() throws RuntimeException {
		synchronized (this.signals) {
			synchronized (this.lockedSignals) {
				if (!this.isEmpty()) {

					for (String sigName : this.signals.keySet()) {
						if (!this.lockedSignals.contains(sigName)) {
							this.remove(sigName);
						}
					}

				} else
					throw new RuntimeException("Il n'y a aucun signal");
			}
		}
	}

	/**
	 * Deverouille un signal, il sera supprime lors d'un reset
	 *
	 * @param sigName : nom du signal
	 */
	public void unlock(String sigName) {
		synchronized (this.lockedSignals) {
			if (this.lockedSignals.contains(sigName)) {

				this.lockedSignals.remove(sigName);

			} else
				throw new RuntimeException("Le signal " + sigName + " n'est pas verouille");
		}
	}

	/**
	 * Return a singleton instance of SignalManager.
	 */
	public static SignalManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (SignalManager.class) {
				if (instance == null) {
					instance = new SignalManager();
				}
			}
		}
		return instance;
	}
}
