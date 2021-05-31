package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.ClientMessageInputParser;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
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
    public final CLI cli;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean nameRegistered;

    private Socket socket;

    public ClientConnection(String ip, int port, CLI cli) {
        this.ip = ip;
        this.port = port;
        this.cli = cli;
        this.nameRegistered = false;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
        this.cli.getReducedModel().setNickname(playerNickname);
    }

    public void connectToServer() throws IOException {
        cli.printMessage("Connection at " + ip + " on port " + port);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            ServerMessage serverMessage = receiveServerMessage();
            serverMessage.updateClient(this, null);
        } catch (Exception e) {
            // welcome message not received
            // connection failed
        }
    }

    public void registerName() throws IOException, ClassNotFoundException {
        while(!nameRegistered) {
            String nickname = cli.getNickname();
            send(new RegisterName(nickname));
            try {
                ServerMessage serverMessage = receiveServerMessage();
                serverMessage.updateClient(this, nickname);

            } catch (Exception e) {
                cli.printErrorMessage("Connection error while reading server response");
            }
        }
    }

    public ServerMessage receiveServerMessage() throws IOException, ClassNotFoundException {
        return (ServerMessage) inputStream.readObject();
    }

    public void nameRegistered() {
        nameRegistered = true;
    }

    private synchronized void send(ClientMessage message){
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (Exception e) {
            close();
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        startReceivingThread();
        startReadingThread();
    }

    private void startReceivingThread() {
        new Thread(() -> {
            while(true) {
                try {
                    ServerMessage serverMessage = receiveServerMessage();
                    cli.printMessage("Received server message: " + serverMessage.toString());
                    serverMessage.updateClient(this, playerNickname);

                } catch (ClassNotFoundException | IOException e) {
                    cli.printErrorMessage("Connection with server lost");
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }



    private void startReadingThread() {
        // TODO how to kill this thread if the receivingThread dies?
        new Thread(() -> {
            while(true) {
                String command = cli.readCommand();
                if(cli.getReducedModel().getReducedGame().getCurrentPlayer() != null) {
                    System.out.println("# It's " + cli.getReducedModel().getReducedGame().getCurrentPlayer() + "'s turn");
                }
                ClientMessage clientMessage = ClientMessageInputParser.parseInput(command, cli);
                if (clientMessage != null) {
                    try {
                        send(clientMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // TODO ugly asf, need to find a way to show the "enter command" after server answers
                // and client is shown the response to its command
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void updateReducedGame(String firstPlayer, String currentPlayer, List<String> playersNicknames) {
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
        reducedGame.setFirstPlayer(firstPlayer);
        reducedGame.setCurrentPlayer(currentPlayer);
        reducedGame.updatePlayers(playersNicknames);
    }

    public void updateReducedDashboard(String nickname, int playerPoints, List<LeaderCard> playedLeaderCards, List<Stack<DevelopmentCard>> playedDevelopmentCards, ArrayList<Resource> supply) {
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPlayerPoints(playerPoints);
        reducedDashboard.setPlayedLeaderCards(playedLeaderCards);
        reducedDashboard.setPlayedDevelopmentCards(playedDevelopmentCards);
        reducedDashboard.setSupply(supply);
    }

    public void updateReducedDVGrid(List<Stack<DevelopmentCard>> grid) {
        cli.getReducedModel().getReducedGameBoard().setGrid(grid);
    }

    public void updateReducedGameBoard(List<LeaderCard> leaderCardDeck, List<DevelopmentCard> developmentCardDeck, List<Card> discardDeck) {
        ReducedGameBoard reducedGameBoard = cli.getReducedModel().getReducedGameBoard();
        reducedGameBoard.setLeaderCardDeck(leaderCardDeck);
        reducedGameBoard.setDevelopmentCardDeck(developmentCardDeck);
        reducedGameBoard.setDiscardDeck(discardDeck);
    }

    public void updateReducedMarket(Marble[] marblesGrid) {
        cli.getReducedModel().getReducedGameBoard().setMarblesGrid(marblesGrid);
    }

    public void updateReducedPlayer(String nickname, List<LeaderCard> hand, int handSize, Resource[] wmrs) {
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
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
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setDeposit(deposit);
        reducedDashboard.setExtraDeposits(extraDeposits);
        reducedDashboard.setLocker(locker);
    }

    public void updateReducedFaithTrack(String nickname, int position, Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports, int[] faithTrackVictoryPoints) {
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPosition(position);
        reducedDashboard.setVaticanReports(vaticanReports);
        reducedDashboard.setFaithTrackVictoryPoints(faithTrackVictoryPoints);
    }

    public void updateGameInfo(String gameID, int numberOfPlayers) {
        ReducedGame reducedGame = cli.getReducedModel().getReducedGame();
        reducedGame.setGameId(gameID);
        reducedGame.setNumberOfPlayers(numberOfPlayers);
    }
}
