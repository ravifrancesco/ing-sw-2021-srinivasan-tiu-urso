package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

public class VaticanReportTest {

    @Test
    public void constructorTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        Assumptions.assumeFalse(vaticanReport.isMissed());
        Assumptions.assumeFalse(vaticanReport.isAchieved());
        Assert.assertEquals(0, vaticanReport.start);
        Assert.assertEquals(25, vaticanReport.end);
        Assert.assertEquals(5, vaticanReport.victoryPoints);

    }

    @Test
    public void missTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        vaticanReport.miss();
        Assumptions.assumeTrue(vaticanReport.isMissed());
        Assumptions.assumeFalse(vaticanReport.isAchieved());

    }

    @Test
    public void achieveTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        vaticanReport.achieve();
        Assumptions.assumeFalse(vaticanReport.isMissed());
        Assumptions.assumeTrue(vaticanReport.isAchieved());

    }

    @Test
    public void resetTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        vaticanReport.miss();
        vaticanReport.achieve();
        vaticanReport.reset();
        Assumptions.assumeFalse(vaticanReport.isMissed());
        Assumptions.assumeFalse(vaticanReport.isAchieved());

    }

    @Test
    public void cloneTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        VaticanReport cloneVaticanReport = vaticanReport.copy();

        Assumptions.assumeFalse(vaticanReport == cloneVaticanReport);

        Assumptions.assumeFalse(cloneVaticanReport.isMissed());
        Assumptions.assumeFalse(cloneVaticanReport.isAchieved());
        Assert.assertEquals(0, cloneVaticanReport.start);
        Assert.assertEquals(25, cloneVaticanReport.end);
        Assert.assertEquals(5, cloneVaticanReport.victoryPoints);

    }




}
