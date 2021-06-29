package it.polimi.ingsw.view.virtualView.server;

import it.polimi.ingsw.model.full.table.*;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.model.utils.GameError;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.lobby.*;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.RegisterName;
import it.polimi.ingsw.network.messages.serverMessages.updates.FailedMoveMessage;
import it.polimi.ingsw.network.messages.serverMessages.updates.SuccessfulMoveMessage;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.network.messages.serverMessages.commons.*;
import it.polimi.ingsw.network.messages.serverMessages.updates.*;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * TODO doc
 * TODO check synchronized methods (send/close)
 */
public class ServerVirtualView implements Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver, GameBoardObserver,
        DevelopmentCardGridObserver, MarketObserver, GameErrorObserver {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;

    private final MainLobby mainLobby;

    public MainLobby getMainLobby() {
        return mainLobby;
    }

    private Lobby currentLobby;

    private boolean active = true;

    /**
     * The server virtual view, handling the network on the server side
     * @param socket the socket
     * @param mainLobby the main lobby with all the lobbies
     */
    public ServerVirtualView(Socket socket, MainLobby mainLobby) {
        this.socket = socket;
        this.mainLobby = mainLobby;
        this.currentLobby = mainLobby;
    }

    /**
     * Gets the current player lobby
     * @return the current lobby
     */
    public Lobby getCurrentLobby() {
        return currentLobby;
    }

    /**
     * Enter a lobby
     * @param lobby the lobby
     */
    public synchronized void enterLobby(Lobby lobby) {
        this.currentLobby = lobby;
    }

    /**
     * Sees if the connection is still active
     * @return
     */
    private synchronized boolean isActive() {
        return active;
    }

    /**
     * Sends a message to the client
     * @param message the message
     */
    private synchronized void send(ServerMessage message) {
        System.out.println(message);
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            close();
        }

    }

    /**
     * Receives and casts a game message (whilst inside a game lobby)
     * @return the client message to be cast
     * @throws IOException if not received correctly
     * @throws ClassNotFoundException if wrong type of cast
     */
    public ClientGameMessage receiveGameMessage() throws IOException, ClassNotFoundException {
        return (ClientGameMessage) in.readObject();
    }

    /**
     * Receives and casts a lobby message (whilst inside a main lobby)
     * @return the client message to be cast
     * @throws IOException if not received correctly
     * @throws ClassNotFoundException if wrong type of cast
     */
    public ClientLobbyMessage receiveLobbyMessage() throws IOException, ClassNotFoundException {
        return (ClientLobbyMessage) in.readObject();
    }

    /**
     * Notifies the client with a message that a move was successful
     * @param message string with the details about the move
     */
    public void sendSuccessfulMoveMessage(String message) {
        send(new SuccessfulMoveMessage(message));
    }

    /**
     * Notifies the client with a message that a move failed
     * @param message string with the details about the move
     */
    public void sendFailedMoveMessage(String message) {
        send(new FailedMoveMessage(message));
    }

    /**
     * Closes the socket
     */
    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        active = false;
    }

    /**
     * Deregisters the connection
     */
    private void deregisterConnection() {
        if (currentLobby.getType() == LobbyType.GAME_LOBBY) {
            GameLobby gameLobby = (GameLobby) currentLobby;
            gameLobby.leaveLobby(this);
            if (gameLobby.getConnectedPlayers().size() == 0) {
                mainLobby.removeGameLobby(gameLobby);
            }
        }
        mainLobby.deregisterConnection(this);
    }

    /**
     * Closes the connection and deregisters it from the lobby
     */
    public synchronized void close() {
        if (active) {
            closeConnection();
            System.out.println("Deregistering client " + nickname + "..");
            deregisterConnection();
            System.out.println("Deregistration complete");
            if (currentLobby.getType() == LobbyType.GAME_LOBBY) {
                ((GameLobby) currentLobby).getConnectedPlayers().values()
                        .forEach(value -> value.sendCLIupdateMessage("force_disconnection"));
            }
        }
    }

    /**
     * Starts the server
     */
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            send(new WelcomeMessage());
            registerName();
            while (isActive()) {
                handleMessage();
            }
        } catch (Exception e) {
            //System.out.println("solo questo");
        } finally {
            close();
        }
    }


    /**
     * Handles name registration
     * @throws IOException if the client disconnect at anytime
     */
    public void registerName() throws IOException {
        while (true) {
            try {
                RegisterName registerName = (RegisterName) receiveLobbyMessage();

                registerName.handle(this, currentLobby);
                currentLobby.enterLobby(this);
                System.out.println("I have registered the player: " + nickname);
                send(new RegisteredNameMessage());
                return;
            } catch (InvalidNameException | ClassNotFoundException e) {
                send(new InvalidNameMessage());
            }
        }
    }

    /**
     * Handles a client message
     */
    public void handleMessage() {
        if (currentLobby.getType() == LobbyType.MAIN_LOBBY) {
            ClientLobbyMessage read;
            try {
                read = receiveLobbyMessage();
                System.out.println("Received main lobby message by " + nickname + ": " + read.toString());
                currentLobby.handleMessage(read, this);
                if (currentLobby.getType() == LobbyType.GAME_LOBBY) {
                    GameLobby gameLobby = (GameLobby) currentLobby;
                    send(new GameInfoMessage(gameLobby.getId(), gameLobby.getMaxPlayers()));
                }
            } catch (Exception e) {
                System.out.println("Error while receiving lobby message: " + e.getMessage());
                send(new ErrorMessage());
            }
        } else {
            ClientGameMessage read;
            try {
                read = receiveGameMessage();
                System.out.println("Received game lobby message by " + nickname + ": " + read.toString());
                currentLobby.handleMessage(read, this);
            } catch (ClassCastException e) {
                System.out.println("Player " + nickname + " tried to use a main lobby command inside whislt inside a game lobby");
                sendFailedMoveMessage("Main lobby commands not available inside game lobby");
            } catch (Exception e) {
                System.out.println("\nException occured: \n");
                System.out.println(e.getClass());
                System.out.println(e.getMessage());
                System.out.println("\n");
                send(new ErrorMessage());
            }
        }
    }

    /**
     * Sends the details of a game lobby
     */
    public void sendGameLobbiesDetails() {
        ArrayList<GameLobbyDetails> gameLobbies = new ArrayList<>();
        mainLobby.getActiveGameLobbies().stream().filter(gameLobby -> gameLobby.getMaxPlayers() != 1)
                .forEach(gameLobby -> gameLobbies.add(new GameLobbyDetails(gameLobby.getId(),
                gameLobby.getCreator(), gameLobby.getConnectedPlayers().size(), gameLobby.getMaxPlayers()))
        );
        send(new GameLobbiesMessage(gameLobbies));
    }

    /**
     * Sends the details of a just entered lobby
     * @param id
     * @param isHost
     */
    public void sendGameLobbyEntered(String id, boolean isHost) {
        send(new SuccessfulConnectionToGameLobbyMessage(id, isHost));
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sends a CLI update message with a certain menuCode, which will correspond to a certain menu
     * @param menuCode the menu code string
     */
    public void sendCLIupdateMessage(String menuCode) {
        send(new CLIMenuUpdate(menuCode));
    }

    /**
     * Sends a notification to announce that a player has joined a lobby
     * @param player the player name
     */
    public void playerJoinedLobby(String player) {
        send(new PlayerJoinedMessage(player));
    }

    /**
     * Sends an update for when a notify is called
     * @param message the update class
     */
    @Override
    public void update(FaithTrack message) {
        send(new FaithTrackUpdateMessage(message));
    }

    @Override
    public void update(Warehouse message) {
        send(new WarehouseUpdateMessage(message));
    }

    @Override
    public void update(Dashboard message) {
        send(new DashboardUpdateMessage(message));
    }

    @Override
    public void update(Player message) {
        send(new PlayerUpdateMessage(message));
    }

    /**
     * Updates the game and check for game over proceedings, handling them if necessary.
     * @param message   update message.
     */
    @Override
    public void update(Game message) {
        send(new GameUpdateMessage(message));
        try {
            GameLobby gl = (GameLobby) currentLobby;
            if(gl.getConnectedPlayers().size() == 1) {
                if(message.getGameEnded()) {
                    if(!gl.isGameOver()) {
                        gl.gameOverSinglePlayer();
                    }
                }

            } else {
                if(message.isEndGamePhase()) {
                    if(!gl.isEndGamePhase()) {
                        gl.startEndGamePhase();
                    };
                }
                if(message.getGameEnded()) {
                    if(!gl.isGameOver()) {
                        gl.gameOverMultiPlayer();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in game end handling, please try again");
        }
    }

    @Override
    public void update(GameBoard message) {
        send(new GameBoardUpdateMessage(message));
    }

    @Override
    public void update(DevelopmentCardGrid message) {
        send(new DevelopmentCardGridUpdateMessage(message));
    }

    @Override
    public void update(Market message) {
        send(new MarketUpdateMessage(message));
    }

    @Override
    public void update(GameError message) {
        send(new GameErrorUpdateMessage(message));
    }
}


