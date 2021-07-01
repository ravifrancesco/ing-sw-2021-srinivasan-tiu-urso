package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DevCardGridController {

    private final static int NUM_OF_ROWS = 3;
    private final static int NUM_OF_COLUMNS = 4;
    @FXML
    private GridPane cardsGridPane;
    @FXML
    private Button backButton;
    private ArrayList<ImageView> cardImageViews;
    private GUI gui;

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Initialize method of the class
     */
    public void initialize() {

        cardImageViews = new ArrayList<>();

        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                ImageView cardImageView = new ImageView();
                cardsGridPane.add(cardImageView, j, i);
                cardImageViews.add(cardImageView);
                int finalI = i;
                int finalJ = j;
                cardImageView.setOnMouseClicked(mouseEvent -> openBuyDevCardController(mouseEvent, cardImageView.getImage(), finalI, finalJ));
            }
        }
    }

    /**
     * Method to handle back button click
     */
    public void onBackPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method do load an image
     * @param imageView the imageview
     * @param developmentCard the development card to set
     */
    private void loadImage(ImageView imageView, DevelopmentCard developmentCard) {
        if (developmentCard == null) {
            imageView.setImage(null);
        } else {
            String id = String.valueOf(developmentCard.getId());
            Image image = new Image(this.getClass().getResourceAsStream("/png/cards/devCards/dev_card_" + id + ".png"));
            imageView.setImage(image);
            imageView.setFitWidth(130);
            imageView.setFitHeight(200);
        }
    }

    /**
     * Method to open BuyDevCardController
     * @param event
     * @param image the image of the card to buy
     * @param row the row of the card to buy
     * @param col the column of the card to buy
     */
    private void openBuyDevCardController(MouseEvent event, Image image, int row, int col) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/buy_dev_card.fxml"));
            Parent root = null;
            root = fxmlLoader.load();
            ((BuyDevCardController) fxmlLoader.getController()).setParameters(gui, image, row, col);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 422, 623));
            stage.setTitle("Buy development card");
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to update the grid
     * @param grid the grid to show
     */
    public void update(List<Stack<DevelopmentCard>> grid) {
        for (int i = 0; i < NUM_OF_ROWS * NUM_OF_COLUMNS; i++) {
            loadImage(cardImageViews.get(i), grid.get(i).isEmpty() ? null : grid.get(i).peek());
        }
    }

}
