package it.polimi.ingsw.view.virtualView.client;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.UIType;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.reduced.*;
import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.UI;

import javax.naming.InvalidNameException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO doc
 * TODO check synchronized methods (send/close)
 */
public class OfflineClientVirtualView implements ClientVirtualViewIF, Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver, GameBoardObserver,
        DevelopmentCardGridObserver, MarketObserver, GameErrorObserver {

    private String nickname;

    private final UI ui;

    private Controller controller;

    private ReducedModel reducedModel;

    public OfflineClientVirtualView(UI ui) {
        this.ui = ui;
        GameSettings gameSettings = GameSettings.loadDefaultGameSettings();
        this.controller = new Controller("local_single_player", 1);
        controller.loadGameSettings(gameSettings);
        this.reducedModel = new ReducedModel();
        this.ui.startUI(this, reducedModel);
        askNickname();
        System.out.println("Isnert STARTGAME to start playing");
    }

    public OfflineClientVirtualView(UI ui, String nickname) {
        this.ui = ui;
        GameSettings gameSettings = GameSettings.loadDefaultGameSettings();
        this.controller = new Controller("local_single_player", 1);
        controller.loadGameSettings(gameSettings);
        this.reducedModel = new ReducedModel();
        ui.startUI(this, reducedModel);
        setNickName(nickname);
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
                setNickName(choice);
                return;
            }
        }
    }

    private void setNickName(String choice) {
        this.nickname = choice;
        this.reducedModel.setNickname(nickname);
        try {
            controller.joinGame(nickname);
        } catch (GameFullException | InvalidNameException e) {
            e.printStackTrace();
        }
        controller.addObserversLocal(this);
    }

    public String getNickname() {
        return nickname;
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
        if(message.getGameEnded()) {
            ui.handleMenuCode("game_has_ended_single");
        }
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
        ((ClientGameMessage) message).handleLocally(this, controller);
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


