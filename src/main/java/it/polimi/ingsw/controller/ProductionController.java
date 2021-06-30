package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.PowerNotActivatableException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.specialAbilities.SpecialAbility;
import it.polimi.ingsw.model.full.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.utils.ResourceContainer;

import java.util.Map;

public class ProductionController {

    private final Game game;

    /**
     * Constructor for a Production Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public ProductionController(Game game) {
        this.game = game;
    }

    /**
     * // @see ServerController#activateLeaderCardProduction(String, int, ResourceContainer, Map, Map)
     */

    public void activateLeaderCardProduction(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        SpecialAbility specialAbility;

        try {
            specialAbility = dashboard.getLeaderCard(cardToActivate).getSpecialAbility();
        } catch (IllegalArgumentException e) {
            throw new PowerNotActivatableException();
        } catch (NullPointerException e) {
            throw new PowerNotActivatableException();
        }

        if (!specialAbility.getType().equals(SpecialAbilityType.PRODUCTION_POWER)) {
            throw new PowerNotActivatableException();
        }

        ProductionPower productionPower = (ProductionPower) specialAbility;

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException();
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException();
        }

        try {
            dashboard.simulatePayment(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

        try {
            productionPower.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException();
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException();
        }

        dashboard.payPrice(resourcesToPayCost);
    }

    /**
     // @see ServerController#activateDashboardProduction(String, ResourceContainer, Map, Map)
     */

    public void activateDashboardProduction(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        ProductionPower productionPower = dashboard.getDashBoardProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable()) {
            throw new PowerNotActivatableException();
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException();
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException();
        }

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException();
        }

        try {
            dashboard.simulatePayment(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

        try {
            productionPower.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException();
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException();
        }

        dashboard.payPrice(resourcesToPayCost);

    }

    /**
     * @see Controller#activateDevelopmentCardProductionPower(String, int, ResourceContainer, Map, Map)
     */

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
        // } else if (!game.getTurnPhase().equals(TurnPhase.COMMON) || !game.getTurnPhase().equals(TurnPhase.PRODUCTION)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (cardToActivate < 0 || cardToActivate > 2 || dashboard.getDevelopmentCard(cardToActivate) == null) {
            throw new PowerNotActivatableException();
        }

        DevelopmentCard developmentCard = dashboard.getDevelopmentCard(cardToActivate);
        ProductionPower productionPower = developmentCard.getProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException();
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException();
        }

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException();
        }

        try {
            dashboard.simulatePayment(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

        try {
            developmentCard.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException();
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException();
        }

        dashboard.payPrice(resourcesToPayCost);

    }
}
