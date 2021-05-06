package it.polimi.ingsw.server;

import it.polimi.ingsw.server.lobby.MainLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int PORT = 53510;

    public static final int THREAD_NUMBER = 256;

    private final ServerSocket serverSocket;

    private final MainLobby mainLobby;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.mainLobby = new MainLobby();
    }

    public void run() {
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this, mainLobby);
                registerConnection(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    private synchronized void registerConnection(Connection c) {
        mainLobby.registerConnection(c);
    }

    public synchronized void deregisterConnection(Connection c) {
        mainLobby.deregisterConnection(c);
    }





}



