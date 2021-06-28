package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
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

    private GUI gui;

    @FXML
    GridPane cardsGridPane;

    @FXML
    Button backButton;

    ArrayList<ImageView> cardImageViews;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

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

    public void onBackPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void loadImage(ImageView imageView, DevelopmentCard developmentCard) {
        String id = String.valueOf(developmentCard.getId());
        File file = new File("src/main/resources/png/cards/devCards/dev_card_" + id + ".png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setFitWidth(130);
        imageView.setFitHeight(200);
    }

    private void openBuyDevCardController(MouseEvent event, Image image, int row, int col) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/buy_dev_card.fxml"));
            Parent root = null;
            root = fxmlLoader.load();
            ((BuyDevCardController) fxmlLoader.getController()).setParameters(gui, image, row, col);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 422, 623));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(List<Stack<DevelopmentCard>> grid) {
        for (int i = 0; i < NUM_OF_ROWS*NUM_OF_COLUMNS; i++) {
            loadImage(cardImageViews.get(i), grid.get(i).peek());
        }
    }

}
