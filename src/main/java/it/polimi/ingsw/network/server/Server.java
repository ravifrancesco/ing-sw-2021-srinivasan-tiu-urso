package it.polimi.ingsw.network.server;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.MainLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//  TODO Logger
public class Server {
    static final int PORT = 3001;

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

                ServerVirtualView serverVirtualView = new ServerVirtualView(socket, this, mainLobby);
                mainLobby.registerConnection(serverVirtualView);

                System.out.println("Connection registered!");
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }







}



