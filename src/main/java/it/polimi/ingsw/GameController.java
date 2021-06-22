package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.StartGameGameMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class GameController {

    private GUI gui;
    @FXML Pane pane;

    private Alert mainAlert;

    private Slot[] depositSlots;

    private Slot[] extraDepositSlots;

    private Slot[] faithSlots;

    private Node cross;

    private ResourceController[] resourceControllers;

    private FaithMarkerController faithMarkerController;

    private ChooseResourceController chooseResourceController;

    public static final int NUM_SHELFES = 6;

    public static final int NUM_FAITH_SLOTS = 25;

    public static final int SIZE_EXTRA_DEPOSITS = 4;

    // Label for locker

    @FXML
    Label coinLabel;

    @FXML
    Label shieldLabel;

    @FXML
    Label stoneLabel;

    @FXML
    Label servantLabel;

    public void setGui(GUI gui) {
        this.gui = gui;
        this.gui.getReducedModel().setGameController(this);
    }

    @FXML
    public void initialize() {
        depositSlots = new Slot[NUM_SHELFES];
        extraDepositSlots = new Slot[SIZE_EXTRA_DEPOSITS];
        resourceControllers = new ResourceController[NUM_SHELFES + SIZE_EXTRA_DEPOSITS];
        depositSlots[0] = new Slot(327, 390, 52, 58);
        depositSlots[1] = new Slot(308, 448, 42, 58);
        depositSlots[2] = new Slot(350, 448, 42, 58);
        depositSlots[3] = new Slot(289, 506, 42, 52);
        depositSlots[4] = new Slot(331, 506, 38, 52);
        depositSlots[5] = new Slot(369, 506, 42, 52);

        for (Slot slot : depositSlots) {
            pane.getChildren().add(slot.getRectangle());
        }

        Resource[] deposit = new Resource[6];
        deposit[0] = Resource.GOLD;
        deposit[2] = Resource.SHIELD;
        deposit[5] = Resource.SERVANT;

        Map<Resource, Integer> map = new HashMap<>();
        map.put(Resource.GOLD, 5);
        map.put(Resource.SERVANT, 3);

        Resource[][] extraDeposits = new Resource[2][2];

        extraDeposits[0][0] = Resource.GOLD;
        extraDeposits[0][1] = Resource.SHIELD;
        extraDeposits[1][0] = Resource.STONE;
        extraDeposits[1][1] = Resource.SERVANT;

        printWarehouse(deposit, map, extraDeposits, new ArrayList<>());

        initializeFaithTrack();

        cleanWarehouse();

        Resource[] deposit2 = new Resource[6];
        deposit2[0] = Resource.SHIELD;
        deposit2[2] = Resource.GOLD;
        deposit2[4] = Resource.STONE;

        Map<Resource, Integer> map2 = new HashMap<>();
        map2.put(Resource.SHIELD, 2);
        map2.put(Resource.STONE, 3);

        Resource[][] extraDeposits2 = new Resource[2][2];

        extraDeposits2[0][0] = Resource.GOLD;
        extraDeposits2[0][1] = Resource.SHIELD;
        extraDeposits2[1][0] = Resource.STONE;
        extraDeposits2[1][1] = Resource.SERVANT;

        printWarehouse(deposit2, map2, extraDeposits2, new ArrayList<>());
    }

    public void printResource(Resource resource, int pos, Slot[] slots) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resource_item.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resourceControllers[pos] = loader.getController();
        resourceControllers[pos].assignSlots(slots);
        resourceControllers[pos].createItem(resource, pos);

        pane.getChildren().add(resourceControllers[pos].getItem());
    }

    public void updateLockerLabel(Resource resource, int quantity) {
        String stringQuantity = "x " + Integer.toString(quantity);
        switch (resource) {
            case GOLD -> coinLabel.setText(stringQuantity);
            case SHIELD -> shieldLabel.setText(stringQuantity);
            case STONE -> stoneLabel.setText(stringQuantity);
            case SERVANT -> servantLabel.setText(stringQuantity);
        }
    }

    public void printWarehouse(Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposit, ArrayList<Resource> supply) {
        IntStream.range(0, deposit.length)
                .filter(i -> deposit[i] != null)
                .forEach(i -> printResource(deposit[i], i, depositSlots));

        locker.forEach(this::updateLockerLabel);

        // TODO extra deposit and supply
    }

    public void cleanWarehouse() {

        for (int i = 0; i < resourceControllers.length; i++) {
            if (resourceControllers[i] != null) {
                pane.getChildren().remove(resourceControllers[i].getItem());
                if (i < NUM_SHELFES) {
                    depositSlots[i].freeSlot();
                } else {
                    extraDepositSlots[i].freeSlot();
                }
                resourceControllers[i] = null;
            }
        }

        updateLockerLabel(Resource.GOLD, 0);
        updateLockerLabel(Resource.SHIELD, 0);
        updateLockerLabel(Resource.STONE, 0);
        updateLockerLabel(Resource.SERVANT, 0);

        // TODO extra deposit and supply
    }

    @FXML
    private void testClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
        faithMarkerController.moveFaithMarker(faithMarkerController.getPosition()+1);
        if (chooseResourceController == null) {
            openChooseResourceWindow();
        }
        else
        {
            System.out.println(chooseResourceController.getResourceContainer());
            System.out.println(chooseResourceController.getSelectedCardSlot());
        }
    }

    public void initializeFaithTrack() {
        faithSlots = new Slot[NUM_FAITH_SLOTS];
        faithSlots[0] = new Slot(267, 250, 40, 40);
        faithSlots[1] = new Slot(311, 250, 40, 40);
        faithSlots[2] = new Slot(355, 250, 40, 40);
        faithSlots[3] = new Slot(355, 206, 40, 40);
        faithSlots[4] = new Slot(355, 162, 40, 40);
        faithSlots[5] = new Slot(399, 162, 40, 40);
        faithSlots[6] = new Slot(443, 162, 40, 40);
        faithSlots[7] = new Slot(487, 162, 40, 40);
        faithSlots[8] = new Slot(531, 162, 40, 40);
        faithSlots[9] = new Slot(575, 162, 40, 40);
        faithSlots[10] = new Slot(575, 206, 40, 40);
        faithSlots[11] = new Slot(575, 250, 40, 40);
        faithSlots[12] = new Slot(619, 250, 40, 40);
        faithSlots[13] = new Slot(663, 250, 40, 40);
        faithSlots[14] = new Slot(707, 250, 40, 40);
        faithSlots[15] = new Slot(751, 250, 40, 40);
        faithSlots[16] = new Slot(795, 250, 40, 40);
        faithSlots[17] = new Slot(795, 206, 40, 40);
        faithSlots[18] = new Slot(795, 162, 40, 40);
        faithSlots[19] = new Slot(839, 162, 40, 40);
        faithSlots[20] = new Slot(883, 162, 40, 40);
        faithSlots[21] = new Slot(927, 162, 40, 40);
        faithSlots[22] = new Slot(971, 162, 40, 40);
        faithSlots[23] = new Slot(1015, 162, 40, 40);
        faithSlots[24] = new Slot(1059, 162, 40, 40);

        for (int i = 0; i < NUM_FAITH_SLOTS; i++) {
            pane.getChildren().add(faithSlots[i].getRectangle());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cross_item.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        faithMarkerController = loader.getController();
        faithMarkerController.assignSlots(faithSlots);
        faithMarkerController.createItem();

        pane.getChildren().add(faithMarkerController.getItem());
    }

    public void openChooseResourceWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_resource.fxml"));
            Parent root = fxmlLoader.load();
            chooseResourceController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Buy dv card");
            stage.setScene(new Scene(root, 500, 700));
            stage.show();

            Resource[] deposit = new Resource[6];
            deposit[0] = Resource.GOLD;
            deposit[2] = Resource.SHIELD;
            deposit[5] = Resource.SERVANT;

            Map<Resource, Integer> map = new HashMap<>();
            map.put(Resource.GOLD, 5);
            map.put(Resource.SERVANT, 3);

            Resource[][] extraDeposits = new Resource[2][2];

            extraDeposits[0][0] = Resource.GOLD;
            extraDeposits[0][1] = Resource.SHIELD;
            extraDeposits[1][0] = Resource.STONE;
            extraDeposits[1][1] = Resource.SERVANT;

            chooseResourceController.setResources(deposit, map, extraDeposits);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initHostAlert() {
        /*
        mainAlert = new Alert(Alert.AlertType.CONFIRMATION);
        mainAlert.setTitle("Start Game");
        mainAlert.setHeaderText("Start game when you are ready!");

        ButtonType buttonTypeOne = new ButtonType("Start");

        mainAlert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = mainAlert.showAndWait();
        if (result.get() == buttonTypeOne){
            this.gui.getClientConnection().send(new StartGameGameMessage());
            Platform.runLater(this::initHostAlert); // TODO make it better
        }

         */
    }

    public void initPlayerAlert() {
        mainAlert = new Alert(Alert.AlertType.NONE);
        mainAlert.setTitle("Start Game");
        mainAlert.setHeaderText("Waiting for host to start the game");
        mainAlert.show();
    }

    public void hideAlert() {
        if (mainAlert != null) {
            Platform.runLater(() -> mainAlert.close());
        }
    }

}
