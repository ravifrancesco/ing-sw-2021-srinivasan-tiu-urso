package it.polimi.ingsw.server.lobby.messageHandlers;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;

import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.lobby.GameLobby;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class GameLobbyMessageHandler {

    private GameLobby gameLobby;

    public GameLobbyMessageHandler(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    // TODO reorder
    public synchronized void handleMessage(String msg, Connection c) {

        switch (GameMessages.valueOf(msg.toUpperCase())) {
            case PLAY_LEADER_CARD -> playLeaderCard(c);
            case ACTIVATE_LEADER_CARD_PRODUCTION_POWER -> activateLeaderCardProductionPower(c);
            case ACTIVATE_DASHBOARD_PRODUCTION_POWER -> activateDashboardProductionPower(c);
            case ACTIVATE_DEVELOPMENT_CARD_PRODUCTION_POWER -> activateDevelopmentCardProductionPower(c);
            case GET_INITIAL_RESOURCES -> getInitialResources(c);
            case GET_FROM_MARKET -> getFromMarket(c);
            case DISCARD_LEADER_CARD -> discardLeaderCard(c);
            case STORE_FROM_SUPPLY_TO_EXTRA_DEPOSIT -> storeFromSupplyInExtraDeposit(c);
            case STORE_FROM_SUPPLY -> storeFromSupply(c);
            case DISCARD_EXCESS_LEADER_CARD -> discardExcessLeaderCards(c);
            case LOAD_GAME_SETTINGS -> loadGameSettings(c);
            case END_TURN -> endTurn(c);
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
  
    // TODO adjust after professor meeting
    public void getFromMarket(Connection c) {
        try {
            int moveIndex = (Integer) c.receive();
            ArrayList<WhiteMarbleResource> wmrs = (ArrayList<WhiteMarbleResource>) c.receive();
            gameLobby.getFromMarket(c.getNickname(), moveIndex, wmrs);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error whilst receiving move parameters");
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongMoveException e) {
            System.err.println("Wrong move index");
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (WrongTurnException e) {
            System.err.println("Wrong turn");
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        }
    }

    // TODO adjust after professor meeting
    public void storeFromSupply(Connection c) {
        try {
            int from = (Integer) c.receive();
            int to = (Integer) c.receive();
            gameLobby.storeFromSupply(c.getNickname(), from, to);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error whilst receiving move parameters.");
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongMoveException e) {
            System.err.println("Wrong move indexes");
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (WrongTurnException e) {
            System.err.println("Wrong turn");
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (IllegalDepositStateException e) {
            System.err.println("Move creates an illegal deposit");
            c.asyncSend(GameErrorMessages.ILLEGAL_DEPOSIT_STATE);
        }
    }

    // TODO adjust after professor meeting
    public void storeFromSupplyInExtraDeposit(Connection c) {
        try {
            int leaderCardPos = (Integer) c.receive();
            int from = (Integer) c.receive();
            int to = (Integer) c.receive();
            gameLobby.storeFromSupplyInExtraDeposit(c.getNickname(), leaderCardPos, from, to);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error whilst receiving move parameters.");
            c.asyncSend(ServerMessages.ERROR);
        } catch (WrongMoveException e) {
            System.err.println("Wrong move indexes");
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (WrongTurnException e) {
            System.err.println("Wrong turn");
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (IllegalDepositStateException e) {
            System.err.println("Move creates an illegal deposit");
            c.asyncSend(GameErrorMessages.ILLEGAL_DEPOSIT_STATE);
        }
    }

    // TODO adjust after professor meeting
    public void discardLeaderCard(Connection c) {
        try {
            int cardToDiscard = (Integer) c.receive();
            gameLobby.discardLeaderCard(c.getNickname(), cardToDiscard);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error whilst receiving move parameters.");
            c.asyncSend(ServerMessages.ERROR);
        } catch (CardNotPlayableException e) {
            System.err.println("Card is not playable");
            c.asyncSend(GameErrorMessages.CARD_NOT_PLAYABLE);
        } catch (WrongTurnException e) {
            System.err.println("Wrong turn");
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        }
    }

    // TODO adjust after professor meeting
    public void endTurn(Connection c) {
        try {
            gameLobby.endTurn(c.getNickname());
        } catch (WrongMoveException e) {
            System.err.println("Wrong move indexes");
            c.asyncSend(GameErrorMessages.WRONG_MOVE);
        } catch (WrongTurnException e) {
            System.err.println("Wrong turn");
            c.asyncSend(GameErrorMessages.WRONG_TURN);
        } catch (LeaderCardInExcessException e) {
            System.err.println("Too many leader cards in hand");
            c.asyncSend(GameErrorMessages.EXCESS_LEADER_CARD);
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
