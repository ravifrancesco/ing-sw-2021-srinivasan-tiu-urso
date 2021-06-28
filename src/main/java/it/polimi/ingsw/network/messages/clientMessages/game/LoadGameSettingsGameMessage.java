package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class LoadGameSettingsGameMessage extends ClientGameMessage implements Serializable {

    private GameSettings gameSettings;

    public LoadGameSettingsGameMessage(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        try {
            controller.loadGameSettings(gameSettings);
        } catch (Exception e) {
            // TODO
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        try {
            controller.loadGameSettings(gameSettings);
        } catch (Exception e) {
            // TODO
        }
    }
}
