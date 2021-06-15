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

    private ResourceController[] resourceControllers;

    public static final int NUM_SHELFES = 6;

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
    }

}
