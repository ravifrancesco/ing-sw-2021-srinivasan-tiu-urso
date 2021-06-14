package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class ResourceController {

    @FXML ImageView item;

    double x;

    double y;

    Resource resourceType;

    Rectangle[] slots;

    public void assignSlots(Rectangle[] slots) {
        this.slots = slots;
    }

    public void createItem(Resource resourceType) {
        String name = "";
        this.resourceType = resourceType;
        switch(resourceType) {
            case GOLD -> name = "coin";
            case SHIELD -> name = "shield";
            case STONE -> name = "stone";
            case SERVANT -> name = "servant";
        }
        File file = new File("src/main/resources/png/"+name+".png");
        Image image = new Image(file.toURI().toString());
        item = new ImageView(image);
        item.setFitHeight(25);
        item.setFitWidth(25);
        item.setOnMousePressed(this::pressed);
        item.setOnMouseDragged(this::dragged);
        item.setOnMouseReleased(this::released);
    }

    public void setX(double x) {
        this.x = x;
        item.setX(this.x);
    }

    public void setY(double y) {
        this.y = y;
        item.setY(this.y);
    }

    public ImageView getItem() {
        return this.item;
    }

    @FXML
    public void pressed(MouseEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        item.setEffect(colorAdjust);
    }

    private void dragged(MouseEvent event) {
        item.setX(event.getX()-item.getFitWidth()/2);
        item.setY(event.getY()-item.getFitHeight()/2);
    }

    private void released(MouseEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        item.setEffect(colorAdjust);
        computePosition(event);
    }

    private void computePosition(MouseEvent event) {
        double x = event.getX() + item.getFitWidth()/2;
        double y = event.getY() + item.getFitHeight()/2;
        if (y >= slots[0].getY() && y <= slots[1].getY() + slots[0].getHeight() &&
                x >= slots[0].getX() && x <= slots[0].getX() + slots[0].getWidth()) {
            setX((slots[0].getX()+slots[0].getWidth()/2)-(item.getFitWidth()/2));
            setY((slots[0].getY()+slots[0].getHeight()/2)-(item.getFitWidth()/2));
        }
        if (y >= slots[1].getY() && y <= slots[1].getY() + slots[1].getHeight()) {
            if (x >= slots[1].getX() && x <= slots[1].getX() + slots[1].getWidth()) {
                setX((slots[1].getX()+slots[1].getWidth()/2)-(item.getFitWidth()/2));
                setY((slots[1].getY()+slots[1].getHeight()/2)-(item.getFitWidth()/2));
            } else if (x >= slots[2].getX() && x <= slots[2].getX() + slots[2].getWidth()) {
                setX((slots[2].getX()+slots[2].getWidth()/2)-(item.getFitWidth()/2));
                setY((slots[2].getY()+slots[2].getHeight()/2)-(item.getFitWidth()/2));
            } else {
                setX(this.x);
                setY(this.y);
            }
        }
        else if (y >= slots[3].getY() && y <= slots[3].getY() + slots[3].getHeight()) {
            if (x >= slots[3].getX() && x <= slots[3].getX() + slots[3].getWidth()) {
                setX((slots[3].getX()+slots[3].getWidth()/2)-(item.getFitWidth()/2));
                setY((slots[3].getY()+slots[3].getHeight()/2)-(item.getFitWidth()/2));
            } else if (x >= slots[4].getX() && x <= slots[4].getX() + slots[4].getWidth()) {
                setX((slots[4].getX()+slots[4].getWidth()/2)-(item.getFitWidth()/2));
                setY((slots[4].getY()+slots[4].getHeight()/2)-(item.getFitWidth()/2));
            } else if (x >= slots[5].getX() && x <= slots[5].getX() + slots[5].getWidth()) {
                setX((slots[5].getX()+slots[5].getWidth()/2)-(item.getFitWidth()/2));
                setY((slots[5].getY()+slots[5].getHeight()/2)-(item.getFitWidth()/2));
            } else {
                setX(this.x);
                setY(this.y);
            }
        } else {
            setX(this.x);
            setY(this.y);
        }
    }
}
