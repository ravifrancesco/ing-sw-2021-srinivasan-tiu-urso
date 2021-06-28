package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.table.VaticanReport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

public class VaticanReportTest {

    @Test
    public void constructorTest() {

        VaticanReport vaticanReport = new VaticanReport(0, 25, 5);
        Assumptions.assumeFalse(vaticanReport.isMissed());
        Assumptions.assumeFalse(vaticanReport.isAchieved());
        Assert.assertEquals(0, vaticanReport.getStart());
        Assert.assertEquals(25, vaticanReport.getEnd());
        Assert.assertEquals(5, vaticanReport.getVictoryPoints());

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
        Assert.assertEquals(0, cloneVaticanReport.getStart());
        Assert.assertEquals(25, cloneVaticanReport.getEnd());
        Assert.assertEquals(5, cloneVaticanReport.getVictoryPoints());

    }




}
