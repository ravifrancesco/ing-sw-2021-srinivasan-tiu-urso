package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import javax.naming.InvalidNameException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientConnection implements Runnable {
    private String playerNickname;

    private String ip;
    private int port;
    public final CLI cli;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private ReducedModel reducedModel;

    private boolean nameRegistered;

    public ClientConnection(String ip, int port, CLI cli) {
        this.ip = ip;
        this.port = port;
        this.cli = cli;
        this.nameRegistered = false;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void connectToServer() throws IOException {
        System.out.println("Connection at " + ip + " on port " + port);
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            ServerMessage serverMessage = receiveServerMessage();
            serverMessage.updateClient(this, null);

        } catch (Exception e) {
            // connection failed
        }
    }

    public void registerName() throws IOException, ClassNotFoundException {
        while(!nameRegistered) {
            String nickname = cli.getNickname();
            send(new RegisterName(nickname));
            try {
                ServerMessage serverMessage = receiveServerMessage();
                serverMessage.updateClient(this, nickname);

            } catch (Exception e) {
                cli.printErrorMessage("Connection error while reading server response");
            }
        }
    }

    public ServerMessage receiveServerMessage() throws IOException, ClassNotFoundException {
        ServerMessage serverMessage =  (ServerMessage) inputStream.readObject();
        return serverMessage;
    }

    public void nicknameRegister(String playerNickname, ObjectInputStream input) throws IOException, ClassNotFoundException {
        send(new RegisterName(playerNickname));
        ServerMessage serverMessage = (ServerMessage) input.readObject();

    }

    public void nameRegistered() {
        nameRegistered = true;
    }

    private synchronized void send(ClientMessage message){

        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            // close()
        }

    }

    @Override
    public void run() {

    }
}
