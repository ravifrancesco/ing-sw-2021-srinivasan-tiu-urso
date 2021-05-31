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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLobby implements Lobby, Serializable {

    private final String id;

    private final int maxPlayers;

    private String creator;

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Map<String, Connection> getConnectedPlayers() {
        return connectedPlayers;
    }

    private final Map<String, Connection> connectedPlayers;


    private final ServerController serverController;
    private GameSettings gameSettings;

    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;

        this.gameSettings = GameSettings.loadDefaultGameSettings();

        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.serverController = new ServerController(id, maxPlayers);
    }

    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, Connection c) {
        ((ClientGameMessage) clientMessage).handle(c, serverController);
    }

    public String getCreator() {
        return creator;
    }

    public synchronized void enterLobby(Connection c) throws InvalidNameException, IllegalStateException {
        if(connectedPlayers.size() == 0) { // if it was the first player
            serverController.loadGameSettings(gameSettings);
            creator = c.getNickname();
        }
        try {
            serverController.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);

        c.enterLobby(this);
        serverController.addObservers(c, connectedPlayers);
        /*
        if(connectedPlayers.size() == maxPlayers) {
            System.out.println("Starting game..");
            serverController.startGame();
        }
         */
    }

    public synchronized void leaveLobby(Connection c) {
        if (connectedPlayers.containsValue(c)) {
            try {
                serverController.removeObservers(c, connectedPlayers);
                connectedPlayers.remove(c.getNickname());
                serverController.leaveGame(c.getNickname());
            } catch (InvalidNameException e) {
                throw new IllegalStateException();
            }

        }
    }

    public void loadGameSettings(GameSettings gameSettings) {
        serverController.loadGameSettings(gameSettings);
    }

    public void discardExcessLeaderCards(String nickname, int cardIndex) {
        serverController.discardExcessLeaderCards(nickname, cardIndex);
    }
    public void getInitialResources(String nickname, Resource resource, int position) {
        serverController.getInitialResources(nickname, resource, position);
    }

    public void playLeaderCard(String nickname, int cardToPlay) {
        serverController.playLeaderCard(nickname, cardToPlay);
    }

    public void activateLeaderCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                             Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        serverController.activateLeaderCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void activateDashboardProductionPower(String nickname, ResourceContainer resourcesToPayCost,
                                            Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        serverController.activateDashboardProductionPower(nickname, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void activateDevelopmentCardProductionPower(String nickname, int cardToActivate, ResourceContainer resourcesToPayCost,
                                                       Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        serverController.activateDevelopmentCardProductionPower(nickname, cardToActivate, resourcesToPayCost, resourceRequiredOptional, resourceProducedOptional);
    }

    public void getFromMarket(String nickname, int move, ArrayList<WhiteMarbleResource> wmrs) {
        serverController.getFromMarket(nickname, move, wmrs);
    }

    public void storeFromSupply(String nickname, int from, int to) {
        serverController.storeFromSupply(nickname, from, to);
    }

    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) {
        serverController.storeFromSupplyInExtraDeposit(nickname, leaderCardPos, from, to);
    }

    public void discardLeaderCard(String nickname, int cardToDiscard) {
        serverController.discardLeaderCard(nickname, cardToDiscard);
    }

    public void endTurn(String nickname) {
        serverController.endTurn(nickname);
    }

    public void buyDevelopmentCard(String nickname, int row, int column, ResourceContainer resourcesToPayCost, int position) {
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
