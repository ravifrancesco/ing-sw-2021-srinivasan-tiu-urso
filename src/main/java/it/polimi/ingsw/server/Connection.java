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
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.ErrorMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.InvalidNameMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.SuccessfulConnectionMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.WelcomeMessage;
import it.polimi.ingsw.server.lobby.messages.serverMessages.updates.*;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TODO doc
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

    private final MainLobby mainLobby;
    private Lobby currentLobby;

    private boolean active = true;

    public Connection(Socket socket, Server server, MainLobby mainLobby){
        this.socket = socket;
        this.server = server;
        this.mainLobby = mainLobby;
        this.currentLobby = mainLobby;
    }

    public synchronized void enterLobby(Lobby lobby) {
        this.currentLobby = lobby;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private void send(ServerMessage message){

        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            close();
        }

    }

    public void asyncSend(ServerMessage message){
        new Thread(() -> send(message)).start();
    }

    public ClientGameMessage receiveGameMessage() throws IOException, ClassNotFoundException {
        return (ClientGameMessage) in.readObject();
    }

    public ClientLobbyMessage receiveLobbyMessage() throws IOException, ClassNotFoundException {
        return (ClientLobbyMessage) in.readObject();
    }

    private synchronized void closeConnection() {
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    private void deregisterConnection() {

        if (currentLobby.getType() == LobbyType.GAME_LOBBY) {
            ((GameLobby) currentLobby).leaveLobby(this);
        }
        mainLobby.deregisterConnection(this);

    }

    public void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        deregisterConnection();
        System.out.println("Done!");
    }

    @Override
    public void run() {
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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
                registerName.handle(this, currentLobby);
                currentLobby.enterLobby(this);
                System.out.println("I have registered the player");
                System.out.println(nickname);
                return;
            } catch (InvalidNameException | IOException | ClassNotFoundException e) {
                asyncSend(new InvalidNameMessage());
            }
        }
    }

    public synchronized void handleMessage() throws IOException {
        if (currentLobby.getType() == LobbyType.MAIN_LOBBY) {
            ClientLobbyMessage read;
            try {
                read = receiveLobbyMessage();
                currentLobby.handleMessage(read, this);
                asyncSend(new SuccessfulConnectionMessage(((GameLobby)currentLobby).getId()));
            } catch (ClassNotFoundException | IllegalArgumentException | InvalidNameException | IllegalStateException e) {
                asyncSend(new ErrorMessage());
            }
        } else {
            ClientGameMessage read;
            try {
                read = receiveGameMessage();
                currentLobby.handleMessage(read, this);
            } catch (ClassNotFoundException | InvalidNameException e) {
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


