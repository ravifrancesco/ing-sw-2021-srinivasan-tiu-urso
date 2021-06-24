package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.server.lobby.*;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.FailedMoveMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.SuccessfulMoveMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.*;
import it.polimi.ingsw.server.lobby.messages.serverMessages.updates.*;

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
public class Connection implements Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver, GameBoardObserver,
        DevelopmentCardGridObserver, MarketObserver, GameErrorObserver {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final Server server;
    private String nickname;

    private final MainLobby mainLobby;

    public MainLobby getMainLobby() {
        return mainLobby;
    }

    private Lobby currentLobby;

    private boolean active = true;

    public Connection(Socket socket, Server server, MainLobby mainLobby){
        this.socket = socket;
        this.server = server;
        this.mainLobby = mainLobby;
        this.currentLobby = mainLobby;
    }


    public Lobby getCurrentLobby() {
        return currentLobby;
    }

    public synchronized void enterLobby(Lobby lobby) {
        this.currentLobby = lobby;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(ServerMessage message) {

        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            close();
        }

    }

    public ClientGameMessage receiveGameMessage() throws IOException, ClassNotFoundException {
        return (ClientGameMessage) in.readObject();
    }

    public ClientLobbyMessage receiveLobbyMessage() throws IOException, ClassNotFoundException {
        return (ClientLobbyMessage) in.readObject();
    }

    public void sendSuccessfulMoveMessage(String message) {
        send(new SuccessfulMoveMessage(message));
    }

    public void sendFailedMoveMessage(String message) {
        send(new FailedMoveMessage(message));
    }

    private void closeConnection() {
        try{
            socket.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

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

    public synchronized void close() {
        if (active) {
            closeConnection();
            System.out.println("Deregistering client " + nickname + "..");
            deregisterConnection();
            System.out.println("Deregistration complete");
        }
    }

    @Override
    public void run() {
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            send(new WelcomeMessage());
            registerName();
            while(isActive()){
                handleMessage();
                System.out.println(isActive());
            }
        } catch (Exception e) {
            //System.out.println("solo questo");
        } finally {
            close();
        }
    }

    public void quit() throws IOException {
        throw new IOException();
    }

    public void registerName() throws IOException {
        while(true) {
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
                System.out.println("Player" + nickname + "tried to use a main lobby command inside whislt inside a game lobby");
                sendFailedMoveMessage("Main lobby commands not available inside game lobby");
            } catch (Exception e) {
                System.out.println("Ma perché");
                System.out.println(e.getClass());
                System.out.println(e.getMessage());
                send(new ErrorMessage());
            }
        }
    }

    public void sendGameLobbiesDetails() {
        ArrayList<GameLobbyDetails> gameLobbies = new ArrayList<>();
        mainLobby.getActiveGameLobbies().forEach(gameLobby -> gameLobbies.add(new GameLobbyDetails(gameLobby.getId(),
                        gameLobby.getCreator(), gameLobby.getConnectedPlayers().size(), gameLobby.getMaxPlayers()))
                );
        send(new GameLobbiesMessage(gameLobbies));
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void sendCLIupdateMessage(String menuCode) {
        send(new CLIMenuUpdate(menuCode));
    }

    /**
     *  TODO all of this methods and messages should handle only the necessary information. To be defined after client.
     *  TODO, this will be done changing the messages.
     *  TODO the notifies have to be handled. (Together)
     */

    public void playerJoinedLobby(String player) {
        send(new PlayerJoinedMessage(player));
    }


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

    @Override
    public void update(Game message) {
        send(new GameUpdateMessage(message));
    }

    @Override
    public void update(GameBoard message) { send(new GameBoardUpdateMessage(message));
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
    public void update(GameError message) { send(new GameErrorUpdateMessage(message));
    }
}


