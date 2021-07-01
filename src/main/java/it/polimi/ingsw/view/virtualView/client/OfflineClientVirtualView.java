package it.polimi.ingsw.view.virtualView.client;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.model.reduced.ReducedDashboard;
import it.polimi.ingsw.model.reduced.ReducedGame;
import it.polimi.ingsw.model.reduced.ReducedModel;
import it.polimi.ingsw.model.reduced.ReducedPlayer;
import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.UI;
import it.polimi.ingsw.view.UI.UIType;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OfflineClientVirtualView implements ClientVirtualViewIF, Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver,
        DevelopmentCardGridObserver, MarketObserver, GameErrorObserver {

    private final UI ui;
    private final Controller controller;
    private final ReducedModel reducedModel;
    private String nickname;

    /**
     * The virtual view for the offline local game
     *
     * @param ui
     */
    public OfflineClientVirtualView(UI ui) {
        this.ui = ui;
        GameSettings gameSettings = GameSettings.loadDefaultGameSettings();
        this.controller = new Controller("local_single_player", 1);
        controller.loadGameSettings(gameSettings);
        this.reducedModel = new ReducedModel();
        reducedModel.getReducedGame().setNumberOfPlayers(1);
        this.ui.startUI(this, reducedModel);
        askNickname();
        System.out.println("Insert STARTGAME to start playing");
    }

    /**
     * The virtual view for the offline local game
     *
     * @param ui
     */
    public OfflineClientVirtualView(UI ui, String nickname) {
        this.ui = ui;
        GameSettings gameSettings = GameSettings.loadDefaultGameSettings();
        this.controller = new Controller("local_single_player", 1);
        controller.loadGameSettings(gameSettings);
        this.reducedModel = new ReducedModel();
        this.reducedModel.getReducedGame().setNumberOfPlayers(1);
        ui.startUI(this, reducedModel);
        setNickName(nickname);
    }

    /**
     * Handles the insertion of the nickname
     */
    public void askNickname() {
        Scanner input = new Scanner(System.in);
        String choice;
        System.out.println("\nPlease insert a nickname: ");
        System.out.print("> ");
        while (true) {
            choice = input.nextLine();
            if (choice.isEmpty()) {
                System.out.println("Please insert a non-empty nickname");
            } else {
                setNickName(choice);
                System.out.println();
                return;
            }
        }
    }

    /**
     * Sets the nickname
     *
     * @param choice the nickname
     */
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

    /**
     * Update methods for the observers
     *
     * @param message the updated class
     */
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
        reducedDashboard.setFaithTrackVictoryPoints();
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
        reducedPlayer.setActivatedWMR(message.getActivatedWMR());
    }

    @Override
    public void update(Game message) {
        ReducedGame reducedGame = ui.getReducedModel().getReducedGame();
        reducedGame.setGameStarted(message.isGameStarted());
        reducedGame.setGameStarted(message.isGameStarted());
        reducedGame.setCurrentPlayer(message.getCurrentPlayer());
        reducedGame.updatePlayers(new ArrayList<>(message.getPlayers().keySet()));
        reducedGame.setTurnPhase(message.getTurnPhase());
        reducedGame.setFirstTurns(message.getFirstTurns());
        reducedGame.setToken(message.getLastToken());
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

    /**
     * Returns a vatican report state
     *
     * @param v the vatican report
     * @return
     */
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

    /**
     * Prints a successful move message
     *
     * @param message the message
     */
    public void printSuccessfulMove(String message) {
        ui.printColoredMessage(message, Constants.ANSI_GREEN);
    }

    public UI getUi() {
        return ui;
    }

}


