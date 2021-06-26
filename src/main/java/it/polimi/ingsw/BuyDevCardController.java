package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.utils.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BuyDevCardController {

    @FXML
    ImageView cardImageView;

    private GUI gui;

    private Pair<Integer, Integer> position;

    private static final int NUM_SHELVES = 6;
    private static final int EXTRA_DEPOSITS_SIZE = 4;

    private Slot[] depositSlots;

    private Slot[] extraDepositSlots;

    private boolean[] selectedDeposit;

    private boolean[] selectedExtraDeposit;

    @FXML
    private Pane pane;

    @FXML private Label labelGold;
    @FXML private Label labelShield;
    @FXML private Label labelStone;
    @FXML private Label labelServant;

    public void setParameters(GUI gui, Image image, int row, int col) {
        this.gui = gui;
        loadCardImage(image);
        this.position = new Pair<>(row, col);
    }

    public void onBuyPressed() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_resource.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Select resources");
            stage.setScene(new Scene(root, 500, 700));

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCancelPressed() {
        // TODO to close card
    }

    public void onConfirmPressed() {
        // TODO giuse your job
        // TODO gui.getClientConnection().send(new BuyDevelopmentCardGameMessage(/* TODO add parameted */));
    }

    public void onCancelResourcePressed() {
        // TODO giuse
    }

    private void loadCardImage(Image image) {
        cardImageView.setImage(image);
        cardImageView.setFitHeight(446);
        cardImageView.setFitWidth(309);
    }

    /*
    @FXML
    public void initializeChoiceResources() {
        depositSlots = new Slot[NUM_SHELVES];
        extraDepositSlots = new Slot[EXTRA_DEPOSITS_SIZE];
        selectedDeposit = new boolean[NUM_SHELVES];
        selectedExtraDeposit = new boolean[EXTRA_DEPOSITS_SIZE];

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

    }

    @FXML
    private void handleAddGold(InputEvent event) {
        int number = Integer.parseInt(labelGold.getText()) + 1;
        labelGold.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubGold(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelGold.getText()) - 1, 0);
        labelGold.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddShield(InputEvent event) {
        int number = Integer.parseInt(labelShield.getText()) + 1;
        labelShield.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubShield(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelShield.getText()) - 1, 0);
        labelShield.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddStone(InputEvent event) {
        int number = Integer.parseInt(labelStone.getText()) + 1;
        labelStone.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubStone(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelStone.getText()) - 1, 0);
        labelStone.setText(Integer.toString(number));
    }

    @FXML
    private void handleAddServant(InputEvent event) {
        int number = Integer.parseInt(labelServant.getText()) + 1;
        labelServant.setText(Integer.toString(number));
    }

    @FXML
    private void handleSubServant(InputEvent event) {
        int number = Math.max(Integer.parseInt(labelServant.getText()) - 1, 0);
        labelServant.setText(Integer.toString(number));
    }

    public void setResources(Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposits) {
        for (int i = 0; i < deposit.length; i++) {
            if (deposit[i] != null) {
                ImageView imageView = createSelectableResourceDeposit(deposit[i], i);
                pane.getChildren().add(imageView);
            }
        }

        locker.forEach(this::updateLockerLabel);

        for (int i = 0; i < EXTRA_DEPOSITS_SIZE/2; i++) {
            for (int j = 0; j < EXTRA_DEPOSITS_SIZE/2; j++) {
                if (extraDeposits[i][j] != null) {
                    ImageView imageView = createSelectableResourceExtraDeposit(extraDeposits[i][j], i*2+j);
                    pane.getChildren().add(imageView);
                }
            }
        }
    }

    private void updateLockerLabel(Resource resource, int quantity) {
        String q = Integer.toString(quantity);
        switch (resource) {
            case GOLD -> labelGold.setText(q);
            case SHIELD -> labelShield.setText(q);
            case STONE -> labelStone.setText(q);
            case SERVANT -> labelServant.setText(q);
        }
    }

    private ImageView createSelectableResourceDeposit(Resource resource, int pos) {
        String resourceName = "";
        switch (resource) {
            case GOLD -> resourceName = "coin";
            case SHIELD -> resourceName = "shield";
            case STONE -> resourceName = "stone";
            case SERVANT -> resourceName = "servant";
        }
        File file = new File("png/"+resourceName+".png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked(mouseEvent -> clickedDepositResource(imageView, mouseEvent, pos));
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX((depositSlots[pos].getX() + depositSlots[pos].getWidth()/2) - (imageView.getFitWidth() / 2));
        imageView.setX((depositSlots[pos].getY() + depositSlots[pos].getHeight()/2) - (imageView.getFitHeight() / 2));
        return imageView;
    }

    private void clickedDepositResource(ImageView imageView, MouseEvent mouseEvent, int pos) {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (selectedDeposit[pos]) {
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
            case GOLD -> resourceName = "coin";
            case SHIELD -> resourceName = "shield";
            case STONE -> resourceName = "stone";
            case SERVANT -> resourceName = "servant";
        }
        File file = new File("png/"+resourceName+".png");
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked(mouseEvent -> clickedExtraDepositResource(imageView, mouseEvent, pos));
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX((depositSlots[pos].getX() + depositSlots[pos].getWidth()/2) - (imageView.getFitWidth() / 2));
        imageView.setX((depositSlots[pos].getY() + depositSlots[pos].getHeight()/2) - (imageView.getFitHeight() / 2));
        return imageView;
    }

    private void clickedExtraDepositResource(ImageView imageView, MouseEvent mouseEvent, int pos) {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (selectedExtraDeposit[pos]) {
            colorAdjust.setBrightness(-0.5);
            imageView.setEffect(colorAdjust);
        } else {
            colorAdjust.setBrightness(0);
            imageView.setEffect(colorAdjust);
        }
        selectedExtraDeposit[pos] = !selectedExtraDeposit[pos];
    }
     */

}
