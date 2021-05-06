package it.polimi.ingsw.server;
/*
import com.sun.tools.javac.Main;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static int PORT = 53510;
    static int THREAD_NUMBER = 256;

    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    private MainLobby lobby;

    private List<Connection> connections = new ArrayList<>();
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private Map<Connection, Connection> playingConnection = new HashMap<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run() {
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this, lobby);
                registerConnection(connection);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    private synchronized void registerConnection(Connection c){
        connections.add(c);
    }

    public synchronized void deregisterConnection(Connection c) {
        connections.remove(c);
        Connection opponent = playingConnection.get(c);
        if(opponent != null){
            opponent.closeConnection();
            playingConnection.remove(c);
            playingConnection.remove(opponent);
        }
    }





}

 */

