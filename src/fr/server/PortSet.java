package fr.server;

public class PortSet {

	int clientServer, serverClient, linkerSearcher, searcherLinker;

	public PortSet(int clientServer, int serverClient, int linkerSearcher, int searcherLinker) {
		super();
		this.clientServer = clientServer;
		this.serverClient = serverClient;
		this.linkerSearcher = linkerSearcher;
		this.searcherLinker = searcherLinker;
	}

	public int getClientServer() {
		return this.clientServer;
	}

	public int getLinkerSearcher() {
		return this.linkerSearcher;
	}

	public int getSearcherLinker() {
		return this.searcherLinker;
	}

	public int getServerClient() {
		return this.serverClient;
	}

	public void setClientServer(int clientServer) {
		this.clientServer = clientServer;
	}

	public void setLinkerSearcher(int linkerSearcher) {
		this.linkerSearcher = linkerSearcher;
	}

	public void setSearcherLinker(int searcherLinker) {
		this.searcherLinker = searcherLinker;
	}

	public void setServerClient(int serverClient) {
		this.serverClient = serverClient;
	}
}
