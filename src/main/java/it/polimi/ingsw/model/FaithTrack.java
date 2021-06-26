package it.polimi.ingsw.model;

import it.polimi.ingsw.model.observerPattern.observables.FaithTrackObservable;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents the Faith Track presents in the user dashboard.
 * The object memorizes the state of the Faith Track. The state includes:
 * <ul>
 * <li> The red cross position.
 * <li> The vatican report zones.
 * <li> The bonus victory points in the faith track.
 * <li> The total victory points accumulated by the faith track.
 * </ul>
 * <p>
 *
 * The class is observable and notifies the observers on a change of the state.
 */
public class FaithTrack extends FaithTrackObservable {

	private int position;

	private Map<Integer, VaticanReport> vaticanReports;
	private int[] faithTrackVictoryPoints;

	private int victoryPoints;

	private Dashboard dashboard;

	public static int maxReached;

	/**
	 * The constructor for a FaithTrack object.
	 * It initializes the winning points and the user position
	 * and load the vatican reports settings and the faith track
	 * bonus victory points from the gameSettings object.
	 *
	 * @param gameSettings the settings for the current game.
	 */
	public FaithTrack(GameSettings gameSettings, Dashboard dashboard) {
		this.victoryPoints = 0;
		this.position = 0;
		this.vaticanReports = gameSettings.getVaticanReports().stream()
				.collect(Collectors.toMap(v -> v.end, VaticanReport::copy));
		this.faithTrackVictoryPoints = gameSettings.getFaithTrackVictoryPoints();
		this.dashboard = dashboard;
	}

	/**
	 * Resets the state of this object to the initial state.
	 */
	public void reset() {
		this.victoryPoints = 0;
		this.position = 0;
		vaticanReports.values().forEach(VaticanReport::reset);
		FaithTrack.maxReached = 0;
		notify(this);
	}

	/**
	 * Moves the red cross by pos positions on the faith track, while
	 * checking and eventually adding if the cross steps on a bonus cell.
	 *
	 * @param pos	number of positions for the movement.
	 */

	public void moveFaithMarker(int pos) {
		for (int i = 1; i <= pos; i++) {
			if (position == GameSettings.FAITH_TRACK_LENGTH - 1) {
				notify(this);
				return;
			}
			position++;
			updateMaxReached(position);
			updateVaticanReports();
			victoryPoints += faithTrackVictoryPoints[position];
		}
		notify(this);
	}

	private void updateMaxReached(int newPosition) {
		if (newPosition > FaithTrack.maxReached) {
			FaithTrack.maxReached = newPosition;
		}
	}

	private void updateVaticanReports() {
		vaticanReports.forEach((k,v) -> {
			if (FaithTrack.maxReached == k) {
				checkVaticanVictoryPoints(k);
			}
		});
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
	public void checkVaticanVictoryPoints(int vaticanReportEnd) {
		VaticanReport currentVaticanReport;
		for (int i = 0; i < vaticanReportEnd; i++) {
			currentVaticanReport = vaticanReports.get(i);
			if (currentVaticanReport != null) {
				if (position < currentVaticanReport.start) {
					currentVaticanReport.miss();
				} else {
					currentVaticanReport.achieve();
					victoryPoints += currentVaticanReport.victoryPoints;
				}
			}
		}
		notify(this);
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
	 * Getter for the victoryPoints
	 *
	 * @return the current victoryPoints
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	//TODO doc
	public int[] getFaithTrackVictoryPoints() {
		return faithTrackVictoryPoints;
	}

	/**
	 * Getter for vaticanReports
	 *
	 * @return list of vaticanReports
	 */
	public Map<Integer, VaticanReport> getVaticanReports() {
		return vaticanReports;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}
}
