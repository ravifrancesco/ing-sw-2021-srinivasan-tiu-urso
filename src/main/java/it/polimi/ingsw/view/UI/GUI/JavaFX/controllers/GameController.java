package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.reduced.*;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.ExtraDepositSlot;
import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.tokens.Token;
import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.network.messages.clientMessages.game.EndTurnGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerChangesDeposit;
import it.polimi.ingsw.network.messages.clientMessages.game.StartGameGameMessage;
import it.polimi.ingsw.utils.Pair;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class GameController {

    private GUI gui;

    @FXML private Pane pane;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    @FXML
    private Text pointsText;

    @FXML
    private ImageView lorenzoPicIW;
    @FXML
    private ImageView tokenIW;

    private Alert mainAlert;

    private Slot[] depositSlots;

    private ExtraDepositSlot[] extraDepositSlots;

    private Slot[] faithSlots;

    private Slot[] leaderCardSlots;

    private Slot[] developmentCardSlots;

    private ResourceController[] resourceControllers;

    private LeaderCardController[] leaderCardControllers;

    private DevelopmentCardController[] developmentCardControllers;

    private ResourceController[] supplyControllers;

    private FaithMarkerController faithMarkerController;

    private FaithMarkerController lorenzoIlMagnificoFaithMarkerController;

    private ChooseResourceController chooseResourceController;

    private MarketViewController marketController;

    private DevCardGridController devCardGridController;

    private HandController handController;

    @FXML private ComboBox<String> nicknameCombo;

    public static final int NUM_SHELFES = 6;

    public static final int NUM_FAITH_SLOTS = 25;

    public static final int NUM_LEADER_CARDS = 2;

    public static final int NUM_DEVELOPMENT_CARDS = 3;

    public static final int SIZE_EXTRA_DEPOSITS = 4;

    public static final int SIZE_SUPPLY = 4;

    @FXML private ImageView marketImage;

    @FXML private ImageView dvGridImage;

    @FXML private ImageView vaticanReport0IW;
    @FXML private ImageView vaticanReport1IW;
    @FXML private ImageView vaticanReport2IW;
    private List<ImageView> vaticanReportTokens;

    @FXML private Pane dashboardProductionPane;

    // Label for locker

    @FXML
    Label goldLabel;

    @FXML
    Label shieldLabel;

    @FXML
    Label stoneLabel;

    @FXML
    Label servantLabel;

    String currentDisplayedPlayer = "";

    public void setGui(GUI gui) {
        this.gui = gui;
        this.gui.getReducedModel().setGameController(this);
    }

    public void setPlayers() {
        Set<String> players = this.gui.getReducedModel().getReducedGame().getPlayers().keySet(); // TODO change with number of players
        if (players.size() == 1) { // TODO solve bug
            this.lorenzoPicIW.setVisible(true); // TODO show lorenzo
            this.tokenIW.setVisible(true);
            setSelectedPlayerSinglePlayer(gui.getReducedModel().getReducedPlayer().getNickname());
        } else {
            nicknameCombo.getItems().addAll(players);
            setSelectedPlayer(this.gui.getReducedModel().getReducedPlayer().getNickname());
            this.nicknameCombo.setVisible(true);
        }
    }

    public void setSelectedPlayer(String nickname) {
        nicknameCombo.getSelectionModel().select(nickname);
        currentDisplayedPlayer = nickname;
    }

    public void setSelectedPlayerSinglePlayer(String nickname) {
        currentDisplayedPlayer = nickname;
    }

    public void comboAction(ActionEvent event) {
        currentDisplayedPlayer = nicknameCombo.getSelectionModel().getSelectedItem();
        ReducedModel reducedModel = gui.getReducedModel();
        reducedModel.askWarehouseUpdate(currentDisplayedPlayer);
        reducedModel.askLeaderCardsUpdate(currentDisplayedPlayer);
        reducedModel.askDevelopmentCardsUpdate(currentDisplayedPlayer);
        reducedModel.askFaithMarkerPosition(currentDisplayedPlayer);
        reducedModel.askPointsUpdate(currentDisplayedPlayer);
        reducedModel.askVaticanReportsUpdate(currentDisplayedPlayer);
        hideWarehouseButtons();
    }

    @FXML
    public void initialize() {
        depositSlots = new Slot[NUM_SHELFES];
        extraDepositSlots = new ExtraDepositSlot[SIZE_EXTRA_DEPOSITS];
        resourceControllers = new ResourceController[NUM_SHELFES + SIZE_EXTRA_DEPOSITS];
        leaderCardSlots = new Slot[NUM_LEADER_CARDS];
        leaderCardControllers = new LeaderCardController[NUM_LEADER_CARDS];
        developmentCardSlots = new Slot[NUM_DEVELOPMENT_CARDS];
        developmentCardControllers = new DevelopmentCardController[NUM_DEVELOPMENT_CARDS];
        supplyControllers = new ResourceController[SIZE_SUPPLY];

        depositSlots[0] = new Slot(327, 391, 52, 58);
        depositSlots[1] = new Slot(308, 449, 42, 58);
        depositSlots[2] = new Slot(350, 449, 42, 58);
        depositSlots[3] = new Slot(289, 507, 42, 52);
        depositSlots[4] = new Slot(331, 507, 38, 52);
        depositSlots[5] = new Slot(369, 507, 42, 52);

        for (Slot slot : depositSlots) {
            pane.getChildren().add(slot.getRectangle());
        }

        extraDepositSlots[0] = new ExtraDepositSlot(55, 380, 40, 40);
        extraDepositSlots[1] = new ExtraDepositSlot(132, 380, 40, 40);
        extraDepositSlots[2] = new ExtraDepositSlot(55, 710, 40, 40);
        extraDepositSlots[3] = new ExtraDepositSlot(132, 710, 40, 40);

        leaderCardSlots[0] = new Slot(10, 125, 205, 310);
        leaderCardSlots[1] = new Slot(10, 450, 205, 310);

        for (Slot slot : leaderCardSlots) {
            slot.setStroke(Color.RED);
            pane.getChildren().add(slot.getRectangle());
        }

        developmentCardSlots[0] = new Slot(580, 430, 155, 235);
        developmentCardSlots[1] = new Slot(755, 430, 155, 235);
        developmentCardSlots[2] = new Slot(930, 430, 155, 235);

        for (Slot slot : developmentCardSlots) {
            slot.setStroke(Color.RED);
            pane.getChildren().add(slot.getRectangle());
        }

        initializeFaithTrack();

        this.lorenzoPicIW.setVisible(false);
        this.tokenIW.setVisible(false);
        nicknameCombo.setVisible(false);
    }

    public void printResource(Resource resource, int pos) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resource_item.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resourceControllers[pos] = loader.getController();
        resourceControllers[pos].setGui(gui);
        resourceControllers[pos].assignSlots(depositSlots, extraDepositSlots);
        resourceControllers[pos].createItem(resource, pos);

        pane.getChildren().add(resourceControllers[pos].getItem());
    }

    public void printSupplyResource(Resource resource, int pos) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resource_item.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        supplyControllers[pos] = loader.getController();
        supplyControllers[pos].setGui(gui);
        supplyControllers[pos].assignSlots(depositSlots, extraDepositSlots);
        supplyControllers[pos].createSupplyItem(resource, pos);

        pane.getChildren().add(supplyControllers[pos].getItem());
    }

    public void printPlayedLeaderCards(String player, List<LeaderCard> playedLeaderCards) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }

        cleanLeaderCards();

        IntStream.range(0, playedLeaderCards.size())
                .forEach(i -> printLeaderCard(playedLeaderCards.get(i), i));
    }

    public void printLeaderCard(LeaderCard leaderCard, int slot) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/leader_card.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        leaderCardControllers[slot] = loader.getController();
        leaderCardControllers[slot].setGui(this.gui);
        leaderCardControllers[slot].assignSlots(leaderCardSlots);
        leaderCardControllers[slot].createItem(leaderCard, slot);

        if (leaderCard.getSpecialAbility().getType() == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
            WarehouseExtraSpace wes = (WarehouseExtraSpace) leaderCard.getSpecialAbility();
            extraDepositSlots[slot*2].setStroke(Color.RED);
            extraDepositSlots[slot*2].setAvailable(true);
            extraDepositSlots[slot*2].setSlotType(wes.getStoredResource());
            extraDepositSlots[slot*2+1].setStroke(Color.RED);
            extraDepositSlots[slot*2+1].setAvailable(true);
            extraDepositSlots[slot*2+1].setSlotType(wes.getStoredResource());
        } else if (leaderCard.getSpecialAbility().getType() == SpecialAbilityType.PRODUCTION_POWER) {
            if (!((ProductionPower)leaderCard.getSpecialAbility()).isActivatable()) {
                leaderCardControllers[slot].setDarker();
            } else {
                leaderCardControllers[slot].getItem().setOnMouseClicked(mouseEvent -> leaderCardControllers[slot].openProductionPowerView(mouseEvent));
            }
        }

        pane.getChildren().add(leaderCardControllers[slot].getItem());

        if (leaderCard.getSpecialAbility().getType() == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
            pane.getChildren().add(extraDepositSlots[slot*2].getRectangle());
            pane.getChildren().add(extraDepositSlots[slot*2+1].getRectangle());
        }

        reloadWarehouseImages();
    }

    public void cleanLeaderCards() {
        for (int i = 0; i < leaderCardControllers.length; i++) {
            if (leaderCardControllers[i] != null) {
                pane.getChildren().remove(leaderCardControllers[i].getItem());
                if (leaderCardControllers[i].getLeaderCard().getSpecialAbility().getType() == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
                    pane.getChildren().remove(extraDepositSlots[i*2].getRectangle());
                    pane.getChildren().remove(extraDepositSlots[i*2+1].getRectangle());
                }
                leaderCardControllers[i] = null;
            }
        }
    }

    public void printPlayedDevelopmentCards(String player, List<Stack<DevelopmentCard>> playedDevelopmentCards) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }
        cleanDevelopmentCards();
        for (int i = 0; i < NUM_DEVELOPMENT_CARDS; i++) {
            if (playedDevelopmentCards != null && !playedDevelopmentCards.get(i).empty()) {
                printDevelopmentCard(playedDevelopmentCards.get(i).peek(), i);
            }
        }
    }

    public void printDevelopmentCard(DevelopmentCard developmentCard, int pos) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/development_card.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        developmentCardControllers[pos] = loader.getController();
        developmentCardControllers[pos].setGui(this.gui);
        developmentCardControllers[pos].assignSlots(developmentCardSlots);
        developmentCardControllers[pos].createItem(developmentCard, pos);

        if (!developmentCard.getProductionPower().isActivatable()) {
            developmentCardControllers[pos].setDarker();
        } else {
            developmentCardControllers[pos].getItem().setOnMouseClicked(mouseEvent -> developmentCardControllers[pos].openProductionPowerView(mouseEvent));
        }

        pane.getChildren().add(developmentCardControllers[pos].getItem());
    }

    public void cleanDevelopmentCards() {
        for (int i = 0; i < developmentCardControllers.length; i++) {
            if (developmentCardControllers[i] != null) {
                pane.getChildren().remove(developmentCardControllers[i].getItem());
            }
        }
    }

    public void reloadWarehouseImages() {
        for (ResourceController resourceController : resourceControllers) {
            if (resourceController != null) {
                pane.getChildren().remove(resourceController.getItem());
                pane.getChildren().add(resourceController.getItem());
            }
        }
        for (ResourceController supplyController : supplyControllers) {
            if (supplyController != null) {
                pane.getChildren().remove(supplyController.getItem());
                pane.getChildren().add(supplyController.getItem());
            }
        }
    }

    public void updateLockerLabel(Resource resource, int quantity) {
        String stringQuantity = "x " + quantity;
        switch (resource) {
            case GOLD -> goldLabel.setText(stringQuantity);
            case SHIELD -> shieldLabel.setText(stringQuantity);
            case STONE -> stoneLabel.setText(stringQuantity);
            case SERVANT -> servantLabel.setText(stringQuantity);
        }
    }

    public void printWarehouse(String player, Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposit, ArrayList<Resource> supply) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }
        cleanWarehouse();
        IntStream.range(0, deposit.length)
                .filter(i -> deposit[i] != null)
                .forEach(i -> printResource(deposit[i], i));

        locker.forEach(this::updateLockerLabel);

        for (int i = 0; i < SIZE_EXTRA_DEPOSITS / 2; i++) {
            for (int j = 0; j < SIZE_EXTRA_DEPOSITS / 2; j++) {
                if (extraDeposit[i] != null && extraDeposit[i][j] != null) {
                    printResource(extraDeposit[i][j], NUM_SHELFES + i*2 + j);
                }
            }
        }

        IntStream.range(0, supply.size())
                .filter(i -> supply.get(i) != null)
                .forEach(i -> printSupplyResource(supply.get(i), i));

        ReducedGame rg = gui.getReducedModel().getReducedGame();
        if (currentDisplayedPlayer.equals(rg.getClientPlayer()) && currentDisplayedPlayer.equals(rg.getCurrentPlayer())) {
            setEnable();
        } else {
            setDisable();
        }
    }

    public void cleanWarehouse() {

        for (int i = 0; i < resourceControllers.length; i++) {
            if (resourceControllers[i] != null) {
                pane.getChildren().remove(resourceControllers[i].getItem());
                if (i < NUM_SHELFES) {
                    depositSlots[i].freeSlot();
                } else {
                    extraDepositSlots[i - NUM_SHELFES].freeSlot();
                }
                resourceControllers[i] = null;
            }
        }

        updateLockerLabel(Resource.GOLD, 0);
        updateLockerLabel(Resource.SHIELD, 0);
        updateLockerLabel(Resource.STONE, 0);
        updateLockerLabel(Resource.SERVANT, 0);

        for (int i = 0; i < supplyControllers.length; i++) {
            if (supplyControllers[i] != null) {
                pane.getChildren().remove(supplyControllers[i].getItem());
                supplyControllers[i] = null;
            }
        }
    }

    public void changeSupplyController(int supplyPos, int pos) {
        resourceControllers[pos] = supplyControllers[supplyPos];
        supplyControllers[supplyPos] = null;
    }

    public void changeResourceController(int from, int to) {
        resourceControllers[to] = resourceControllers[from];
        resourceControllers[from] = null;
    }

    @FXML
    private void testClick(MouseEvent event) {
    }


    public void initializeFaithTrack() {
        faithSlots = new Slot[NUM_FAITH_SLOTS];
        faithSlots[0] = new Slot(267, 251, 40, 40);
        faithSlots[1] = new Slot(311, 251, 40, 40);
        faithSlots[2] = new Slot(355, 251, 40, 40);
        faithSlots[3] = new Slot(355, 207, 40, 40);
        faithSlots[4] = new Slot(355, 163, 40, 40);
        faithSlots[5] = new Slot(399, 163, 40, 40);
        faithSlots[6] = new Slot(443, 163, 40, 40);
        faithSlots[7] = new Slot(487, 163, 40, 40);
        faithSlots[8] = new Slot(531, 163, 40, 40);
        faithSlots[9] = new Slot(575, 163, 40, 40);
        faithSlots[10] = new Slot(575, 207, 40, 40);
        faithSlots[11] = new Slot(575, 251, 40, 40);
        faithSlots[12] = new Slot(619, 251, 40, 40);
        faithSlots[13] = new Slot(663, 251, 40, 40);
        faithSlots[14] = new Slot(707, 251, 40, 40);
        faithSlots[15] = new Slot(751, 251, 40, 40);
        faithSlots[16] = new Slot(795, 251, 40, 40);
        faithSlots[17] = new Slot(795, 207, 40, 40);
        faithSlots[18] = new Slot(795, 163, 40, 40);
        faithSlots[19] = new Slot(839, 163, 40, 40);
        faithSlots[20] = new Slot(883, 163, 40, 40);
        faithSlots[21] = new Slot(927, 163, 40, 40);
        faithSlots[22] = new Slot(971, 163, 40, 40);
        faithSlots[23] = new Slot(1015, 163, 40, 40);
        faithSlots[24] = new Slot(1059, 163, 40, 40);

        for (int i = 0; i < NUM_FAITH_SLOTS; i++) {
            pane.getChildren().add(faithSlots[i].getRectangle());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cross_item.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        faithMarkerController = loader.getController();
        faithMarkerController.assignSlots(faithSlots);
        faithMarkerController.createItem();

        pane.getChildren().add(faithMarkerController.getItem());

        lorenzoIlMagnificoFaithMarkerController = new FaithMarkerController();
        lorenzoIlMagnificoFaithMarkerController.assignSlots(faithSlots);
        lorenzoIlMagnificoFaithMarkerController.createLorenzoItem();
        lorenzoIlMagnificoFaithMarkerController.hide();

        pane.getChildren().add(lorenzoIlMagnificoFaithMarkerController.getItem());

        vaticanReportTokens = Arrays.asList(vaticanReport0IW, vaticanReport1IW, vaticanReport2IW);
    }

    public void openMarketWindow(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/market_view.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        marketController = fxmlLoader.getController();
        marketController.setGui(this.gui);
        gui.getReducedModel().askMarketUpdate();
        Stage stage = new Stage();
        stage.setTitle("Market");
        stage.setScene(new Scene(root, 465, 700));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }

    public void openDevCardGridWindow(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dev_card_grid.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        devCardGridController = fxmlLoader.getController();
        devCardGridController.setGui(this.gui);
        gui.getReducedModel().askDevCardGridUpdate();
        Stage stage = new Stage();
        stage.setTitle("Development card grid");
        stage.setScene(new Scene(root, 700, 800));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }

    public void initHostAlert() {
        mainAlert = new Alert(Alert.AlertType.CONFIRMATION);
        mainAlert.setTitle("Start Game");
        mainAlert.setHeaderText("Start game when you are ready!");

        ButtonType buttonTypeOne = new ButtonType("Start");

        mainAlert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = mainAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            this.gui.send(new StartGameGameMessage());
            Platform.runLater(this::initHostAlert); // TODO make it better
        }
    }

    public void btnConfirmClick(MouseEvent event) {
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
        Resource[] deposit = new Resource[NUM_SHELFES];
        for (int i = 0; i < NUM_SHELFES; i++) {
            if (resourceControllers[i] != null) {
                deposit[i] = resourceControllers[i].getResourceType();
            }
        }
        gui.setEnableNoDeposit();
        gui.setEnableImageViews();
        gui.send(new PlayerChangesDeposit(deposit));
    }

    public void btnCancelClick(MouseEvent event) {
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
        gui.setEnableNoDeposit();
        gui.setEnableImageViews();
        gui.getReducedModel().askWarehouseUpdate(currentDisplayedPlayer);
    }

    public void initPlayerAlert() {
        mainAlert = new Alert(Alert.AlertType.NONE);
        mainAlert.setTitle("Start Game");
        mainAlert.setHeaderText("Waiting for host to start the game");
        mainAlert.setResult(ButtonType.CANCEL);
        mainAlert.show();
    }

    public void hideAlert() {
        if (mainAlert != null) {
            if (mainAlert.getAlertType().equals(Alert.AlertType.INFORMATION)) {
                for ( ButtonType bt : mainAlert.getDialogPane().getButtonTypes() ) {
                    if ( bt.getButtonData() == ButtonBar.ButtonData.OK_DONE ) {
                        Button cancelButton = ( Button ) mainAlert.getDialogPane().lookupButton( bt );
                        Platform.runLater(cancelButton::fire);
                    }
                }
            } else {
                Platform.runLater(() -> mainAlert.close());
            }
        }
    }

    public Resource[] getDepositView() {
        Resource[] deposit = new Resource[NUM_SHELFES];
        for (int i = 0; i < NUM_SHELFES; i++) {
            if (resourceControllers[i] != null) {
                deposit[i] = resourceControllers[i].getResourceType();
            }
        }
        return deposit;
    }

    public Resource[] getExtraDepositView(int lcIndex) {
        Resource[] extraDeposit = new Resource[SIZE_EXTRA_DEPOSITS/2];
        int newPos = NUM_SHELFES + lcIndex * 2;
        extraDeposit[0] = resourceControllers[newPos] != null ? resourceControllers[newPos].getResourceType() : null;
        extraDeposit[1] = resourceControllers[newPos+1] != null ? resourceControllers[newPos+1].getResourceType() : null;
        return extraDeposit;
    }

    public void setDisableNoDeposit() {
        IntStream.range(NUM_SHELFES, resourceControllers.length)
                .filter(i -> resourceControllers[i] != null)
                .forEach(i -> resourceControllers[i].setDisable());

        IntStream.range(0, SIZE_SUPPLY)
                .filter(i -> supplyControllers[i] != null)
                .forEach(i -> supplyControllers[i].setDisable());
    }

    public void setDisableDeposit() {
        IntStream.range(0, NUM_SHELFES)
                .filter(i -> resourceControllers[i] != null)
                .forEach(i -> resourceControllers[i].setDisable());
    }

    public void setDisableImageViews() {
        marketImage.setDisable(true);
        dvGridImage.setDisable(true);
    }

    public void setDisable() {
        setDisableNoDeposit();
        setDisableDeposit();

        // TODO disable productions
    }

    public void setEnableNoDeposit() {
        IntStream.range(NUM_SHELFES, resourceControllers.length)
                .filter(i -> resourceControllers[i] != null)
                .forEach(i -> resourceControllers[i].setEnable());

        IntStream.range(0, SIZE_SUPPLY)
                .filter(i -> supplyControllers[i] != null)
                .forEach(i -> supplyControllers[i].setEnable());
    }

    public void setEnableDeposit() {
        IntStream.range(0, NUM_SHELFES)
                .filter(i -> resourceControllers[i] != null)
                .forEach(i -> resourceControllers[i].setEnable());
    }

    public void setEnableImageViews() {
        marketImage.setDisable(false);
        dvGridImage.setDisable(false);
    }

    public void setEnable() {
        setEnableNoDeposit();
        setEnableDeposit();

        // TODO enable productions
    }

    public void showWarehouseButtons() {
        btnConfirm.setVisible(true);
        btnCancel.setVisible(true);
    }

    public void hideWarehouseButtons() {
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
    }

    public void moveFaithMarker(String player, int position) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }
        faithMarkerController.moveFaithMarker(position);
    }

    public void moveLorenzoFaithMarker(int position) {
        lorenzoIlMagnificoFaithMarkerController.moveFaithMarker(position);
        if (gui.getReducedModel().getReducedGame().getPlayers().size() == 1) { // TODO change with number of players
            lorenzoIlMagnificoFaithMarkerController.show();
        }
    }

    public void showAfterGameStart() {
        hideAlert();
        setPlayers();
        mainAlert = new Alert(Alert.AlertType.INFORMATION);
        mainAlert.setTitle("Game is starting");
        mainAlert.showAndWait();
        showDiscardExcessLeaderCardMenu();
    }

    public void showAfterEndTurn() {
        hideAlert();
        mainAlert = new Alert(Alert.AlertType.INFORMATION);
        mainAlert.setTitle("New turn");
        ReducedGame rg = gui.getReducedModel().getReducedGame();
        if(rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            mainAlert.setHeaderText("It's your turn!");
            setSelectedPlayer(rg.getClientPlayer());
            setEnable(); // TODO understand y not working
        } else {
            mainAlert.setHeaderText("It is now " + rg.getCurrentPlayer()  + "'s turn!");
            setDisable();
        }
        mainAlert.showAndWait();
        if(rg.getTurnPhase() == TurnPhase.FIRST_TURN) {
            showDiscardExcessLeaderCardMenu();
        }
    }

    public void gameHasEnded() {
        hideAlert();
        mainAlert = new Alert(Alert.AlertType.INFORMATION);
        mainAlert.setTitle("Game has ended");
        Optional<ReducedPlayer> winner = gui.getReducedModel().getReducedGame().getPlayers().values()
                .stream()
                .max(Comparator.comparingInt(p -> p.getDashboard().getPlayerPoints())
                );
        mainAlert.setHeaderText(winner.get().getNickname() + " has one with " + winner.get().getDashboard().getPlayerPoints() + " victory points!");
        mainAlert.showAndWait();;
        gui.resetGUI();
    }

    public void gameHasEndedSinglePlayer() {
        hideAlert();
        mainAlert = new Alert(Alert.AlertType.INFORMATION);
        mainAlert.setTitle("Game has ended");
        int winner = establishWinner();
        if(winner == 0) {
            mainAlert.setHeaderText("Lorenzo has won!");
        } else {
            mainAlert.setHeaderText("You've won!");
        }
        mainAlert.showAndWait();;
        gui.resetGUIOffline();
    }

    private int establishWinner() {
        ReducedDashboard reducedDashboard = gui.getReducedModel().getReducedPlayer().getDashboard();
        ReducedGameBoard reducedGameBoard = gui.getReducedModel().getReducedGameBoard();
        if(checkFTEnd(reducedDashboard) || checkDvGridEnd(reducedGameBoard)) {
            return 0;
        } else {
            return 1;
        }
    }

    private boolean checkFTEnd(ReducedDashboard reducedDashboard) {
        return reducedDashboard.getLorenzoIlMagnificoPosition() == GameSettings.FAITH_TRACK_LENGTH-1;
    }

    private boolean checkDvGridEnd(ReducedGameBoard reducedGameBoard) {
        for(int i = 8; i < 12; i++) {
            if(reducedGameBoard.getGrid().get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }


    public void showDiscardExcessLeaderCardMenu() {
        ReducedGame rg = gui.getReducedModel().getReducedGame();
        if(!rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            return; // ADD ALERT BOX
        } else if (rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2 > 0) {
            hideAlert();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/discard_excess_leader_card.fxml"));
                Parent root;
                root = fxmlLoader.load();
                DiscardLeaderCardController controller = fxmlLoader.getController();
                controller.setGui(gui);
                controller.setCards(rg.getPlayers().get(rg.getClientPlayer()).getHand());
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 760, 462));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getInitialResources();
        }
    }

    public void getInitialResources() {
        ReducedGame reducedGame = gui.getReducedModel().getReducedGame();
        int owed = getOwed(reducedGame.getFirstTurns());
        if(owed - Arrays.stream(reducedGame.getPlayers()
                .get(reducedGame.getClientPlayer()).getDashboard().getDeposit()).filter(Objects::nonNull).count() == 0) {
            gui.send(new EndTurnGameMessage());
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_bonus_resources.fxml"));
                Parent root;
                root = fxmlLoader.load();
                ChooseBonusResourcesController controller = fxmlLoader.getController();
                controller.setGui(gui);
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 524, 471));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getOwed(int firstTurns) {
        return switch (firstTurns) {
            case 1, 2 -> 1;
            case 3 -> 2;
            default -> 0;
        };
    }

    public void updateMarket(Marble[] marblesGrid, Marble freeMarble) {
        if (marketController != null) {
            marketController.update(marblesGrid, freeMarble);
        }
    }

    public void updateDevCardGrid(List<Stack<DevelopmentCard>> grid) {
        if (devCardGridController != null) {
            devCardGridController.update(grid);
        }
    }

    public void onEndTurnPressed() {
        gui.send(new EndTurnGameMessage());
    }

    public void setPointsText(String player, String text) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }
        pointsText.setText(text);
    }

    public void onHandPressed(InputEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hand.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handController = fxmlLoader.getController();
        handController.setGui(this.gui);
        ReducedGame rg = gui.getReducedModel().getReducedGame();
        gui.getReducedModel().askHandUpdate(rg.getClientPlayer());
        Stage stage = new Stage();
        stage.setTitle("Hand");
        stage.setScene(new Scene(root, 508, 510));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }

    public void updateHand(String player, List<LeaderCard> leaderCards) {
        ReducedGame rg = gui.getReducedModel().getReducedGame();
        if (handController != null && player.equals(rg.getClientPlayer())) {
            handController.update(leaderCards);
        }
    }

    public void updateVaticanReports(String player, List<Pair<Integer, Integer>> vaticanReports) {
        if (!player.equals(currentDisplayedPlayer)) {
            return;
        }
        vaticanReports.forEach(p -> loadVaticanReportImage(vaticanReports.indexOf(p), p.second));
    }

    private void loadVaticanReportImage(int i, int state) {
        ImageView vaticanReportIW = vaticanReportTokens.get(i);
        vaticanReportIW.setImage(null);
        Image image;
        switch (state) {
            case 1 -> {
                File file = new File("src/main/resources/png/vaticanReport/pope_favor_" + i + "_missed.png");
                image = new Image(file.toURI().toString());
            }
            case 2 -> {
                File file = new File("src/main/resources/png/vaticanReport/pope_favor_" + i + "_achieved.png");
                image = new Image(file.toURI().toString());
            }
            default -> image = null;

        }
        vaticanReportIW.setImage(image);
        vaticanReportIW.setFitHeight(63);
        vaticanReportIW.setFitWidth(66);
    }

    @FXML private void dashboardProductionClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dashboard_production.fxml"));
            Parent root = fxmlLoader.load();
            DashboardProductionController dashboardProductionController = fxmlLoader.getController();
            dashboardProductionController.setGui(this.gui);
            dashboardProductionController.setResources();
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

    public void updateToken(Token token) {
        if (token == null) {
            tokenIW.setImage(null);
        } else {
            File file = new File("src/main/resources/png/singlePlayer/tokens/token_"+token.getType()+".png");
            Image image = new Image(file.toURI().toString());
            tokenIW.setImage(image);
        }
    }

    public Scene getScene() {
        return pane.getScene();
    }

}
