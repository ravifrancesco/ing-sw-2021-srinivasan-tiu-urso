package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.Warehouse;
import it.polimi.ingsw.model.observerPattern.observers.DashboardObserver;
import it.polimi.ingsw.model.observerPattern.observers.FaithTrackObserver;
import it.polimi.ingsw.model.observerPattern.observers.HandObserver;
import it.polimi.ingsw.model.observerPattern.observers.WarehouseObserver;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messageHandlers.LobbyMessages;
import it.polimi.ingsw.server.messages.ServerMessage;
import it.polimi.ingsw.server.messages.updates.DashboardUpdateMessage;
import it.polimi.ingsw.server.messages.updates.FaithTrackUpdateMessage;
import it.polimi.ingsw.server.messages.updates.HandUpdateMessage;
import it.polimi.ingsw.server.messages.updates.WarehouseUpdateMessage;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Runnable,
        FaithTrackObserver, WarehouseObserver, DashboardObserver, HandObserver {

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

    public synchronized void changeLobby(Lobby lobby) {
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
        asyncSend(ServerMessages.CONNECTION_CLOSED);
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    private void close(){
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
            send(ServerMessages.WELCOME_MESSAGE);
            registerName();
            asyncSend(ServerMessages.OK);
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
                send(LobbyMessages.INVALID_NAME);
            }
        }
    }

    public String getNickname() {
        return nickname;
    }

    /**
     *  TODO all of this methods and mesages should handle only the necessary information. To be defined after client.
     *  TODO, this will be done changing the messages.
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
    public void update(Hand message) {
        asyncSend(new HandUpdateMessage(message));
    }
}


