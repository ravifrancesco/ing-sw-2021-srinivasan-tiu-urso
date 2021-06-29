package it.polimi.ingsw.view.UI;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.model.reduced.ReducedModel;
import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;

import java.util.ArrayList;

public interface UI {

    /**
     * Starts the UI with the online view
     * @param clientVirtualView the client virtual view
     * @param reducedModel the associated reduced model
     */
    void startUI(ClientVirtualView clientVirtualView, ReducedModel reducedModel);


    /**
     * Starts the UI with the offline view
     * @param offlineClientVirtualView the client offline virtual view
     * @param reducedModel the associated reduced model
     */
    void startUI(OfflineClientVirtualView offlineClientVirtualView, ReducedModel reducedModel);

    /**
     * Returns an UI type
     * @return the UI type
     */
    UIType getType();

    /**
     * @return the user nickname
     */
    String getNickname();

    /**
     * Print a message
     * @param s the message
     */
    void printMessage(String s);

    /**
     * Prints an error message
     * @param s the error message
     */
    void printErrorMessage(String s);

    /**
     * Prints a colored message
     * @param message the message
     * @param ansiGreen the associated color
     */
    void printColoredMessage(String message, String ansiGreen);

    /**
     * Shows the game lobbies
     * @param gameLobbies the game lobbies
     */
    void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies);

    /**
     * Handles entering a certain game phase for the single player game
     * @param isHost true if the player is the host
     * @param isOffline true if playing offline
     */
    void enterGamePhase(boolean isHost, boolean isOffline);

    /**
     * Handles a menu code from the server
     * @param menuCode the menu code
     */
    void handleMenuCode(String menuCode);

    /**
     * @return the reduced model
     */
    ReducedModel getReducedModel();
}
