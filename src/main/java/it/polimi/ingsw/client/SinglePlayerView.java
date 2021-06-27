package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.lobby.*;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.FailedMoveMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.SuccessfulMoveMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.*;
import it.polimi.ingsw.server.lobby.messages.serverMessages.updates.*;
import it.polimi.ingsw.utils.Pair;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO doc
 * TODO check synchronized methods (send/close)
 */
public class SinglePlayerView implements Intermediary, Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver, GameBoardObserver,
        DevelopmentCardGridObserver, MarketObserver, GameErrorObserver {

    private String nickname;

    private final UI ui;

    private ServerController serverController;

    private ReducedModel reducedModel;

    public SinglePlayerView(UI ui) {
        this.ui = ui;
        GameSettings gameSettings = GameSettings.loadDefaultGameSettings();
        this.serverController = new ServerController("local_single_player", 1);
        serverController.loadGameSettings(gameSettings);
        this.reducedModel = new ReducedModel();
        ui.startUI(this, reducedModel);
        askNickname();
        System.out.println("Isnert STARTGAME to start playing");
    }

    public void askNickname() {
        Scanner input = new Scanner(System.in);
        String choice;
        System.out.println("Please insert a nickname: ");
        while(true) {
            choice = input.nextLine();
            if(choice.isEmpty()) {
                System.out.println("Please insert a non-empty nickname");
            } else {
                nickname = choice;
                this.reducedModel.setNickname(nickname);
                try {
                    serverController.joinGame(nickname);
                } catch (GameFullException | InvalidNameException e) {
                    e.printStackTrace();
                }
                serverController.addObserversLocal(this);
                return;
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {
        if (ui.getType() == UIType.CLI) {
            ((CLI) ui).startReadingThread();
        }
    }

    @Override
    public void update(FaithTrack message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPosition(message.getPosition());
        reducedDashboard.setLorenzoIlMagnificoPosition(message.getLorenzoIlMagnificoPosition());
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports = new HashMap<>();
        message.getVaticanReports().forEach((key, value) -> vaticanReports.put(new Pair<>(value.getStart(), value.getEnd()), new Pair<>(value.getVictoryPoints(), vaticanReportState(value))));
        reducedDashboard.setVaticanReports(vaticanReports);
        reducedDashboard.setFaithTrackVictoryPoints(message.getFaithTrackVictoryPoints());
    }

    @Override
    public void update(Warehouse message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setDeposit(message.getDeposit());
        reducedDashboard.setExtraDeposits(message.getExtraDeposits());
        reducedDashboard.setLocker(message.getLocker());
    }

    @Override
    public void update(Dashboard message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPlayerPoints(message.getPlayerPoints());
        reducedDashboard.setPlayedLeaderCards(message.getPlayedLeaderCards());
        reducedDashboard.setPlayedDevelopmentCards(message.getPlayedDevelopmentCards());
        reducedDashboard.setSupply(message.getSupply());
        reducedDashboard.setProductionPower(message.getDashBoardProductionPower());
    }

    @Override
    public void update(Player message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedPlayer reducedPlayer = reducedGame.getPlayers().get(nickname);
        reducedPlayer.setNickname(nickname);
        reducedPlayer.setHand(message.getHand());
        reducedPlayer.setHandSize(message.getHandSize());
        System.out.println(Arrays.toString(message.getActivatedWMR()));
        reducedPlayer.setActivatedWMR(message.getActivatedWMR());
    }

    @Override
    public void update(Game message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        reducedGame.setGameStarted(message.isGameStarted());
        reducedGame.setGameStarted(message.isGameStarted());
        reducedGame.setFirstPlayer(message.getFirstPlayer());
        reducedGame.setCurrentPlayer(message.getCurrentPlayer());
        reducedGame.updatePlayers(new ArrayList<>(message.getPlayers().keySet()));
        reducedGame.setTurnPhase(message.getTurnPhase());
        reducedGame.setFirstTurns(message.getFirstTurns());
        reducedGame.setTokens(message.getTokens());
        reducedGame.setToken(message.getLastToken());
    }

    @Override
    public void update(GameBoard message) {
        ReducedGameBoard reducedGameBoard = ui.getReducedModel().getReducedGameBoard();
        reducedGameBoard.setLeaderCardDeck(message.getLeaderDeck().toList().stream().map(card -> (LeaderCard) card).collect(Collectors.toList()));
        reducedGameBoard.setDevelopmentCardDeck(message.getDevelopmentDeck().toList().stream().map(card -> (DevelopmentCard) card).collect(Collectors.toList()));
        reducedGameBoard.setDiscardDeck(message.getDiscardDeck().toList());
    }

    @Override
    public void update(DevelopmentCardGrid message) {
        ui.getReducedModel().getReducedGameBoard().setGrid(message.getGrid());
    }

    @Override
    public void update(Market message) {
        ui.getReducedModel().getReducedGameBoard().setMarblesGrid(message.getMarblesGrid());
    }

    @Override
    public void update(GameError message) {
        if (message.getError().first.equals(nickname)) {
            ui.printErrorMessage("Move failed: " + message.getError().second.getMessage());
        }
    }

    private int vaticanReportState(VaticanReport v) {
        if (!v.isMissed() && !v.isAchieved()) {
            return 0;
        } else if (v.isMissed()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void send(ClientMessage message) {
        ((ClientGameMessage) message).handleLocally(this, serverController);
    }

    public void printSuccessfulMove(String message) {
        ui.printColoredMessage(message, Constants.ANSI_GREEN);
    }

    public UI getUi() {
        return ui;
    }

    public void handleGameEnded() {
        // TODO
    }
}


