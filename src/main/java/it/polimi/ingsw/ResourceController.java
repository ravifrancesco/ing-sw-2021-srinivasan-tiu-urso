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

    Slot[] slots;

    int currentSlot;

    public void assignSlots(Slot[] slots) {
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
        currentSlot = -1;
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
        double x = event.getX();
        double y = event.getY();

        for (int i = 0; i < GameController.NUM_SHELFES; i++) {
            if (slots[i].isPointInSlot(x, y) && slots[i].isEmpty()) {
                setX((slots[i].getX() + slots[i].getWidth()/2) - (item.getFitWidth() / 2));
                setY((slots[i].getY() + slots[i].getHeight()/2) - (item.getFitWidth() / 2));
                slots[i].filLSlot();
                if (currentSlot != -1) {
                    slots[currentSlot].freeSlot();
                }
                currentSlot = i;
                return;
            }
        }
        setX(this.x);
        setY(this.y);
    }
}
