package it.polimi.ingsw.server.lobby.messageHandlers;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.lobby.GameLobby;

import java.io.IOException;

public class GameLobbyMessageHandler {

    private GameLobby gameLobby;

    public GameLobbyMessageHandler(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    public synchronized void handleMessage(String msg, Connection c) {

        switch (GameMessages.valueOf(msg.toUpperCase())) {
            case LOAD_GAME_SETTINGS -> loadGameSettings(c);
            case GET_INITIAL_RESOURCES -> getInitialResources(c);
            case BUY_DEVELOPMENT_CARD -> buyDevelopmentCard(c);
            default -> c.asyncSend((ServerMessages.ERROR));
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

        String nickname = c.getNickname();

        try {
            Resource resource = (Resource) c.receive();
            int position = (int) c.receive();
            gameLobby.getInitialResources(nickname, resource, position);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongTurnException e) {
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (WrongMoveException e) {
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (DepositCellNotEmpty e) {
            c.asyncSend(GameErrorMessages.DEPOSIT_CELL_NOT_EMPTY);
        } catch (IllegalDepositStateException e) {
            c.asyncSend(GameErrorMessages.ILLEGAL_DEPOSIT_STATE);
        }

    }

    public void buyDevelopmentCard(Connection c) {

        String nickname = c.getNickname();

        try {
            int row = (int) c.receive();
            int col = (int) c.receive();
            ResourceContainer resourcesToPayCost = (ResourceContainer) c.receive();
            int position = (int) c.receive();
            gameLobby.buyDevelopmentCard(nickname,row,col,resourcesToPayCost, position);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            c.asyncSend(ServerMessages.ERROR);
        } catch (CardNotPlayableException e) {
            c.asyncSend(GameErrorMessages.CARD_NOT_PLAYABLE);
        } catch (WrongMoveException e) {
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (CardNotBuyableException e) {
            c.asyncSend(GameErrorMessages.CARD_NOT_BUYABLE_EXCEPTION);
        } catch (WrongTurnException e) {
            c.asyncSend(GameErrorMessages.WRONG_TURN);
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



}
