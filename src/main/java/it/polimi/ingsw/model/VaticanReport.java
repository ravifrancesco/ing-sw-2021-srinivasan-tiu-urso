package it.polimi.ingsw.model;

public class VaticanReport {

    protected final int start;
    protected final int end;

    protected final int popeSpace;

    protected final int winningPoints;

    private boolean achieved;

    public VaticanReport(int start, int end, int popeSpace, int winningPoints) {
        this.start = start;
        this.end = end;
        this.popeSpace = popeSpace;
        this.winningPoints = winningPoints;
        this.achieved = false;
    }

    public void achieve() {
        this.achieved = true;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
