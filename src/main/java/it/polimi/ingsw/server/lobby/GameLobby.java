package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messageHandlers.GameLobbyMessageHandler;
import it.polimi.ingsw.server.lobby.messageHandlers.GameMessages;
import it.polimi.ingsw.utils.Pair;

import javax.naming.InvalidNameException;
import java.util.HashMap;
import java.util.Map;

public class GameLobby implements Lobby {

    private final String id;

    private final int maxPlayers;
    private final Map<String, Connection> connectedPlayers;

    private GameLobbyMessageHandler gameLobbyMessageHandler;

    private final ServerController serverController;

    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.gameLobbyMessageHandler = new GameLobbyMessageHandler(this);
        this.serverController = new ServerController(id, maxPlayers);
    }

    public void handleMessage(String msg, Connection c) {
        // TODO
    }

    public void enterLobby(Connection c) throws InvalidNameException, IllegalStateException {
        try {
            serverController.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);
        c.changeLobby(this);
    }

    public void loadGameSettings(GameSettings gameSettings) {
        serverController.loadGameSettings(gameSettings);
    }

    public void discardExcessLeaderCards(String nickname, int cardIndex) throws WrongTurnException, WrongMoveException, CardNotPlayableException {
        try {
            serverController.discardExcessLeaderCards(nickname, cardIndex);
        } catch (WrongTurnException e) {
            throw new WrongTurnException("Not your turn");
        } catch (WrongMoveException e) {
            throw new WrongMoveException("Invalid move");
        } catch (CardNotPlayableException e) {
            throw new CardNotPlayableException("Card is not playable");
        }
    }
    public void getInitialResources(String nickname, Resource resource, int position) throws WrongTurnException, WrongMoveException, DepositCellNotEmpty, IllegalDepositStateException {
        serverController.getInitialResources(nickname, resource, position);
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

}
