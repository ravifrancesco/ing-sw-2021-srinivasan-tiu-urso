package it.polimi.ingsw.model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents the Faith Track presents in the user dashboard.
 * The object memorizes the state of the Faith Track. The state includes:
 * <ul>
 * <li> The red cross position.
 * <li> The vatican report zones.
 * <li> The bonus victory points in the faith track.
 * <li> The total winning points accumulated by the faith track.
 * </ul>
 * <p>
 */
public class FaithTrack {

	static final int LENGTH = 25;

	private int position;

	private Map<Integer, VaticanReport> vaticanReports;
	private Map<Integer, Integer> faithTrackVictoryPoints;

	private int winningPoints;

	/**
	 * The constructor for a FaithTrack object.
	 * It initializes the winning points and the user position
	 * and load the vatican reports settings and the faith track
	 * bonus victory points from the gameSettings object.
	 *
	 * @param gameSettings the settings for the current game.
	 */
	public FaithTrack(GameSettings gameSettings) {
		this.winningPoints = 0;
		this.position = 0;
		this.vaticanReports = gameSettings.getVaticanReports().stream()
				.collect(Collectors.toMap(v -> v.end, VaticanReport::clone));
		this.faithTrackVictoryPoints = gameSettings.getFaithTrackVictoryPoints();
	}

	/**
	 * Resets the state of this object to the initial state.
	 */
	public void reset() {
		this.winningPoints = 0;
		this.position = 0;
		vaticanReports.values().forEach(VaticanReport::reset);
	}

	/**
	 * Moves the red cross by pos positions on the faith track, while
	 * checking and eventually adding if the cross steps on a bonus cell.
	 *
	 * @param pos	number of positions for the movement.
	 * @return		<code>true</code> if the player has reached the
	 * 				last cell;
	 * 				<code>false</code> otherwise.
	 */
	public boolean moveFaithMarker(int pos) {
		for (int i = 0; i < pos; i++) {
			if (position == LENGTH) {
				return true;
			} else {
				position++;
				winningPoints += faithTrackVictoryPoints.get(position) == null ? 0 : faithTrackVictoryPoints.get(position);
			}
		}
		return false;
	}

	/**
	 * To be called when a player reaches a vatican checkpoint cell.
	 * This methods checks if the cross has reached the start of
	 * the vatican report zone described by the checkpoint that is
	 * passes to the method. In case it has, it will add the points
	 * to the winningPoints and set the vaticanReport to achieved,
	 * otherwise it will set the vatican report to missed.
	 *
	 * @param vaticanReportEnd checkpoint of the vatican report zone
	 *                         to be checked.
	 */
	public void getVaticanVictoryPoints(int vaticanReportEnd) {
		VaticanReport currentVaticanReport = vaticanReports.get(vaticanReportEnd);
		if (position < currentVaticanReport.start) { currentVaticanReport.isMissed(); }
		else { currentVaticanReport.isAchieved(); }
	}

	/**
	 * Getter for the position
	 *
	 * @return the current red cross position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Getter for the winningPoints
	 *
	 * @return the current winningPoints
	 */
	public int getWinningPoints() {
		return winningPoints;
	}

}
