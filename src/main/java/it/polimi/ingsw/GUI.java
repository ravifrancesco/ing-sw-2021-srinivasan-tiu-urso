package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application implements UI {

    private ClientConnection clientConnection;
    private ReducedModel reducedModel;

    private NicknameChoiceController nicknameChoiceController;
    private ServerChoiceController serverChoiceController;
    private ClientMainLobbyController clientMainLobbyController;
    private GameController gameController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ServerChoice.fxml"));
        Parent root = fxmlLoader.load();
        serverChoiceController = fxmlLoader.getController();
        serverChoiceController.setGui(this);
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void startUI(ClientConnection clientConnection, ReducedModel reducedModel) {
        this.clientConnection = clientConnection;
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error box");
        alert.setHeaderText("");
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public void printColoredMessage(String message, String ansiGreen) {
        // TODO
    }

    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        clientMainLobbyController.updateServerList(gameLobbies);
    }

    @Override
    public void enterGamePhase(boolean isHost) {
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
                        clientMainLobbyController.getScene().getWindow().hide();
                        if (isHost) {
                            gameController.initHostAlert();
                        } else {
                            gameController.initPlayerAlert();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void setNicknameChoiceController(NicknameChoiceController nicknameChoiceController) {
        this.nicknameChoiceController = nicknameChoiceController;
    }

    public NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    public void setClientMainLobbyController(ClientMainLobbyController clientMainLobbyController) {
        this.clientMainLobbyController = clientMainLobbyController;
    }

    public ClientMainLobbyController getClientMainLobbyController() {
        return clientMainLobbyController;
    }

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
        System.out.println("DEBUG " + menuCode);
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
            //showAfterInitialResources();
        }
        if ("after_getfrommarket".equals(menuCode)) {
            //showAfterMarketMenu();
        }
    }
}
