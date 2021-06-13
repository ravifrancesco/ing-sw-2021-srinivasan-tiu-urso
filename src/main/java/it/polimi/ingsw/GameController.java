package it.polimi.ingsw;

import javafx.fxml.FXML;
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
        rectangles[0] = new Rectangle(116, 312, 38, 38);
        rectangles[1] = new Rectangle(96, 374, 38, 38);
        rectangles[2] = new Rectangle(132, 374, 38, 38);
        rectangles[3] = new Rectangle(76, 436, 38, 38);
        rectangles[4] = new Rectangle(116, 436, 38, 38);
        rectangles[5] = new Rectangle(152, 436, 38, 38);

        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setFill(Color.TRANSPARENT);
            rectangles[i].setStroke(Color.RED);
            pane.getChildren().add(rectangles[i]);
        }

        pane.getChildren().add(getCoin());

    }

    @FXML
    private void testClick(MouseEvent event) {
        System.out.println(event.getX());
        System.out.println(event.getY());
    }

    public ImageView getCoin() {
        File file = new File("src/main/resources/png/coin.png");
        Image image = new Image(file.toURI().toString());
        ImageView coin = new ImageView(image);

        coin.setX(100);
        coin.setY(100);
        coin.setFitHeight(30);
        coin.setFitWidth(30);
        return coin;
    }

}
