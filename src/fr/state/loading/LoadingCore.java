package fr.state.loading;

import java.awt.Color;
import java.util.Map;

import fr.server.p2p.Multiplayer;
import fr.server.p2p.PData;
import fr.server.p2p.PData.OP;
import fr.server.p2p.PDataProcessor;
import fr.util.point.Point;

public class LoadingCore implements PDataProcessor {

	Map<Integer, Integer> linkIDPorts;
	LoadingRequestor requestor;
	Multiplayer multiplayer;

	int myId;

	Map<Integer, PlayerData> players;

	public LoadingCore(Multiplayer m, Map<Integer, Integer> linkIDPorts, LoadingRequestor requestor) {
		this.linkIDPorts = linkIDPorts;
		this.requestor = requestor;

		this.multiplayer = m;
		this.multiplayer.setPDataProcessor(this);
	}

	@Override
	public void newPData(PData pdata) {
		OP op = pdata.getOpId();

		if (pdata.getId() != this.myId) {
			if (op == OP.PLAYER_STATE) {
				Object[] data = pdata.getData();

				int i = 0;

				this.players.put(pdata.getId(), new PlayerData(pdata.getId(), (String) data[i++], (Point) data[i++],
						(Point) data[i++], (Color) data[i++]));

				this.linkIDPorts.remove(pdata.getId());
			}
		}
	}

	public void start() {
		this.multiplayer.start();
		this.requestor.start();
	}

	public void stop() {
		this.multiplayer.stop();
		this.requestor.stop();
	}
}