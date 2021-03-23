package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.Map;

public class FaithTrack {

	static final int LENGTH = 25;

	private int position;

	private VaticanReport[] vaticanReports;
	private Map<Integer, Integer> faithTrackVictoryPoints;

	private int winningPoints;

	public FaithTrack(GameSettings gameSettings) {
		this.winningPoints = 0;
		this.position = 0;
		this.vaticanReports = gameSettings.getVaticanReports();
		this.faithTrackVictoryPoints = gameSettings.getFaithTrackVictoryPoints();
	}

	public boolean moveFaithMarker(int pos) {
		for (int i = 0; i < pos; i++) {
			position++;
			winningPoints += faithTrackVictoryPoints.get(position) == null ? 0 : faithTrackVictoryPoints.get(position);
		}

		Arrays.stream(vaticanReports).filter(v -> v.start <= position).forEach(VaticanReport::reach);

	}


	public int getPosition() {
		return position;
	}

	public int getWinningPoints() {
		return winningPoints;
	}

}
