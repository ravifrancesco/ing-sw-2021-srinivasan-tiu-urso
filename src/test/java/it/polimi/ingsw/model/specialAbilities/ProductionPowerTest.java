package it.polimi.ingsw.model.specialAbilities;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductionPowerTest {

    @Test
    public void constructorTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Assert.assertEquals(p.getNumberFaithPoints(), 2);
        Assert.assertEquals(p.getResourceRequired(), resourceRequired);
        Assert.assertEquals(p.getResourceProduced(), resourceProduced);
        Assert.assertTrue(p.isActivatable());
        Assert.assertEquals(p.getType(), SpecialAbilityType.PRODUCTION_POWER);
    }

    @Test
    public void toStringTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;RP=SHIELD:1;FP=2;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;RP=SHIELD:1;FP=2;";

        Assert.assertTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void toStringWithoutRPTest() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        ProductionPower p = new ProductionPower(resourceRequired, null,2);

        String s1 = "SA=PP;RR=STONE:1,GOLD:1;FP=2;";
        String s2 = "SA=PP;RR=GOLD:1,STONE:1;FP=2;";

        Assert.assertTrue(s1.equals(p.toString()) || s2.equals(p.toString()));
    }

    @Test
    public void isActivatableTrueWithoutSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.STONE, 3);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertTrue(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableFalseWithoutSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SERVANT, 3);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertFalse(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableTrueWithSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 2);

        Assert.assertTrue(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableFalseWithSelectableResources() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 1);

        Assert.assertFalse(p.isActivatable(playerResources));
    }

    @Test
    public void isActivatableExceptionTest1() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceRequiredSelectable = new HashMap<>();

        resourceRequiredSelectable.put(Resource.GOLD, 1);
        resourceRequiredSelectable.put(Resource.STONE, 1);

        Optional<Map<Resource, Integer>> resourceRequiredOptional = Optional.of(resourceRequiredSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(resourceRequiredOptional, emptyOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 2);
        playerResources.put(Resource.SHIELD, 1);

        try{
            p.isActivatable(playerResources);
        } catch(IllegalArgumentException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void isActivatableExceptionTest2() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Map<Resource, Integer> resourceProducedSelectable = new HashMap<>();

        resourceProducedSelectable.put(Resource.GOLD, 1);
        resourceProducedSelectable.put(Resource.STONE, 1);

        Optional<Map<Resource, Integer>> resourceProducedOptional = Optional.of(resourceProducedSelectable);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        p.setSelectableResource(emptyOptional, resourceProducedOptional);

        Map<Resource, Integer> playerResources = new HashMap<>();

        playerResources.put(Resource.GOLD, 3);
        playerResources.put(Resource.SHIELD, 3);
        playerResources.put(Resource.STONE, 1);
        playerResources.put(Resource.SERVANT, 2);

        try{
            p.isActivatable(playerResources);
        } catch(IllegalArgumentException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidRequiredMapTest() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SERVANT, 3);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        try{
            p.setSelectableResource(emptyOptional, emptyOptional);
        } catch(IllegalStateException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidProducedMapTest() {
        int thrownExceptions = 0;

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.SHIELD, 2);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        Optional<Map<Resource, Integer>> emptyOptional = Optional.empty();

        try{
            p.setSelectableResource(emptyOptional, emptyOptional);
        } catch(IllegalStateException e) {
            thrownExceptions++;
        }

        Assert.assertEquals(thrownExceptions, 1);
    }
}
