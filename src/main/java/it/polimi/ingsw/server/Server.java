package it.polimi.ingsw.server;

import it.polimi.ingsw.server.lobby.MainLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//  TODO Logger
public class Server {
    static final int PORT = 2000;

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
                mainLobby.registerConnection(connection);

                System.out.println("Connection registered!");
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }







}



