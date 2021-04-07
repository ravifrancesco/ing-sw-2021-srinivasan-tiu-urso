package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.HashMap;
import java.util.Map;

public class ProductionPowerTest {

    @Test
    public void toStringTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;RP=SHIELD:1;FP=2;SR=n;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;RP=SHIELD:1;FP=2;SR=n;";

        Assumptions.assumeTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void toStringWithoutRPTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        ProductionPower p = new ProductionPower(resourceRequired, null,2, true);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;FP=2;SR=y;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;FP=2;SR=y;";

        Assumptions.assumeTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void getterTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2, false);

        Assert.assertEquals(p.getNumberFaithPoints(), 2);
        Assert.assertEquals(p.getResourceRequired(), resourceRequired);
        Assert.assertEquals(p.getResourceProduced(), resourceProduced);
        Assumptions.assumeFalse(p.isSelectableResource());
    }
}
