package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BuyDevCardController {

    @FXML
    ImageView cardImageView;

    @FXML
    HBox discountHbox;

    @FXML
    ImageView resourceOneIW;
    @FXML
    Label resourceOneLabel;

    @FXML
    ImageView resourceTwoIW;
    @FXML
    Label resourceTwoLabel;

    private GUI gui;

    private Pair<Integer, Integer> position;

    @FXML
    private Button cancelButton;

    /**
     * Initialize method of the class
     */
    public void initialize() {
        hideDiscounts();
    }

    /**
     * Method to hide discounts
     */
    private void hideDiscounts() {
        discountHbox.setVisible(false);
        resourceOneIW.setVisible(false);
        resourceOneLabel.setVisible(false);
        resourceTwoIW.setVisible(false);
        resourceTwoLabel.setVisible(false);
    }

    /**
     * Setter for the parameters
     * @param gui the GUI
     * @param image the image to be displayed
     * @param row the row of the card
     * @param col the col of the card
     */
    public void setParameters(GUI gui, Image image, int row, int col) {
        this.gui = gui;
        loadCardImage(image);
        this.position = new Pair<>(row, col);
        displayDiscounts();
    }

    /**
     * Method to handle the buy click
     */
    public void onBuyPressed(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_resource.fxml"));
            Parent root = fxmlLoader.load();
            ChooseResourceController chooseResourceController = fxmlLoader.getController();
            chooseResourceController.setGui(this.gui);
            chooseResourceController.setResources();
            chooseResourceController.setBuyDevCardController(this);
            Stage stage = new Stage();
            stage.setTitle("Select resources");
            stage.setScene(new Scene(root, 500, 700));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to close the window
     */
    public void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to load the card image
     * @param image the image to be loaded
     */
    private void loadCardImage(Image image) {
        cardImageView.setImage(image);
        cardImageView.setFitWidth(309);
        cardImageView.setFitHeight(466);
    }

    /**
     * Getter for the position
     * @return a pair that represents the position of the card
     */
    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    /**
     * Method to display the active discounts of the player
     */
    private void displayDiscounts() {
        List<LeaderCard> leaderCards = gui.getReducedModel().getReducedGame()
                .getReducedPlayer(gui.getReducedModel().getReducedPlayer().getNickname())
                .getDashboard().getPlayedLeaderCards().stream()
                .filter(l -> l.getSpecialAbility().getType().equals(SpecialAbilityType.DEVELOPMENT_CARD_DISCOUNT))
                .collect(Collectors.toList());
        if (leaderCards.size() == 1) {
            Resource r = ((DevelopmentCardDiscount) leaderCards.get(0).getSpecialAbility()).getResource();
            displayResource(resourceOneIW, resourceOneLabel, r);
        }
        if (leaderCards.size() == 2) {
            Resource r = ((DevelopmentCardDiscount) leaderCards.get(1).getSpecialAbility()).getResource();
            displayResource(resourceTwoIW, resourceTwoLabel, r);
        }
    }

    /**
     * Method to display the resource of a discount
     * @param iw the imageview
     * @param lb the label
     * @param r the displayed resource
     */
    private void displayResource(ImageView iw, Label lb, Resource r) {
        File file = new File("src/main/resources/png/resources/" + r.name().toLowerCase() + ".png");
        Image image = new Image(file.toURI().toString());
        iw.setImage(image);
        discountHbox.setVisible(true);
        iw.setVisible(true);
        lb.setVisible(true);
    }
}
