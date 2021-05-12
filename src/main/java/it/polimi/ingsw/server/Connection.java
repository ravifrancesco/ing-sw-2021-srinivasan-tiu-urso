package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.LobbyType;
import it.polimi.ingsw.server.lobby.MainLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.ConnectionClosedMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.ErrorMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.InvalidNameMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.WelcomeMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.updates.*;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TODO probably separate the lobbies
 * TODO add observers to gameLobby
 */
public class Connection implements Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver,
        PlayerObserver, GameObserver, GameBoardObserver,
        DevelopmentCardGridObserver, MarketObserver {

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Server server;
    private String nickname;
    private Lobby lobby;
    private boolean active = true;

    public Connection(Socket socket, Server server, Lobby lobby){
        this.socket = socket;
        this.server = server;
        this.lobby = lobby;
    }

    public synchronized void enterLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private void send(ServerMessage message){
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO solve related problems
    public void asyncSend(ServerMessage message){
        new Thread(() -> send(message)).start();
    }

    public ClientGameMessage receiveGameMessage() throws IOException, ClassNotFoundException {
        return (ClientGameMessage) in.readObject();
    }

    public ClientLobbyMessage receiveLobbyMessage() throws IOException, ClassNotFoundException {
        return (ClientLobbyMessage) in.readObject();
    }

    public synchronized void closeConnection(){
        asyncSend(new ConnectionClosedMessage());
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    public void deregisterConnection() {
        if (lobby.getType() == LobbyType.MAIN_LOBBY) {
            ((MainLobby) lobby).deregisterConnection(this);
        } else {
            ((GameLobby) lobby).leaveLobby(this);
            // TODO have reference to MainLobby
        }
    }

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        System.out.println("Done!");
    }

    @Override
    public void run() {
        try{
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            asyncSend(new WelcomeMessage());
            registerName();
            while(isActive()){
                handleMessage();
            }
        } catch(IOException e) {
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public void registerName() {
        while(true) {
            try {
                RegisterName registerName = (RegisterName) receiveLobbyMessage();
                registerName.handle(this, lobby);
                lobby.enterLobby(this);
                return;
            } catch (InvalidNameException | IOException | ClassNotFoundException e) {
                asyncSend(new InvalidNameMessage());
            }
        }
    }

    public synchronized void handleMessage() {
        if (lobby.getType() == LobbyType.MAIN_LOBBY) {
            ClientLobbyMessage read;
            try {
                read = receiveLobbyMessage();
                lobby.handleMessage(read, this);
            } catch (IOException | ClassNotFoundException e) {
                asyncSend(new ErrorMessage());
            }
        } else {
            ClientGameMessage read;
            try {
                read = receiveGameMessage();
                lobby.handleMessage(read, this);
            } catch (IOException | ClassNotFoundException e) {
                asyncSend(new ErrorMessage());
            }
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     *  TODO all of this methods and mesages should handle only the necessary information. To be defined after client.
     *  TODO, this will be done changing the messages.
     *  TODO the notifies have to be handled. (Together)
     */

    @Override
    public void update(FaithTrack message) {
        asyncSend(new FaithTrackUpdateMessage(message));
    }

    @Override
    public void update(Warehouse message) {
        asyncSend(new WarehouseUpdateMessage(message));
    }

    @Override
    public void update(Dashboard message) {
        asyncSend(new DashboardUpdateMessage(message));
    }

    @Override
    public void update(Player message) {
        asyncSend(new PlayerUpdateMessage(message));
    }

    @Override
    public void update(Game message) {
        asyncSend(new GameUpdateMessage(message));
    }

    @Override
    public void update(GameBoard message) {
        asyncSend(new GameBoardUpdateMessage(message));
    }

    @Override
    public void update(DevelopmentCardGrid message) {
        asyncSend(new DevelopmentCardGridUpdateMessage(message));
    }

    @Override
    public void update(Market message) {
        asyncSend(new MarketUpdateMessage(message));
    }
}


