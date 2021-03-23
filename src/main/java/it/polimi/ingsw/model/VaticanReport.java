package it.polimi.ingsw.model;

/**
 * This class represents a Vatican Report Zone that is contained in the
 * Faith Track. It contains variables describing the vatican report zone.
 * The object memorizes the state of the Vatican Report Zone. The state includes:
 * <ul>
 * <li> A boolean "missed" that describes if the victory point card was discarded.
 * <li> A boolean "achieve" that describes if the victory point card was flipped.
 * </ul>
 * <p>
 */
public class VaticanReport implements Cloneable {

    protected final int start;
    protected final int end;

    protected final int victoryPoints;

    private boolean missed;
    private boolean achieved;

    /**
     * The constructor for a VaticanReport object. It sets the missed and achieved
     * to false.
     *
     * @param start             the start of the vatican report zone.
     * @param end               the end of the vatican report zone (also the checkpoint).
     * @param victoryPoints     the victory points obtained if the the card is flipped.
     */
    public VaticanReport(int start, int end, int victoryPoints) {
        this.start = start;
        this.end = end;
        this.victoryPoints = victoryPoints;
        this.missed = false;
        this.achieved = true;
    }

    /**
     * Resets the object to the initial state: missed and achieved are set to false.
     */
    public void reset() {
        this.missed = false;
        this.achieved = true;
    }

    /**
     * To be called if another player reaches the checkpoint for this zone
     * before this player has reached the start of the zone. missed is set
     * to false, representing the bonus victory points card being discarded.
     */
    public void miss() {
        this.missed = true;
    }

    /**
     * To be called if another player reaches the checkpoint for this zone
     * and this player has reached the start of the zone. achieved is set
     * to true, representing the bonus victory points card being flipped.
     */
    public void achieve() {
        this.achieved = true;
    }

    /**
     * Getter for missed.
     *
     * @return  <code>true</code> if the player has discarded
     *          the bonus victory points card;
     *          <code>false</code> otherwise.
     */
    public boolean isMissed() {
        return missed;
    }

    /**
     * Getter for achieved.
     *
     * @return  <code>true</code> if the player has flipped
     *          the bonus victory points card;
     *          <code>false</code> otherwise.
     */
    public boolean isAchieved() {
        return achieved;
    }

    /**
     * Clones the current object
     *
     * @return a clone of <code>this</code>
     */
    @Override
    protected VaticanReport clone() {
        return new VaticanReport(start, end, victoryPoints);
    }

}
