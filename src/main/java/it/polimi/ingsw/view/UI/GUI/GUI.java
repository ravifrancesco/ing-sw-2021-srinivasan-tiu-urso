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
import java.nio.charset.StandardCharsets;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/server_choice.fxml"));
        Parent root = fxmlLoader.load();
        serverChoiceController = fxmlLoader.getController();
        serverChoiceController.setGui(this);
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @Override
    public void startUI(ClientVirtualView clientVirtualView, ReducedModel reducedModel) {
        local = false;
        this.offlineClientVirtualView = null;
        this.clientVirtualView = clientVirtualView;
        this.reducedModel = reducedModel;
    }

    @Override
    public void startUI(OfflineClientVirtualView offlineClientVirtualView, ReducedModel reducedModel) {
        local = true;
        this.clientVirtualView = null;
        this.offlineClientVirtualView = offlineClientVirtualView;
        this.reducedModel = reducedModel;
    }

    @Override
    public UIType getType() {
        return UIType.GUI;
    }

    @Override
    public String getNickname() {
        return nicknameChoiceController.getNickname();
    }

    @Override
    public void printMessage(String s) {
        System.out.println(s);
    }

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

    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        clientMainLobbyController.updateServerList(gameLobbies);
    }

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

    public ClientVirtualView getClientConnection() {
        return clientVirtualView;
    }

    public void send(ClientMessage clientMessage) {
        if (local) {
            offlineClientVirtualView.send(clientMessage);
        } else {
            clientVirtualView.send(clientMessage);
        }
    }

    public NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    public void setNicknameChoiceController(NicknameChoiceController nicknameChoiceController) {
        this.nicknameChoiceController = nicknameChoiceController;
    }

    public ClientMainLobbyController getClientMainLobbyController() {
        return clientMainLobbyController;
    }

    public void setClientMainLobbyController(ClientMainLobbyController clientMainLobbyController) {
        this.clientMainLobbyController = clientMainLobbyController;
    }

    @Override
    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    public void changeSupplyController(int supplyPos, int pos) {
        gameController.changeSupplyController(supplyPos, pos);
    }

    public Resource[] getDepositView() {
        return gameController.getDepositView();
    }

    public Resource[] getExtraDepositView(int lcIndex) {
        return gameController.getExtraDepositView(lcIndex);
    }

    public void setDisableNoDeposit() {
        gameController.setDisableNoDeposit();
    }

    public void setEnableNoDeposit() {
        gameController.setEnableNoDeposit();
    }

    public void changeResourceController(int from, int to) {
        gameController.changeResourceController(from, to);
    }

    public void showWarehouseButtons() {
        gameController.showWarehouseButtons();
    }

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
            Platform.runLater(() -> gameController.gameHasEnded());
        }
        if ("game_has_ended_single".equals(menuCode)) {
            Platform.runLater(() -> gameController.gameHasEndedSinglePlayer());
        }
        if ("force_disconnection".equals(menuCode)) {
            Platform.runLater(() -> gameController.forceDisconnection());
        }
    }

    public void setDisableImageViews() {
        gameController.setDisableImageViews();
    }

    public void setEnableImageViews() {
        gameController.setEnableImageViews();
    }
}
