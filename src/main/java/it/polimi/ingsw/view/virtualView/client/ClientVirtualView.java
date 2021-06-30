package it.polimi.ingsw.view.virtualView.client;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.tokens.Token;
import it.polimi.ingsw.model.reduced.ReducedDashboard;
import it.polimi.ingsw.model.reduced.ReducedGame;
import it.polimi.ingsw.model.reduced.ReducedModel;
import it.polimi.ingsw.model.reduced.ReducedPlayer;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.RegisterName;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.UI;
import it.polimi.ingsw.view.UI.UIType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ClientVirtualView implements Runnable, ClientVirtualViewIF {
    public final UI ui;
    private final String ip;
    private final int port;
    ReducedModel reducedModel;
    private String playerNickname;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean nameRegistered;
    private Socket socket;

    /**
     * Virtual view for the multi player game
     *
     * @param ip   the ip string
     * @param port the port string
     * @param ui   the ui
     */
    public ClientVirtualView(String ip, int port, UI ui) {
        this.ip = ip;
        this.port = port;
        this.ui = ui;
        this.nameRegistered = false;
        this.reducedModel = new ReducedModel();
        this.ui.startUI(this, reducedModel);
    }

    /**
     * Sets the player nickname
     *
     * @param playerNickname the player nickname
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
        this.reducedModel.setNickname(playerNickname);
    }

    /**
     * Handles server connection
     *
     * @throws IOException if the connection fails
     */
    public void connectToServer() throws IOException {
        ui.printColoredMessage("Connecting to " + ip + " on port " + port, Constants.GOLD_COLOR);
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
            System.out.println("Connection failed");
        }
    }

    /**
     * Handles the registration of a nickname to the server
     *
     * @throws IOException            if the registration fails
     * @throws ClassNotFoundException if the wrong message is received
     */
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
        if (isNameRegistered()) {
            if (ui.getType() == UIType.CLI) {
                ((CLI) ui).showMainLobbyMenu();
            }
        }
    }

    /**
     * Receives a server message and reads it
     *
     * @return the read server message
     * @throws IOException            if it is not received correctly
     * @throws ClassNotFoundException if the wrong class is parsed
     */
    public ServerMessage receiveServerMessage() throws IOException, ClassNotFoundException {
        return (ServerMessage) inputStream.readObject();
    }

    /**
     * Checks to see if the user registered
     *
     * @return true if correctly registered
     */
    public boolean isNameRegistered() {
        return nameRegistered;
    }

    /**
     * Sets the name registered boolean
     */
    public void nameRegistered() {
        nameRegistered = true;
    }

    /**
     * Sends a client message
     *
     * @param message the client message
     */
    public synchronized void send(ClientMessage message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (Exception e) {
            close();
        }
    }

    /**
     * Closes the socket connection
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * See UI
     */
    @Override
    public void run() {
        startReceivingThread();
        if (ui.getType() == UIType.CLI) {
            ((CLI) ui).startReadingThread();
        }
    }

    /**
     * Starts the thread that receives messages from the server
     */
    private void startReceivingThread() {
        new Thread(() -> {
            while (true) {
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

    /**
     * Methods for updates on information on the reduced model
     */
    public void updateReducedGame(String currentPlayer, List<String> playersNicknames, TurnPhase turnPhase, int firstTurns, boolean gameStarted, Token token) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        reducedGame.setGameStarted(gameStarted);
        reducedGame.setCurrentPlayer(currentPlayer);
        reducedGame.updatePlayers(playersNicknames);
        reducedGame.setTurnPhase(turnPhase);
        reducedGame.setFirstTurns(firstTurns);
        reducedGame.setToken(token);
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

    public void updateReducedMarket(Marble[] marblesGrid) {
        reducedModel.getReducedGameBoard().setMarblesGrid(marblesGrid);
    }

    public void updateReducedPlayer(String nickname, List<LeaderCard> hand, int handSize, Resource[] wmrs, ArrayList<DevelopmentCardDiscount> discounts) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedPlayer reducedPlayer = reducedGame.getPlayers().get(nickname);
        reducedPlayer.setNickname(nickname);
        reducedPlayer.setHand(hand);
        reducedPlayer.setHandSize(handSize);
        reducedPlayer.setActivatedWMR(wmrs);
        reducedPlayer.setDiscounts(discounts);
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

    public void updateReducedFaithTrack(String nickname, int position, int LorenzoIlMagnificoPosition, Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        if (!reducedGame.getPlayers().containsKey(nickname)) {
            reducedGame.createPlayer(nickname);
        }
        ReducedDashboard reducedDashboard = reducedGame.getPlayers().get(nickname).getDashboard();
        reducedDashboard.setPosition(position);
        reducedDashboard.setLorenzoIlMagnificoPosition(LorenzoIlMagnificoPosition);
        reducedDashboard.setVaticanReports(vaticanReports);
        reducedDashboard.setFaithTrackVictoryPoints();
    }

    public void updateGameInfo(int numberOfPlayers) {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        reducedGame.setNumberOfPlayers(numberOfPlayers);
    }

}
