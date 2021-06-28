package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import it.polimi.ingsw.model.reduced.ReducedDashboard;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.network.messages.clientMessages.game.BuyDevelopmentCardGameMessage;
import it.polimi.ingsw.utils.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class ChooseResourceController {

    private GUI gui;

    private static final int NUM_RESOURCES = 4;
    private static final int NUM_SHELVES = 6;
    private static final int EXTRA_DEPOSITS_SIZE = 4;

    private int[] maxLockerResources;

    private Slot[] depositSlots;

    private Slot[] extraDepositSlots;

    private boolean[] selectedDeposit;

    private boolean[] selectedExtraDeposit;

    private int selectedCardSlot;

    private ResourceContainer resourceContainer;

    private BuyDevCardController buyDevCardController;

    @FXML
    private Pane pane;

    @FXML private Label labelGold;
    @FXML private Label labelShield;
    @FXML private Label labelStone;
    @FXML private Label labelServant;
    @FXML private Button btnSlot1;
    @FXML private Button btnSlot2;
    @FXML private Button btnSlot3;
    @FXML private Button btnOk;
    @FXML private Button btnCanc;

    @FXML
    public void initialize() {
        depositSlots = new Slot[NUM_SHELVES];
        extraDepositSlots = new Slot[EXTRA_DEPOSITS_SIZE];
        selectedDeposit = new boolean[NUM_SHELVES];
        selectedExtraDeposit = new boolean[EXTRA_DEPOSITS_SIZE];
        maxLockerResources = new int[NUM_RESOURCES];
        selectedCardSlot = -1;

        depositSlots[0] = new Slot(230, 20, 40, 40);
        depositSlots[1] = new Slot(210, 60, 40, 40);
        depositSlots[2] = new Slot(250, 60, 40, 40);
        depositSlots[3] = new Slot(190, 100, 40, 40);
        depositSlots[4] = new Slot(230, 100, 40, 40);
        depositSlots[5] = new Slot(270, 100, 40, 40);
        extraDepositSlots[0] = new Slot(210, 390, 40, 40);
        extraDepositSlots[1] = new Slot(250, 390, 40, 40);
        extraDepositSlots[2] = new Slot(210, 450, 40, 40);
        extraDepositSlots[3] = new Slot(250, 450, 40, 40);

        for (Slot depositSlot : depositSlots) {
            depositSlot.setStroke(Color.RED);
            pane.getChildren().add(depositSlot.getRectangle());
        }

        for (Slot extraSlot : extraDepositSlots) {
            extraSlot.setStroke(Color.RED);
            pane.getChildren().add(extraSlot.getRectangle());
        }

        Arrays.fill(selectedDeposit, false);
        Arrays.fill(selectedExtraDeposit, false);
        Arrays.fill(maxLockerResources, 0);

        resourceContainer = new ResourceContainer();

    }

    @FXML
    private void handleAddGold(InputEvent event) {
        int number = Math.min(Integer.parseInt(labelGold.getText()) + 1, maxLockerResources[0]);
        labelGold.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubGold(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelGold.getText()) - 1, 0);
        labelGold.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddShield(InputEvent event) {
        int number = Math.min(Integer.parseInt(labelShield.getText()) + 1, maxLockerResources[1]);
        labelShield.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubShield(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelShield.getText()) - 1, 0);
        labelShield.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddStone(InputEvent event) {
        int number = Math.min(Integer.parseInt(labelStone.getText()) + 1, maxLockerResources[2]);
        labelStone.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubStone(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelStone.getText()) - 1, 0);
        labelStone.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddServant(InputEvent event) {
        int number = Math.min(Integer.parseInt(labelServant.getText()) + 1, maxLockerResources[3]);
        labelServant.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubServant(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelServant.getText()) - 1, 0);
        labelServant.setText(Integer.toString(number));
    }

    public void setBuyDevCardController(BuyDevCardController buyDevCardController) {
        this.buyDevCardController = buyDevCardController;
    }

    /* TODO could be cancelled, use setResources without parameters */
    public void setResources(Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposits) {
        for (int i = 0; i < deposit.length; i++) {
            if (deposit[i] != null) {
                ImageView imageView = createSelectableResourceDeposit(deposit[i], i);
                pane.getChildren().add(imageView);
            }
        }

        locker.forEach(this::updateLockerMaximum);

        for (int i = 0; i < EXTRA_DEPOSITS_SIZE / 2; i++) {
            for (int j = 0; j < EXTRA_DEPOSITS_SIZE / 2; j++) {
                if (extraDeposits[i][j] != null) {
                    ImageView imageView2 = createSelectableResourceExtraDeposit(extraDeposits[i][j], i*2+j);
                    pane.getChildren().add(imageView2);
                }
            }
        }
    }

    public void setResources() {
        ReducedDashboard reducedDashboard = gui.getReducedModel().getReducedPlayer().getDashboard();
        Resource[] deposit = reducedDashboard.getDeposit();
        Map<Resource, Integer> locker = reducedDashboard.getLocker();
        Resource[][] extraDeposits = reducedDashboard.getExtraDeposits();

        for (int i = 0; i < deposit.length; i++) {
            if (deposit[i] != null) {
                ImageView imageView = createSelectableResourceDeposit(deposit[i], i);
                pane.getChildren().add(imageView);
            }
        }

        locker.forEach(this::updateLockerMaximum);

        for (int i = 0; i < EXTRA_DEPOSITS_SIZE / 2; i++) {
            for (int j = 0; j < EXTRA_DEPOSITS_SIZE / 2; j++) {
                if (extraDeposits[i] != null && extraDeposits[i][j] != null) {
                    ImageView imageView2 = createSelectableResourceExtraDeposit(extraDeposits[i][j], i * 2 + j);
                    pane.getChildren().add(imageView2);
                }
            }
        }
    }

    private void updateLockerMaximum(Resource resource, int quantity) {
        switch (resource) {
            case GOLD -> maxLockerResources[0] = quantity;
            case SHIELD -> maxLockerResources[1] = quantity;
            case STONE -> maxLockerResources[2] = quantity;
            case SERVANT -> maxLockerResources[3] = quantity;
        }
    }

    private ImageView createSelectableResourceDeposit(Resource resource, int pos) {
        String resourceName = "";
        switch (resource) {
            case GOLD -> resourceName = "gold";
            case SHIELD -> resourceName = "shield";
            case STONE -> resourceName = "stone";
            case SERVANT -> resourceName = "servant";
        }
        File file = new File("src/main/resources/png/resources/"+resourceName+".png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked(mouseEvent -> clickedDepositResource(imageView, mouseEvent, pos));
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX((depositSlots[pos].getX() + depositSlots[pos].getWidth()/2) - (imageView.getFitWidth() / 2));
        imageView.setY((depositSlots[pos].getY() + depositSlots[pos].getHeight()/2) - (imageView.getFitHeight() / 2));
        return imageView;
    }

    private void clickedDepositResource(ImageView imageView, MouseEvent mouseEvent, int pos) {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (!selectedDeposit[pos]) {
            colorAdjust.setBrightness(-0.5);
            imageView.setEffect(colorAdjust);
        } else {
            colorAdjust.setBrightness(0);
            imageView.setEffect(colorAdjust);
        }
        selectedDeposit[pos] = !selectedDeposit[pos];
    }

    private ImageView createSelectableResourceExtraDeposit(Resource resource, int pos) {
        String resourceName = "";
        switch (resource) {
            case GOLD -> resourceName = "gold";
            case SHIELD -> resourceName = "shield";
            case STONE -> resourceName = "stone";
            case SERVANT -> resourceName = "servant";
        }
        File file = new File("src/main/resources/png/resources/"+resourceName+".png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked(mouseEvent -> clickedExtraDepositResource(imageView, mouseEvent, pos));
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX((extraDepositSlots[pos].getX() + extraDepositSlots[pos].getWidth()/2) - (imageView.getFitWidth() / 2));
        imageView.setY((extraDepositSlots[pos].getY() + extraDepositSlots[pos].getHeight()/2) - (imageView.getFitHeight() / 2));
        return imageView;
    }

    private void clickedExtraDepositResource(ImageView imageView, MouseEvent mouseEvent, int pos) {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (!selectedExtraDeposit[pos]) {
            colorAdjust.setBrightness(-0.5);
            imageView.setEffect(colorAdjust);
        } else {
            colorAdjust.setBrightness(0);
            imageView.setEffect(colorAdjust);
        }
        selectedExtraDeposit[pos] = !selectedExtraDeposit[pos];
    }

    @FXML
    private void clickedBtnSlot1(MouseEvent event) {
        if (btnOk.isDisabled()) {
            btnOk.setDisable(false);
        }
        btnSlot1.setDisable(true);
        btnSlot2.setDisable(false);
        btnSlot3.setDisable(false);
        selectedCardSlot = 0;
    }

    @FXML
    private void clickedBtnSlot2(MouseEvent event) {
        if (btnOk.isDisabled()) {
            btnOk.setDisable(false);
        }
        btnSlot1.setDisable(false);
        btnSlot2.setDisable(true);
        btnSlot3.setDisable(false);
        selectedCardSlot = 1;
    }

    @FXML
    private void clickedBtnSlot3(MouseEvent event) {
        if (btnOk.isDisabled()) {
            btnOk.setDisable(false);
        }
        btnSlot1.setDisable(false);
        btnSlot2.setDisable(false);
        btnSlot3.setDisable(true);
        selectedCardSlot = 2;
    }

    @FXML
    private void clickedOkBtn(MouseEvent event) {
        IntStream.range(0, selectedDeposit.length).filter(i -> selectedDeposit[i]).forEach(i -> resourceContainer.addDepositSelectedResource(i));
        IntStream.range(0, selectedExtraDeposit.length).filter(i -> selectedExtraDeposit[i]).forEach(i -> resourceContainer.addExtraDepositSelectedResource(i/2,i%2));
        if (!labelGold.getText().equals("0")) {
            resourceContainer.addLockerSelectedResource(Resource.GOLD, Integer.parseInt(labelGold.getText()));
        }
        if (!labelShield.getText().equals("0")) {
            resourceContainer.addLockerSelectedResource(Resource.SHIELD, Integer.parseInt(labelShield.getText()));
        }
        if (!labelStone.getText().equals("0")) {
            resourceContainer.addLockerSelectedResource(Resource.STONE, Integer.parseInt(labelStone.getText()));
        }
        if (!labelServant.getText().equals("0")) {
            resourceContainer.addLockerSelectedResource(Resource.SERVANT, Integer.parseInt(labelServant.getText()));
        }

        Pair<Integer, Integer> position = buyDevCardController.getPosition();

        gui.send(new BuyDevelopmentCardGameMessage(position.first+1, position.second+1, resourceContainer, selectedCardSlot));

        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
        buyDevCardController.closeWindow();
    }

    @FXML
    private void clickedCancBtn(MouseEvent event) {
        Stage stage = (Stage) btnCanc.getScene().getWindow();
        stage.close();
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
