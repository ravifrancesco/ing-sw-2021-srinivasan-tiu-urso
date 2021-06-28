package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

    public void setDarker() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        item.setEffect(colorAdjust);
    }

    public void openProductionPowerView(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/production_power.fxml"));
            Parent root = fxmlLoader.load();
            DevelopmentProductionController developmentProductionController = fxmlLoader.getController();
            developmentProductionController.setGui(this.gui);
            developmentProductionController.setResources();
            developmentProductionController.setCardIndex(currentSlot);
            Stage stage = new Stage();
            stage.setTitle("Select resources");
            stage.setScene(new Scene(root, 500, 600));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
