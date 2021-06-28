package it.polimi.ingsw.view.UI;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.model.reduced.ReducedModel;
import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;

import java.util.ArrayList;

public interface UI {

    void startUI(ClientVirtualView clientVirtualView, ReducedModel reducedModel);

    void startUI(OfflineClientVirtualView offlineClientVirtualView, ReducedModel reducedModel);

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
