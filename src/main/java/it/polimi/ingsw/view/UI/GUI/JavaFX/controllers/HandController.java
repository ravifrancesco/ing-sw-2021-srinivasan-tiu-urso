package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayLeaderCardGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerDiscardsLeaderCard;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HandController {

    public GUI gui;
    @FXML
    private HBox leaderCardContainerHBox;
    @FXML
    private Button closeButton;
    @FXML
    private Button discardButton;
    @FXML
    private Button playButton;
    private Integer selectedCard;
    private List<ImageView> imageViews;

    /**
     * Initialize method of the class
     */
    public void initialize() {
        imageViews = new ArrayList<>();
        enableButtons(false);
    }

    /**
     * Method to enable buttons
     * @param b true if enable and visible, false disable and hidden
     */
    private void enableButtons(boolean b) {
        playButton.setVisible(b);
        playButton.setDisable(!b);
        discardButton.setVisible(b);
        discardButton.setDisable(!b);
    }

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Setter for the hand
     * @param leaderCards the leader cards in the hand
     */
    public void setCards(List<LeaderCard> leaderCards) {
        leaderCards.forEach(c -> setLeaderCard(c));
    }

    /**
     * Method to set a leader card
     * @param leaderCard a leader card
     */
    private void setLeaderCard(LeaderCard leaderCard) {
        Image image = new Image(this.getClass().getResourceAsStream("/png/cards/leaderCards/leader_card_" + leaderCard.getId() + ".png"));
        ImageView imageView = new ImageView(image);
        leaderCardContainerHBox.getChildren().add(imageView);
        imageView.setFitHeight(200);
        imageView.setFitWidth(132);
        HBox.setMargin(imageView, new Insets(10, 10, 10, 10));
        imageView.setOnMouseClicked(event -> selectedCard(imageView));
        imageViews.add(imageView);
    }

    /**
     * Method to select a card
     * @param imageView the imageview of the card selected
     */
    private void selectedCard(ImageView imageView) {
        selectedCard = imageViews.indexOf(imageView);
        imageViews.forEach(this::setBrightnessHigh);
        setBrightnessLow(imageView);
        enableButtons(true);
    }

    /**
     * Method to make a card darker
     * @param imageView the imageview of the card
     */
    private void setBrightnessLow(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        imageView.setEffect(colorAdjust);
    }

    /**
     * Method to make a card lighter
     * @param imageView the imageview of the card
     */
    private void setBrightnessHigh(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        imageView.setEffect(colorAdjust);
    }

    /**
     * Method to handle on close event
     */
    @FXML
    private void onCloseClicked(InputEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Method to handle play button click
     */
    @FXML
    private void onPlayClicked(InputEvent event) {
        if (selectedCard == null) {
            this.gui.printErrorMessage("Select a card first!");
            return;
        }
        gui.send(new PlayLeaderCardGameMessage(selectedCard));
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Method to handle discard button click
     */
    @FXML
    private void onDiscardClicked(InputEvent event) {
        if (selectedCard == null) {
            this.gui.printErrorMessage("Select a card first!");
        }
        gui.send(new PlayerDiscardsLeaderCard(selectedCard));
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Method to update the cards
     * @param leaderCards the leader cards in the hand
     */
    public void update(List<LeaderCard> leaderCards) {
        leaderCardContainerHBox.getChildren().clear();
        setCards(leaderCards);
    }

}
