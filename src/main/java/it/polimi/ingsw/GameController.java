package it.polimi.ingsw;

import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
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

        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setFill(Color.TRANSPARENT);
            rectangles[i].setStroke(Color.TRANSPARENT);
            pane.getChildren().add(rectangles[i]);
        }

        pane.getChildren().add(getResource(100, 100, "coin"));
        pane.getChildren().add(getResource(200, 200, "servant"));
        pane.getChildren().add(getResource(300, 300, "shield"));

    }

    @FXML
    private void testClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
    }

    private ImageView getResource(int x, int y, String resourceType) {
        File file = new File("src/main/resources/png/"+resourceType+".png");
        Image image = new Image(file.toURI().toString());
        ImageView resource = new ImageView(image);

        resource.setX(x);
        resource.setY(y);
        resource.setFitHeight(25);
        resource.setFitWidth(25);
        resource.setOnMousePressed(event -> pressed(event, resource));
        resource.setOnMouseDragged(event -> dragged(event, resource));
        resource.setOnMouseReleased(event -> released(event, resource));

        return resource;
    }

    private void pressed(MouseEvent event, ImageView resource) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        resource.setEffect(colorAdjust);
    }

    private void dragged(MouseEvent event, ImageView resource) {
        resource.setX(event.getX()-12.5);
        resource.setY(event.getY()-12.5);
    }

    private void released(MouseEvent event, ImageView resource) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        resource.setEffect(colorAdjust);
        computePosition(resource);
    }

    private void computePosition(ImageView resource) {
        double x = resource.getX() + 12.5;
        double y = resource.getY() + 12.5;
        if (y >= rectangles[0].getY() && y <= rectangles[1].getY() + rectangles[0].getHeight() &&
            x >= rectangles[0].getX() && x <= rectangles[0].getX() + rectangles[0].getWidth()) {
            resource.setX((rectangles[0].getX()+rectangles[0].getWidth()/2)-(resource.getFitWidth()/2));
            resource.setY((rectangles[0].getY()+rectangles[0].getHeight()/2)-(resource.getFitWidth()/2));
        }
        if (y >= rectangles[1].getY() && y <= rectangles[1].getY() + rectangles[1].getHeight()) {
            if (x >= rectangles[1].getX() && x <= rectangles[1].getX() + rectangles[1].getWidth()) {
                resource.setX((rectangles[1].getX()+rectangles[1].getWidth()/2)-(resource.getFitWidth()/2));
                resource.setY((rectangles[1].getY()+rectangles[1].getHeight()/2)-(resource.getFitWidth()/2));
            } else if (x >= rectangles[2].getX() && x <= rectangles[2].getX() + rectangles[2].getWidth()) {
                resource.setX((rectangles[2].getX()+rectangles[2].getWidth()/2)-(resource.getFitWidth()/2));
                resource.setY((rectangles[2].getY()+rectangles[2].getHeight()/2)-(resource.getFitWidth()/2));
            }
        }
        else if (y >= rectangles[3].getY() && y <= rectangles[3].getY() + rectangles[3].getHeight()) {
            if (x >= rectangles[3].getX() && x <= rectangles[3].getX() + rectangles[3].getWidth()) {
                resource.setX((rectangles[3].getX()+rectangles[3].getWidth()/2)-(resource.getFitWidth()/2));
                resource.setY((rectangles[3].getY()+rectangles[3].getHeight()/2)-(resource.getFitWidth()/2));
            } else if (x >= rectangles[4].getX() && x <= rectangles[4].getX() + rectangles[4].getWidth()) {
                resource.setX((rectangles[4].getX()+rectangles[4].getWidth()/2)-(resource.getFitWidth()/2));
                resource.setY((rectangles[4].getY()+rectangles[4].getHeight()/2)-(resource.getFitWidth()/2));
            } else if (x >= rectangles[5].getX() && x <= rectangles[5].getX() + rectangles[5].getWidth()) {
                resource.setX((rectangles[5].getX()+rectangles[5].getWidth()/2)-(resource.getFitWidth()/2));
                resource.setY((rectangles[5].getY()+rectangles[5].getHeight()/2)-(resource.getFitWidth()/2));
            }
        }

    }

}
