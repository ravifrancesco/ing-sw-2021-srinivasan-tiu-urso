package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.reduced.ReducedDashboard;
import it.polimi.ingsw.network.messages.clientMessages.game.GetInitialResourcesGameMessage;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ChooseBonusResourcesController {

    @FXML
    private ImageView goldIW;
    @FXML
    private ImageView servantIW;
    @FXML
    private ImageView shieldIW;
    @FXML
    private ImageView stoneIW;

    @FXML
    private ImageView slot0IW;
    @FXML
    private ImageView slot1IW;
    @FXML
    private ImageView slot2IW;
    @FXML
    private ImageView slot3IW;
    @FXML
    private ImageView slot4IW;
    @FXML
    private ImageView slot5IW;

    private List<ImageView> resources;
    private List<ImageView> slots;

    private Resource selectedResource;
    private Integer selectedIndex;

    private GUI gui;

    /**
     * Initialized method of the class
     */
    public void initialize() {
        initializeResources();
        initializeSlots();
    }

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
        setDeposit();
    }

    /**
     * Method to initialize the resources
     */
    private void initializeResources() {
        resources = Arrays.asList(goldIW, servantIW, shieldIW, stoneIW);
        goldIW.setOnMouseClicked(mouseEvent -> {
            resources.forEach(this::setBrightnessLow);
            setBrightnessHigh(goldIW);
            selectedResource = Resource.GOLD;
        });
        servantIW.setOnMouseClicked(mouseEvent -> {
            resources.forEach(this::setBrightnessLow);
            setBrightnessHigh(servantIW);
            selectedResource = Resource.SERVANT;
        });
        shieldIW.setOnMouseClicked(mouseEvent -> {
            resources.forEach(this::setBrightnessLow);
            setBrightnessHigh(shieldIW);
            selectedResource = Resource.SHIELD;
        });
        stoneIW.setOnMouseClicked(mouseEvent -> {
            resources.forEach(this::setBrightnessLow);
            setBrightnessHigh(stoneIW);
            selectedResource = Resource.STONE;
        });
        resources.forEach(this::setBrightnessLow);
    }

    /**
     * Method to initialize the slots
     */
    private void initializeSlots() {
        slots = Arrays.asList(slot0IW, slot1IW, slot2IW, slot3IW, slot4IW, slot5IW);
        slots.forEach(iw -> iw.setOnMouseClicked(event -> clickSlot(slots.indexOf(iw))));
    }

    /**
     * Method to handle the click on a slot
     * @param index the index of the selected slot
     */
    private void clickSlot(int index) {
        if (selectedResource != null) {
            selectedIndex = index;
            slots.stream()
                    .filter(iw -> !iw.isDisable())
                    .forEach(iw -> iw.setImage(null));
            slots.get(index).setImage(loadResourceImage(selectedResource.name().toLowerCase()));
        } else {
            gui.printErrorMessage("Select a resource first");
        }
    }

    /**
     * Method to load the resource image
     * @param resource the resource to be displayed
     * @return an image that represents the resource
     */
    private Image loadResourceImage(String resource) {
        return new Image(this.getClass().getResourceAsStream("/png/resources/" + resource + ".png"));
    }

    /**
     * Method to make an image darker
     * @param imageView the image to change
     */
    private void setBrightnessLow(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        imageView.setEffect(colorAdjust);
    }

    /**
     * Method to make an image lighter
     * @param imageView the image to change
     */
    private void setBrightnessHigh(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        imageView.setEffect(colorAdjust);
    }

    /**
     * Setter for the deposit
     */
    private void setDeposit() {
        ReducedDashboard rd = gui.getReducedModel().getReducedPlayer().getDashboard();
        Resource[] deposit = rd.getDeposit();
        slots.forEach(iw -> {
            int i = slots.indexOf(iw);
            if (deposit[i] != null) {
                iw.setImage(loadResourceImage(deposit[i].name()));
                iw.setDisable(true);
            }
        });
    }

    /**
     * Method to handle confirm click
     */
    public void handleConfirmClick(InputEvent event) {
        if (selectedResource == null || selectedIndex == null) {
            gui.printErrorMessage("Select a resource and a slot!");
        } else {
            gui.send(new GetInitialResourcesGameMessage(selectedResource, selectedIndex));
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

}
