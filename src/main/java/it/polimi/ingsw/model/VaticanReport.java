package it.polimi.ingsw.model;

public class VaticanReport {

    protected final int start;
    protected final int end;

    protected final int popeSpace;

    protected final int victoryPoints;

    private boolean reached;
    private boolean achieved;

    public VaticanReport(int start, int end, int popeSpace, int victoryPoints) {
        this.start = start;
        this.end = end;
        this.popeSpace = popeSpace;
        this.victoryPoints = victoryPoints;
        this.reached = false;
        this.achieved = false;
    }

    public void reach() {
        this.reached = true;
    }

    public void achieve() {
        this.achieved = true;
    }

    public boolean isReached() {
        return reached;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
