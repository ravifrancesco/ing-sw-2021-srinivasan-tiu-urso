package it.polimi.ingsw.view.UI.GUI;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.reduced.ReducedModel;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.view.UI.GUI.JavaFX.controllers.ClientMainLobbyController;
import it.polimi.ingsw.view.UI.GUI.JavaFX.controllers.GameController;
import it.polimi.ingsw.view.UI.GUI.JavaFX.controllers.NicknameChoiceController;
import it.polimi.ingsw.view.UI.GUI.JavaFX.controllers.ServerChoiceController;
import it.polimi.ingsw.view.UI.UI;
import it.polimi.ingsw.view.UI.UIType;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application implements UI {

    private ClientVirtualView clientVirtualView;
    private OfflineClientVirtualView offlineClientVirtualView;
    private boolean local;

    private ReducedModel reducedModel;

    private NicknameChoiceController nicknameChoiceController;
    private ServerChoiceController serverChoiceController;
    private ClientMainLobbyController clientMainLobbyController;
    private GameController gameController;

    public static void launchGUI() { launch(); }

    /**
     * Method to start GUI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/server_choice.fxml"));
        Parent root = fxmlLoader.load();
        serverChoiceController = fxmlLoader.getController();
        serverChoiceController.setGui(this);
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Method to start UI in online mode
     * @param clientVirtualView the client virtual view
     * @param reducedModel      the associated reduced model
     */
    @Override
    public void startUI(ClientVirtualView clientVirtualView, ReducedModel reducedModel) {
        local = false;
        this.offlineClientVirtualView = null;
        this.clientVirtualView = clientVirtualView;
        this.reducedModel = reducedModel;
    }

    /**
     * Method to start the UI in offline mode
     * @param offlineClientVirtualView the client offline virtual view
     * @param reducedModel             the associated reduced model
     */
    @Override
    public void startUI(OfflineClientVirtualView offlineClientVirtualView, ReducedModel reducedModel) {
        local = true;
        this.clientVirtualView = null;
        this.offlineClientVirtualView = offlineClientVirtualView;
        this.reducedModel = reducedModel;
    }

    /**
     * Getter for the UI type
     * @return the UI type
     */
    @Override
    public UIType getType() {
        return UIType.GUI;
    }

    /**
     * Getter for the nickname from the NicknameChoiceController
     * @return the nickname
     */
    @Override
    public String getNickname() {
        return nicknameChoiceController.getNickname();
    }

    /**
     * Method to print onto stdout
     * @param s the message
     */
    @Override
    public void printMessage(String s) {
        System.out.println(s);
    }

    /**
     * Method to print an error
     * @param s the message
     */
    @Override
    public void printErrorMessage(String s) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error box");
            alert.setHeaderText("");
            alert.setContentText(s);
            alert.showAndWait();
        });
    }

    @Override
    public void printColoredMessage(String message, String ansiGreen) {
        // no action
    }

    /**
     * Method to show the game lobbies
     * @param gameLobbies the game lobbies
     */
    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        clientMainLobbyController.updateServerList(gameLobbies);
    }

    /**
     * Method to enter in game phase
     * @param isHost    true if the player is the host
     * @param isOffline true if playing offline
     */
    @Override
    public void enterGamePhase(boolean isHost, boolean isOffline) {
        Platform.runLater(
                () -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
                        Parent root = fxmlLoader.load();
                        gameController = fxmlLoader.getController();
                        gameController.setGui(this);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root, 1366, 768));
                        stage.setTitle("Master of Renaissance");
                        stage.setResizable(false);
                        stage.setOnCloseRequest(t -> System.exit(0));
                        stage.show();
                        if (isOffline) {
                            serverChoiceController.getScene().getWindow().hide();
                        } else {
                            clientMainLobbyController.getScene().getWindow().hide();
                        }
                        if (isHost) {
                            gameController.initHostAlert();
                        } else {
                            gameController.initPlayerAlert();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * Method to get ClientVirtualView
     * @return the ClientVirtualView
     */
    public ClientVirtualView getClientConnection() {
        return clientVirtualView;
    }

    /**
     * Method to send a message to the server
     * @param clientMessage the message
     */
    public void send(ClientMessage clientMessage) {
        if (local) {
            offlineClientVirtualView.send(clientMessage);
        } else {
            clientVirtualView.send(clientMessage);
        }
    }

    /**
     * Getter for the NicknameChoiceController
     * @return the NicknameChoiceController
     */
    public NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    /**
     * Setter for the NicknameChoiceController
     * @param nicknameChoiceController the NicknameChoiceController
     */
    public void setNicknameChoiceController(NicknameChoiceController nicknameChoiceController) {
        this.nicknameChoiceController = nicknameChoiceController;
    }

    /**
     * Getter for the ClientMainLobbyController
     * @return the ClientMainLobbyController
     */
    public ClientMainLobbyController getClientMainLobbyController() {
        return clientMainLobbyController;
    }

    /**
     * Setter for the ClientMainLobbyController
     * @param clientMainLobbyController the ClientMainLobbyController
     */
    public void setClientMainLobbyController(ClientMainLobbyController clientMainLobbyController) {
        this.clientMainLobbyController = clientMainLobbyController;
    }

    /**
     * Getter for the reduced model
     * @return the ReducedModel
     */
    @Override
    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    /**
     * Method to change the controller from a supply controller to a resource controller
     * @param supplyPos the position of the resource in supply controllers array
     * @param pos the new position of the resource in resource controllers array
     */
    public void changeSupplyController(int supplyPos, int pos) {
        gameController.changeSupplyController(supplyPos, pos);
    }

    /**
     * Getter for the deposit view
     * @return the deposit view
     */
    public Resource[] getDepositView() {
        return gameController.getDepositView();
    }

    /**
     * Getter for the extra deposit view
     * @return the extra deposit view
     */
    public Resource[] getExtraDepositView(int lcIndex) {
        return gameController.getExtraDepositView(lcIndex);
    }

    /**
     * Method to disable all warehouse resources (no deposit)
     */
    public void setDisableNoDeposit() {
        gameController.setDisableNoDeposit();
    }

    /**
     * Method to enable all warehouse resources (no deposit)
     */
    public void setEnableNoDeposit() {
        gameController.setEnableNoDeposit();
    }

    /**
     * Method to change the position of a resource in resource controllers array
     * @param from the old position
     * @param to the new position
     */
    public void changeResourceController(int from, int to) {
        gameController.changeResourceController(from, to);
    }

    /**
     * Method to show warehouse buttons
     */
    public void showWarehouseButtons() {
        gameController.showWarehouseButtons();
    }

    /**
     * Method to handle the menu code
     * @param menuCode the menu code
     */
    @Override
    public void handleMenuCode(String menuCode) {
        if ("after_game_start".equals(menuCode)) {
            Platform.runLater(() -> gameController.showAfterGameStart());
        }
        if ("after_end_turn".equals(menuCode)) {
            Platform.runLater(() -> gameController.showAfterEndTurn());
        }
        if ("next_card_discard".equals(menuCode)) {
            Platform.runLater(() -> gameController.showDiscardExcessLeaderCardMenu());
        }
        if ("after_initial_resources".equals(menuCode)) {
            Platform.runLater(() -> gameController.getInitialResources());
        }
        if ("game_has_ended".equals(menuCode)) {
            reducedModel.getReducedGame().setGameIsEnded(true);
            Platform.runLater(() -> gameController.gameHasEnded());
        }
        if ("game_has_ended_single".equals(menuCode)) {
            reducedModel.getReducedGame().setGameIsEnded(true);
            Platform.runLater(() -> gameController.gameHasEndedSinglePlayer());
        }
        if ("force_disconnection".equals(menuCode) && !reducedModel.getReducedGame().isGameIsEnded()) {
            Platform.runLater(() -> gameController.forceDisconnection());
        }
    }

    /**
     * Method to disable image views
     */
    public void setDisableImageViews() {
        gameController.setDisableImageViews();
    }

    /**
     * Method to enable image views
     */
    public void setEnableImageViews() {
        gameController.setEnableImageViews();
    }
}
