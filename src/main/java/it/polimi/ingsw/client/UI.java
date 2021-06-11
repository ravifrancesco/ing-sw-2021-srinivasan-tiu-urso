package it.polimi.ingsw.client;

import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.util.ArrayList;

public interface UI {

    void startUI(ClientConnection clientConnection, ReducedModel reducedModel);

    UIType getType();

    String getNickname();

    void printMessage(String s);

    void printErrorMessage(String s);

    void printColoredMessage(String message, String ansiGreen);

    void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies);

    // TODO add to cli
    void enterGamePhase();
}
