package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class GameController {

    private GUI gui;
    @FXML Pane pane;

    private Rectangle[] rectangles;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void initialize() {
        rectangles = new Rectangle[6];
        rectangles[0] = new Rectangle(108, 306, 52, 52);
        rectangles[1] = new Rectangle(90, 366, 42, 52);
        rectangles[2] = new Rectangle(132, 366, 42, 52);
        rectangles[3] = new Rectangle(68, 430, 42, 52);
        rectangles[4] = new Rectangle(110, 430, 40, 52);
        rectangles[5] = new Rectangle(150, 430, 42, 52);

        for (Rectangle rectangle : rectangles) {
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.TRANSPARENT);
            pane.getChildren().add(rectangle);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resource_item.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResourceController resourceController = loader.getController();
        resourceController.assignSlots(rectangles);
        resourceController.createItem(Resource.GOLD);

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
