package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.PowerNotActivatableException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.specialAbilities.SpecialAbility;
import it.polimi.ingsw.model.specialAbilities.SpecialAbilityType;

import java.util.Map;

public class ProductionController {

    private final Game game;

    private String currentPlayer;

    /**
     * Constructor for a Production Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public ProductionController(Game game) {
        this.game = game;
    }

    /**
     * Setter for the current player.
     * @param currentPlayer the current player of the game.
     */

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @see ServerController#activateLeaderCardProductionPower(String, int, ResourceContainer, Map, Map)
     */

    public void activateLeaderCardProduction(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!currentPlayer.equals(nickname)) {
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
            throw new PowerNotActivatableException("Invalid index");
        } catch (NullPointerException e) {
            throw new PowerNotActivatableException("Card is null");
        }

        if (!specialAbility.getType().equals(SpecialAbilityType.PRODUCTION_POWER)) {
            throw new PowerNotActivatableException("Card doesn't have a production power special ability");
        }

        ProductionPower productionPower = (ProductionPower) specialAbility;

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException("Illegal amount of required/produced resources");
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException("Selectable resources setting failed");
        }

        try {
            productionPower.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException("Not enough resources");
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException("Production already activated");
        }

        try {
            dashboard.payPrice(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }
    }

    /**
     * @see ServerController#activateDashboardProductionPower(String, ResourceContainer, Map, Map)
     */

    public void activateDashboardProduction(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!(game.getTurnPhase().equals(TurnPhase.COMMON) || game.getTurnPhase().equals(TurnPhase.PRODUCTION))) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();
        ProductionPower productionPower = dashboard.getDashBoardProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable()) {
            throw new PowerNotActivatableException("Production already activated");
        }
        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException("Illegal amount of required/produced resources");
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException("Selectable resources setting failed");
        }

        try {
            productionPower.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException("Not enough resources");
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException("Production already activated");
        }

        try {
            dashboard.payPrice(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

    }

    /**
     * @see ServerController#activateDevelopmentCardProductionPower(String, int, ResourceContainer, Map, Map)
     */

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, PowerNotActivatableException, WrongMoveException {

        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON) || !game.getTurnPhase().equals(TurnPhase.PRODUCTION)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);
        Dashboard dashboard = player.getDashboard();

        if (cardToActivate < 0 || cardToActivate > 2 || dashboard.getDevelopmentCard(cardToActivate) == null) {
            throw new PowerNotActivatableException("Invalid index");
        }

        DevelopmentCard developmentCard = dashboard.getDevelopmentCard(cardToActivate);
        ProductionPower productionPower = developmentCard.getProductionPower();
        Map<Resource, Integer> playerResources = dashboard.getAllPlayerResources();

        if (!productionPower.isActivatable(playerResources)) {
            throw new PowerNotActivatableException("Not enough resources");
        }

        if (game.getTurnPhase().equals(TurnPhase.COMMON)) {
            game.startUniquePhase(TurnPhase.PRODUCTION);
        }

        try {
            productionPower.setSelectableResource(resourceRequiredOptional, resourceProducedOptional);
        } catch(IllegalArgumentException e) {
            throw new PowerNotActivatableException("Illegal amount of required/produced resources");
        } catch(IllegalStateException e) {
            throw new PowerNotActivatableException("Selectable resources setting failed");
        }

        try {
            developmentCard.activate(player);
        } catch (IllegalStateException e) {
            throw new PowerNotActivatableException("Not enough resources");
        } catch (UnsupportedOperationException e) {
            throw new PowerNotActivatableException("Production already activated");
        }

        try {
            dashboard.payPrice(resourcesToPayCost, productionPower.getResourceRequiredModified());
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Resources do not match the cost");
        }

    }
}
