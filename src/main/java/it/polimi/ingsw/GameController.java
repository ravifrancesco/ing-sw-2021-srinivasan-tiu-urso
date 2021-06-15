package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GameController {

    private GUI gui;
    @FXML Pane pane;

    private Slot[] slots;

    private Node[] nodes;

    private Slot[] faithSlots;

    private Node cross;

    private ResourceController[] resourceControllers;

    private FaithMarkerController faithMarkerController;

    public static final int NUM_SHELFES = 6;

    public static final int NUM_FAITH_SLOTS = 25;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void initialize() {
        slots = new Slot[NUM_SHELFES];
        nodes = new Node[NUM_SHELFES];
        resourceControllers = new ResourceController[NUM_SHELFES];
        slots[0] = new Slot(108, 306, 52, 52);
        slots[1] = new Slot(90, 366, 42, 52);
        slots[2] = new Slot(132, 366, 42, 52);
        slots[3] = new Slot(68, 430, 42, 52);
        slots[4] = new Slot(110, 430, 40, 52);
        slots[5] = new Slot(150, 430, 42, 52);

        for (Slot slot : slots) {
            pane.getChildren().add(slot.getRectangle());
        }

        initializeFaithTrack();

        printResource(Resource.GOLD);
        printResource(Resource.SHIELD);
        printResource(Resource.SHIELD);
    }

    public void printResource(Resource resource) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resource_item.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResourceController resourceController = loader.getController();
        resourceController.assignSlots(slots);
        resourceController.createItem(resource);

        resourceController.setX(100);
        resourceController.setY(100);

        pane.getChildren().add(resourceController.getItem());
    }

    @FXML
    private void testClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
        faithMarkerController.moveFaithMarker(faithMarkerController.getPosition()+1);
    }

    public void initializeFaithTrack() {
        faithSlots = new Slot[NUM_FAITH_SLOTS];
        faithSlots[0] = new Slot(38, 140, 45, 45);
        faithSlots[1] = new Slot(87, 140, 45, 45);
        faithSlots[2] = new Slot(135, 140, 45, 45);
        faithSlots[3] = new Slot(135, 90, 45, 45);
        faithSlots[4] = new Slot(135, 42, 45, 45);
        faithSlots[5] = new Slot(184, 42, 45, 45);
        faithSlots[6] = new Slot(233, 42, 45, 45);
        faithSlots[7] = new Slot(283, 42, 45, 45);
        faithSlots[8] = new Slot(332, 42, 45, 45);
        faithSlots[9] = new Slot(380, 42, 45, 45);
        faithSlots[10] = new Slot(380, 90, 45, 45);
        faithSlots[11] = new Slot(380, 140, 45, 45);
        faithSlots[12] = new Slot(430, 140, 45, 45);
        faithSlots[13] = new Slot(478, 140, 45, 45);
        faithSlots[14] = new Slot(528, 140, 45, 45);
        faithSlots[15] = new Slot(576, 140, 45, 45);
        faithSlots[16] = new Slot(626, 140, 45, 45);
        faithSlots[17] = new Slot(626, 90, 45, 45);
        faithSlots[18] = new Slot(624, 42, 45, 45);
        faithSlots[19] = new Slot(674, 42, 45, 45);
        faithSlots[20] = new Slot(724, 42, 45, 45);
        faithSlots[21] = new Slot(772, 42, 45, 45);
        faithSlots[22] = new Slot(822, 42, 45, 45);
        faithSlots[23] = new Slot(870, 42, 45, 45);
        faithSlots[24] = new Slot(920, 42, 45, 45);

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

}
