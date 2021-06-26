package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.GetInitialResourcesGameMessage;
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
    private ImageView coinIW;
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

    public void initialize() {
        initializeResources();
        initializeSlots();
    }

    public void setGui(GUI gui) {
        this.gui = gui;
        setDeposit();
    }

    private void initializeResources() {
        resources = Arrays.asList(coinIW, servantIW, shieldIW, stoneIW);
        coinIW.setOnMouseClicked(mouseEvent -> {
            resources.forEach(this::setBrightnessLow);
            setBrightnessHigh(coinIW);
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

    private void initializeSlots() {
        slots = Arrays.asList(slot0IW, slot1IW, slot2IW, slot3IW, slot4IW, slot5IW);
        slots.forEach(iw -> {
            iw.setStyle("-fx-background-color: A9A9A9");
            iw.setOnMouseClicked(event -> clickSlot(slots.indexOf(iw)));
        });
    }

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


    private Image loadResourceImage(String resource) {
        File file = new File("src/main/resources/png/"+resource+".png");
        return new Image(file.toURI().toString());
    }

    private void setBrightnessLow(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        imageView.setEffect(colorAdjust);
    }

    private void setBrightnessHigh(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        imageView.setEffect(colorAdjust);
    }

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

    public void handleConfirmClick(InputEvent event) {
        if (selectedResource == null || selectedIndex == null) {
            gui.printErrorMessage("Select a resource and a slot!");
        } else {
            gui.getClientConnection().send(new GetInitialResourcesGameMessage(selectedResource, selectedIndex));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }

}
