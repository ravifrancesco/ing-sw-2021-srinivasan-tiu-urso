package it.polimi.ingsw.server;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.observerPattern.observers.*;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.messages.ServerMessage;
import it.polimi.ingsw.server.messages.commons.ConnectionClosedMessage;
import it.polimi.ingsw.server.messages.commons.InvalidNameMessage;
import it.polimi.ingsw.server.messages.commons.WelcomeMessage;
import it.polimi.ingsw.server.messages.updates.*;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TODO proably separate the lobbies
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

    public Object receive() throws IOException, ClassNotFoundException {
        return in.readObject();
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

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
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
                String read = (String) receive();
                lobby.handleMessage(read, this);
            }
        } catch(IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public void registerName() {
        while(true) {
            try {
                nickname = (String) receive();
                lobby.enterLobby(this);
                return;
            } catch (InvalidNameException | IOException | ClassNotFoundException e) {
                asyncSend(new InvalidNameMessage());
            }
        }
    }

    public String getNickname() {
        return nickname;
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


