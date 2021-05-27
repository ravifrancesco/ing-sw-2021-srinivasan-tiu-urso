package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.ClientInputParser;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private String playerNickname;

    private String ip;
    private int port;
    public final CLI cli;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean nameRegistered;

    private Socket socket;

    public ClientConnection(String ip, int port, CLI cli) {
        this.ip = ip;
        this.port = port;
        this.cli = cli;
        this.nameRegistered = false;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
        this.cli.getReducedModel().setNickname(playerNickname);
    }

    public void connectToServer() throws IOException {
        System.out.println("Connection at " + ip + " on port " + port);
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
            // welcome message not received
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
        return (ServerMessage) inputStream.readObject();
    }

    public void nameRegistered() {
        nameRegistered = true;
    }

    private synchronized void send(ClientMessage message){
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (Exception e) {
            close();
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        startReceivingThread();
        startReadingThread();
    }

    private void startReceivingThread() {
        new Thread(() -> {
            while(true) {
                try {
                    ServerMessage serverMessage = receiveServerMessage();
                    System.out.println("Received server message: " + serverMessage.toString());
                    serverMessage.updateClient(this, playerNickname);

                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Connection with server lost");
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }



    private void startReadingThread() {
        // TODO how to kill this thread if the receivingThread dies?
        new Thread(() -> {
            while(true) {
                String command = cli.readCommand();
                ClientMessage clientMessage = ClientInputParser.parseInput(command);
                if (clientMessage == null) {
                    cli.printErrorMessage("Invalid command");
                } else {
                    try {
                        send(clientMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // TODO ugly asf, need to find a way to show the "enter command" after server answers
                // and client is shown the response to its command
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
