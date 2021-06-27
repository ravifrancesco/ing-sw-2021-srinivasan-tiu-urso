package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ReducedModel;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.util.ArrayList;

public interface UI {

    void startUI(ClientConnection clientConnection, ReducedModel reducedModel);

    void startUI(SinglePlayerView singlePlayerView, ReducedModel reducedModel);

    UIType getType();

    String getNickname();

    void printMessage(String s);

    void printErrorMessage(String s);

    void printColoredMessage(String message, String ansiGreen);

    void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies);

    // TODO add to cli
    void enterGamePhase(boolean isHost, boolean isOffline);

    void handleMenuCode(String menuCode);

    ReducedModel getReducedModel();
}
