package it.polimi.ingsw;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.utils.Pair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.LabelView;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @FXML private Button cancelButton;

    public void initialize() {
        hideDiscounts();
    }

    private void hideDiscounts() {
        discountHbox.setVisible(false);
        resourceOneIW.setVisible(false);
        resourceOneLabel.setVisible(false);
        resourceTwoIW.setVisible(false);
        resourceTwoLabel.setVisible(false);
    }

    public void setParameters(GUI gui, Image image, int row, int col) {
        this.gui = gui;
        loadCardImage(image);
        this.position = new Pair<>(row, col);
        displayDiscounts();
    }

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
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void loadCardImage(Image image) {
        cardImageView.setImage(image);
        cardImageView.setFitWidth(309);
        cardImageView.setFitHeight(466);
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    private void displayDiscounts() {
        List<LeaderCard> leaderCards = gui.getReducedModel().getReducedGame()
                .getReducedPlayer(gui.getReducedModel().getReducedPlayer().getNickname())
                .getDashboard().getPlayedLeaderCards().stream()
                .filter(l-> l.getSpecialAbility().getType().equals(SpecialAbilityType.DEVELOPMENT_CARD_DISCOUNT))
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

    private void displayResource(ImageView iw, Label lb, Resource r) {
        File file = new File("src/main/resources/png/"+r.name().toLowerCase()+".png");
        Image image = new Image(file.toURI().toString());
        iw.setImage(image);
        discountHbox.setVisible(true);
        iw.setVisible(true);
        lb.setVisible(true);
    }
}
