package it.polimi.ingsw.server.lobby.messageHandlers;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.ServerMessages;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.MainLobby;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainLobbyMessageHandler {

    private final MainLobby mainLobby;

    private final List<GameLobby> gameLobbies;

    public MainLobbyMessageHandler(MainLobby mainLobby) {
        this.mainLobby = mainLobby;
        this.gameLobbies = new ArrayList<>();
    }

    public synchronized void handleMessage(String msg, Connection c) {

        switch (LobbyMessages.valueOf(msg.toUpperCase())) {
            case GET_SERVER_STATUS ->
                getServerStatus(c);
            case CREATE_GAME ->
                createGame(c);
            case JOIN_GAME ->
                joinGame(c);
            default ->
                c.asyncSend(ServerMessages.ERROR);
        }

    }

    public void getServerStatus(Connection c) {
        c.asyncSend(ServerMessages.OK);
        c.asyncSend(this.gameLobbies.stream()
                .collect(Collectors.toMap(GameLobby::getId, GameLobby::getLobbyStatus)));
    }

    public void createGame(Connection c) {

        try {
            int numberOfPlayers = (int) c.receive();
            mainLobby.createGame(c, numberOfPlayers);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            c.asyncSend(ServerMessages.ERROR);
        } catch (InvalidNameException e) {
            c.asyncSend(LobbyMessages.INVALID_NAME);
        } catch (IllegalStateException e) {
            c.asyncSend(ServerMessages.SERVER_FULL);
        }

    }

    public void joinGame(Connection c) {

        try {
            String gameID = (String) c.receive();
            mainLobby.joinGame(c, gameID);
            c.asyncSend(ServerMessages.OK);
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            c.asyncSend(ServerMessages.ERROR);
        } catch (InvalidNameException e) {
            c.asyncSend(LobbyMessages.INVALID_NAME);
        } catch (IllegalStateException e) {
            c.asyncSend(LobbyMessages.GAME_FULL);
        }

    }


}
