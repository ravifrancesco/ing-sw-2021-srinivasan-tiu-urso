package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;
import it.polimi.ingsw.utils.Pair;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLobby implements Lobby {

    private final String id;

    private final int maxPlayers;
    private final Map<String, Connection> connectedPlayers;


    private final ServerController serverController;
    private GameSettings gameSettings;

    public GameLobby(String id, GameSettings gameSettings, int maxPlayers) throws IllegalArgumentException {
        this.id = id;
        this.gameSettings = gameSettings;
        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.serverController = new ServerController(id, maxPlayers);
    }

    @Override
    public void handleMessage(ClientMessage clientMessage, Connection c) {
        // TODO
    }

    public void enterLobby(Connection c) throws InvalidNameException, IllegalStateException {
        try {
            serverController.loadGameSettings(gameSettings);
            serverController.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);
        c.enterLobby(this);
        System.out.println(connectedPlayers);
        serverController.addObservers(c);
    }

    public void leaveLobby(Connection c) {
        if (connectedPlayers.containsValue(c)) {
            connectedPlayers.remove(c.getNickname());
            serverController.removeObservers(c);
        }
    }

    public void loadGameSettings(GameSettings gameSettings) {
        serverController.loadGameSettings(gameSettings);
    }

    public void discardExcessLeaderCards(String nickname, int cardIndex) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        serverController.discardExcessLeaderCards(nickname, cardIndex);
    }
    public void getInitialResources(String nickname, Resource resource, int position) throws WrongTurnException, WrongMoveException, DepositCellNotEmpty, IllegalDepositStateException {
        serverController.getInitialResources(nickname, resource, position);
    }

    public void playLeaderCard(String nickname, int cardToPlay) throws WrongTurnException, CardNotPlayableException {
        serverController.playLeaderCard(nickname, cardToPlay);
    }

    public void activateLeaderCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, WrongMoveException, PowerNotActivatableException {
        serverController.activateLeaderCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void activateDashboardProductionPower(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, WrongMoveException, PowerNotActivatableException {
        serverController.activateDashboardProductionPower(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) throws WrongTurnException, WrongMoveException, PowerNotActivatableException {
        serverController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) throws WrongMoveException, WrongTurnException {
        serverController.getFromMarket(nickname, move, wmrs);
    }

    public void storeFromSupply(String nickname, int from, int to) throws WrongMoveException, WrongTurnException, IllegalDepositStateException {
        serverController.storeFromSupply(nickname, from, to);
    }

    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) throws WrongMoveException, WrongTurnException, IllegalDepositStateException {
        serverController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
    }

    public void discardLeaderCard(String nickname, int cardToDiscard) throws CardNotPlayableException, WrongTurnException {
        serverController.discardLeaderCard(nickname, cardToDiscard);
    }

    public void endTurn(String nickname) throws WrongMoveException, WrongTurnException, LeaderCardInExcessException {
        serverController.endTurn(nickname);
    }

    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position)
            throws WrongTurnException, CardNotBuyableException, CardNotPlayableException, WrongMoveException {
        serverController.buyDevelopmentCard(nickname, row, column, resourcesToPayCost, position);
    }

    public String getId() {
        return id;
    }

    public Pair<Integer, Integer> getLobbyStatus() {
        return new Pair<>(connectedPlayers.size(), maxPlayers);
    }

    public LobbyType getType() {
        return LobbyType.GAME_LOBBY;
    }
}
