package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class DevelopmentCardController {

    private GUI gui;

    @FXML
    private ImageView item;

    private DevelopmentCard developmentCard;

    private Slot[] slots;

    private int currentSlot;

    public void assignSlots(Slot[] slots) {
        this.slots = slots;
    }

    public void createItem(DevelopmentCard developmentCard, int slot) {
        String name = "dev_card_" + developmentCard.getId() + ".png";
        File file = new File("src/main/resources/png/cards/devCards/"+name);
        Image image = new Image(file.toURI().toString());
        item = new ImageView(image);
        item.setFitWidth(slots[slot].getWidth());
        item.setFitHeight(slots[slot].getHeight());

        // TODO

        //item.setOnMousePressed(this::pressed);
        //item.setOnMouseDragged(this::dragged);
        //item.setOnMouseReleased(this::released);

        item.setX(slots[slot].getX());
        item.setY(slots[slot].getY());

        slots[slot].filLSlot();
        currentSlot = slot;
        this.developmentCard = developmentCard;
    }

    public ImageView getItem() {
        return this.item;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
