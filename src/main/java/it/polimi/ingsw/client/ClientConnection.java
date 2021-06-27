package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.controller.ReducedModel;
import it.polimi.ingsw.controller.ReducedDashboard;
import it.polimi.ingsw.controller.ReducedGame;
import it.polimi.ingsw.controller.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import it.polimi.ingsw.model.singlePlayer.tokens.Token;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ClientConnection implements Runnable {
    private String playerNickname;

    private String ip;
    private int port;
    public final UI ui;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean nameRegistered;

    private Socket socket;

    ReducedModel reducedModel;

    public ClientConnection(String ip, int port, UI ui) {
        this.ip = ip;
        this.port = port;
        this.ui = ui;
        this.nameRegistered = false;
        this.reducedModel = new ReducedModel();
        this.ui.startUI(this, reducedModel);
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
        this.reducedModel.setNickname(playerNickname);
    }

    public void connectToServer() throws IOException {
        ui.printColoredMessage("Connection at " + ip + " on port " + port, Constants.GOLD_COLOR);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            ServerMessage serverMessage = receiveServerMessage();
            // System.out.println("I have received serverMessage: " + serverMessage.toString());
            serverMessage.updateClient(this, null);
        } catch (Exception e) {
            // welcome message not received
            // connection failed
        }
    }

    public void registerName() throws IOException, ClassNotFoundException {
        String nickname = ui.getNickname();
        if (!nickname.equals("")) {
            send(new RegisterName(nickname));
            try {
                ServerMessage serverMessage = receiveServerMessage();
                serverMessage.updateClient(this, nickname);
            } catch (Exception e) {
                ui.printErrorMessage("Connection error while reading server response");
            }
        } else {
            ui.printErrorMessage("Insert a non-empty nickname");
        }
        if(isNameRegistered()) {
            if(ui.getType() == UIType.CLI) { ((CLI) ui).showMainLobbyMenu(); }
        }
    }

    public ServerMessage receiveServerMessage() throws IOException, ClassNotFoundException {
        return (ServerMessage) inputStream.readObject();
    }

    public boolean isNameRegistered() {
        return nameRegistered;
    }

    public void nameRegistered() {
        nameRegistered = true;
    }

    public synchronized void send(ClientMessage message){
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (Exception e) {
            close();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        startReceivingThread();
        if (ui.getType() == UIType.CLI) {
            ((CLI) ui).startReadingThread();
        }
    }

    private void startReceivingThread() {
        new Thread(() -> {
            while(true) {
                try {
                    ServerMessage serverMessage = receiveServerMessage();
                    serverMessage.updateClient(this, playerNickname);
                } catch (ClassNotFoundException | IOException e) {
                    ui.printErrorMessage("Connection with server lost");
                    System.exit(-1);
                    break;
                }
            }
        }).start();
    }

    public void updateReducedGame(String firstPlayer, String currentPlayer, List<String> playersNicknames, TurnPhase turnPhase, int firstTurns, boolean gameStarted, Stack<Token> tokens, Token token) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        reducedGame.setGameStarted(gameStarted);
        String oldPlayer = reducedGame.getCurrentPlayer();
        TurnPhase oldPhase = reducedGame.getTurnPhase();
        reducedGame.setGameStarted(gameStarted);
        reducedGame.setFirstPlayer(firstPlayer);
        reducedGame.setCurrentPlayer(currentPlayer);
        reducedGame.updatePlayers(playersNicknames);
        reducedGame.setTurnPhase(turnPhase);
        reducedGame.setFirstTurns(firstTurns);
        reducedGame.setTokens(tokens);
        reducedGame.setToken(token);
        handleMenus(reducedGame, oldPlayer, oldPhase);
    }



    public void handleMenus(ReducedGame reducedGame, String oldPlayer, TurnPhase oldPhase) {
        String currentPlayer = reducedGame.getCurrentPlayer();
        TurnPhase currentPhase = reducedGame.getTurnPhase();
    }



    public void updateReducedDashboard(String nickname, int playerPoints,
                                       List<LeaderCard> playedLeaderCards,
                                       List<Stack<DevelopmentCard>> playedDevelopmentCards,
                                       ArrayList<Resource> supply, ProductionPower productionPower) {

        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPlayerPoints(playerPoints);
        reducedDashboard.setPlayedLeaderCards(playedLeaderCards);
        reducedDashboard.setPlayedDevelopmentCards(playedDevelopmentCards);
        reducedDashboard.setSupply(supply);
        reducedDashboard.setProductionPower(productionPower);
    }

    public void updateReducedDVGrid(List<Stack<DevelopmentCard>> grid) {
        reducedModel.getReducedGameBoard().setGrid(grid);
    }

    public void updateReducedGameBoard(List<LeaderCard> leaderCardDeck, List<DevelopmentCard> developmentCardDeck, List<Card> discardDeck) {
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        reducedGameBoard.setLeaderCardDeck(leaderCardDeck);
        reducedGameBoard.setDevelopmentCardDeck(developmentCardDeck);
        reducedGameBoard.setDiscardDeck(discardDeck);
    }

    public void updateReducedMarket(Marble[] marblesGrid) {
        reducedModel.getReducedGameBoard().setMarblesGrid(marblesGrid);
    }

    public void updateReducedPlayer(String nickname, List<LeaderCard> hand, int handSize, Resource[] wmrs) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedPlayer reducedPlayer = reducedGame.getPlayers().get(nickname);
        reducedPlayer.setNickname(nickname);
        reducedPlayer.setHand(hand);
        reducedPlayer.setHandSize(handSize);
        reducedPlayer.setActivatedWMR(wmrs);
    }

    public void updateReducedWarehouse(String nickname, Resource[] deposit, Resource[][] extraDeposits, Map<Resource, Integer> locker) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setDeposit(deposit);
        reducedDashboard.setExtraDeposits(extraDeposits);
        reducedDashboard.setLocker(locker);
    }

    public void updateReducedFaithTrack(String nickname, int position, int LorenzoIlMagnificoPosition, Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports, int[] faithTrackVictoryPoints) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPosition(position);
        reducedDashboard.setLorenzoIlMagnificoPosition(LorenzoIlMagnificoPosition);
        reducedDashboard.setVaticanReports(vaticanReports);
        reducedDashboard.setFaithTrackVictoryPoints(faithTrackVictoryPoints);
    }

    public void updateGameInfo(String gameID, int numberOfPlayers) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        reducedGame.setGameId(gameID);
        reducedGame.setNumberOfPlayers(numberOfPlayers);
    }

    public ReducedModel getReducedModel() {
        return reducedModel;
    }

}
