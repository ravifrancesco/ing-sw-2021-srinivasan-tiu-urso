package it.polimi.ingsw.server.lobby.messageHandlers;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.lobby.GameLobby;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameLobbyMessageHandler {

    private GameLobby gameLobby;

    public GameLobbyMessageHandler(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public synchronized void handleMessage(String msg, Connection c) {

        switch (GameMessages.valueOf(msg.toUpperCase())) {
            case LOAD_GAME_SETTINGS ->
                loadGameSettings(c);
            case PLAY_LEADER_CARD ->
                playLeaderCard(c);
            case ACTIVATE_LEADER_CARD_PRODUCTION_POWER ->
                activateLeaderCardProductionPower(c);
            case ACTIVATE_DASHBOARD_PRODUCTION_POWER ->
                activateDashboardProductionPower(c);
            case ACTIVATE_DEVELOPMENT_CARD_PRODUCTION_POWER ->
                activateDevelopmentCardProductionPower(c);
            default ->
                c.asyncSend((ServerMessages.ERROR));
        }

    }

    public void loadGameSettings(Connection c) {
        try {
            GameSettings gameSettings = (GameSettings) c.receive();
            gameLobby.loadGameSettings(gameSettings);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        }
    }

    public void getInitialResources(Connection c) {

        String nickName = c.getNickname();

        try {
            Resource resource = (Resource) c.receive();
            int position = (int) c.receive();
            gameLobby.getInitialResources(nickName, resource, position);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            e.printStackTrace();
        } catch (DepositCellNotEmpty depositCellNotEmpty) {
            depositCellNotEmpty.printStackTrace();
        } catch (IllegalDepositStateException e) {
            e.printStackTrace();
        }

    }


    public void discardExcessLeaderCards(Connection c) {
        try {
            int cardIndex = (Integer) c.receive();
            gameLobby.discardExcessLeaderCards(c.getNickname(), cardIndex);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (CardNotPlayableException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.CARD_NOT_PLAYABLE);
        }
    }

    // TODO
    public void playLeaderCard(Connection c) {
        try {
            int cardToPlay = (Integer) c.receive();
            gameLobby.playLeaderCard(c.getNickname(), cardToPlay);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (CardNotPlayableException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.CARD_NOT_PLAYABLE);
        }
    }

    // TODO
    public void activateLeaderCardProductionPower(Connection c) {
        try {
            int cardToActivate = (Integer) c.receive();
            ResourceContainer resourceContainer = (ResourceContainer) c.receive();
            HashMap<Resource, Integer> resourceRequiredOptional = (HashMap<Resource, Integer>) c.receive();
            HashMap<Resource, Integer> resourceProducedOptional = (HashMap<Resource, Integer>) c.receive();
            gameLobby.activateLeaderCardProductionPower(c.getNickname(), cardToActivate, resourceContainer, resourceRequiredOptional, resourceProducedOptional);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (PowerNotActivatableException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.POWER_NOT_ACTIVATABLE);
        }
    }

    // TODO
    public void activateDashboardProductionPower(Connection c) {
        try {
            ResourceContainer resourceContainer = (ResourceContainer) c.receive();
            HashMap<Resource, Integer> resourceRequiredOptional = (HashMap<Resource, Integer>) c.receive();
            HashMap<Resource, Integer> resourceProducedOptional = (HashMap<Resource, Integer>) c.receive();
            gameLobby.activateDashboardProductionPower(c.getNickname(), resourceContainer, resourceRequiredOptional, resourceProducedOptional);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (PowerNotActivatableException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.POWER_NOT_ACTIVATABLE);
        }
    }

    // TODO
    public void activateDevelopmentCardProductionPower(Connection c) {
        try {
            int cardToActivate = (Integer) c.receive();
            ResourceContainer resourceContainer = (ResourceContainer) c.receive();
            HashMap<Resource, Integer> resourceRequiredOptional = (HashMap<Resource, Integer>) c.receive();
            HashMap<Resource, Integer> resourceProducedOptional = (HashMap<Resource, Integer>) c.receive();
            gameLobby.activateDevelopmentCardProductionPower(c.getNickname(), cardToActivate, resourceContainer, resourceRequiredOptional, resourceProducedOptional);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (PowerNotActivatableException e) {
            System.err.println(e.getMessage());
            c.asyncSend(GameErrorMessages.POWER_NOT_ACTIVATABLE);
        }
    }
}
