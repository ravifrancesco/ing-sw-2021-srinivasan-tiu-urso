package it.polimi.ingsw;

import it.polimi.ingsw.client.IO.ClientMessageInputParser;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.model.Banner;
import it.polimi.ingsw.model.BannerEnum;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.EndTurnGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerChangesDeposit;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.StartGameGameMessage;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

public class GameController {

    private GUI gui;

    @FXML private Pane pane;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    private Alert mainAlert;

    private Slot[] depositSlots;

    private ExtraDepositSlot[] extraDepositSlots;

    private Slot[] faithSlots;

    private Slot[] leaderCardSlots;

    private ResourceController[] resourceControllers;

    private LeaderCardController[] leaderCardControllers;

    private ResourceController[] supplyControllers;

    private FaithMarkerController faithMarkerController;

    private ChooseResourceController chooseResourceController;

    private MarketViewController marketController;

    private DevCardGridController devCardGridController;

    @FXML private ComboBox<String> nicknameCombo;

    public static final int NUM_SHELFES = 6;

    public static final int NUM_FAITH_SLOTS = 25;

    public static final int NUM_LEADER_CARDS = 2;

    public static final int SIZE_EXTRA_DEPOSITS = 4;

    public static final int SIZE_SUPPLY = 4;

    private ArrayList<LeaderCard> playedLeaderCard; // test

    // Label for locker

    @FXML
    Label coinLabel;

    @FXML
    Label shieldLabel;

    @FXML
    Label stoneLabel;

    @FXML
    Label servantLabel;

    String currentDisplayedPlayer = ""; // TODO change for testing

    public void setGui(GUI gui) {
        this.gui = gui;
        this.gui.getReducedModel().setGameController(this);
    }

    public void setPlayers() {
        Set<String> players = this.gui.getReducedModel().getReducedGame().getPlayers().keySet();
        nicknameCombo.getItems().addAll(players);
        setSelectedPlayer(this.gui.getReducedModel().getReducedPlayer().getNickname());
    }

    public void setSelectedPlayer(String nickname) {
        nicknameCombo.getSelectionModel().select(nickname);
        currentDisplayedPlayer = nickname;
    }

    public void comboAction(ActionEvent event) {
        System.out.println(nicknameCombo.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void initialize() {
        depositSlots = new Slot[NUM_SHELFES];
        extraDepositSlots = new ExtraDepositSlot[SIZE_EXTRA_DEPOSITS];
        resourceControllers = new ResourceController[NUM_SHELFES + SIZE_EXTRA_DEPOSITS];
        leaderCardSlots = new Slot[NUM_LEADER_CARDS];
        leaderCardControllers = new LeaderCardController[NUM_LEADER_CARDS];
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

        initializeFaithTrack();

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

    public void printPlayedLeaderCards(List<LeaderCard> playedLeaderCards) {
        IntStream.range(0, playedLeaderCards.size())
                .filter(i -> leaderCardSlots[i].isEmpty())
                .forEach(System.out::println);

        IntStream.range(0, playedLeaderCards.size())
                .filter(i -> leaderCardSlots[i].isEmpty())
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
        leaderCardControllers[slot].assignSlots(leaderCardSlots);
        leaderCardControllers[slot].createItem(leaderCard, slot);

        pane.getChildren().add(leaderCardControllers[slot].getItem());

        if (leaderCard.getSpecialAbility().getType() == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
            WarehouseExtraSpace wes = (WarehouseExtraSpace) leaderCard.getSpecialAbility();
            extraDepositSlots[slot*2].setStroke(Color.RED);
            extraDepositSlots[slot*2].setAvailable(true);
            extraDepositSlots[slot*2].setSlotType(wes.getStoredResource());
            extraDepositSlots[slot*2+1].setStroke(Color.RED);
            extraDepositSlots[slot*2+1].setAvailable(true);
            extraDepositSlots[slot*2+1].setSlotType(wes.getStoredResource());
            pane.getChildren().add(extraDepositSlots[slot*2].getRectangle());
            pane.getChildren().add(extraDepositSlots[slot*2+1].getRectangle());
        }

        reloadWarehouseImages();
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
            case GOLD -> coinLabel.setText(stringQuantity);
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
        System.out.println(event.getX());
        System.out.println(event.getY());
        faithMarkerController.moveFaithMarker(faithMarkerController.getPosition()+1);
        if (chooseResourceController == null) {
            // DEBUG

            Map<Banner, Integer> bannerCost = new HashMap<>();

            bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
            bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

            Map<Resource, Integer> resourceCost = new HashMap<>();

            resourceCost.put(Resource.GOLD, 3);

            Map<Resource, Integer> resourceRequired = new HashMap<>();
            resourceRequired.put(Resource.ANY, 1);

            SpecialAbility sa = new WarehouseExtraSpace(Resource.SHIELD);

            LeaderCard leaderCard = new LeaderCard(7, 5, bannerCost, resourceCost, sa);

            playedLeaderCard = new ArrayList<>();

            playedLeaderCard.add(leaderCard);

            //printLeaderCard(leaderCard, 0);

            printPlayedLeaderCards(playedLeaderCard);

            // DEBUGGING

            Resource[] deposit = new Resource[6];
            deposit[0] = Resource.GOLD;
            deposit[2] = Resource.SHIELD;
            deposit[5] = Resource.SERVANT;

            Map<Resource, Integer> map = new HashMap<>();
            map.put(Resource.GOLD, 5);
            map.put(Resource.SERVANT, 3);

            ArrayList<Resource> supplyOld = new ArrayList<>();
            supplyOld.add(Resource.GOLD);

            printWarehouse("ravi", deposit, map, new Resource[2][2], supplyOld);

            cleanWarehouse();

            Resource[] deposit2 = new Resource[6];
            deposit2[0] = Resource.SHIELD;
            deposit2[2] = Resource.GOLD;
            deposit2[4] = Resource.STONE;

            Map<Resource, Integer> map2 = new HashMap<>();
            map2.put(Resource.SHIELD, 2);
            map2.put(Resource.STONE, 3);

            Resource[][] extraDeposits2 = new Resource[2][2];

            extraDeposits2[0][1] = Resource.SHIELD;

            ArrayList<Resource> supply = new ArrayList<>();

            supply.add(Resource.SHIELD);
            supply.add(Resource.STONE);
            supply.add(Resource.SERVANT);
            supply.add(Resource.GOLD);


            printWarehouse("ravi", deposit2, map2, extraDeposits2, supply);

            openChooseResourceWindow();

            Map<Banner, Integer> bannerCost2 = new HashMap<>();

            bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
            bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

            Map<Resource, Integer> resourceCost2 = new HashMap<>();

            resourceCost.put(Resource.GOLD, 3);

            SpecialAbility sa2 = new DevelopmentCardDiscount(Resource.SHIELD, 1);

            LeaderCard leaderCard2 = new LeaderCard(1, 5, bannerCost2, resourceCost2, sa2);

            playedLeaderCard.add(leaderCard2);

            //printLeaderCard(leaderCard2, 1);

            printPlayedLeaderCards(playedLeaderCard);
        }
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
    }

    public void openChooseResourceWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_resource.fxml"));
            Parent root = fxmlLoader.load();
            chooseResourceController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setTitle("Buy dv card");
            stage.setScene(new Scene(root, 500, 700));
            stage.show();

            Resource[] deposit = new Resource[6];
            deposit[0] = Resource.GOLD;
            deposit[2] = Resource.SHIELD;
            deposit[5] = Resource.SERVANT;

            Map<Resource, Integer> map = new HashMap<>();
            map.put(Resource.GOLD, 5);
            map.put(Resource.SERVANT, 3);

            Resource[][] extraDeposits = new Resource[2][2];

            extraDeposits[0][0] = Resource.GOLD;
            extraDeposits[0][1] = Resource.SHIELD;
            extraDeposits[1][0] = Resource.STONE;
            extraDeposits[1][1] = Resource.SERVANT;

            chooseResourceController.setResources(deposit, map, extraDeposits);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        Stage stage = new Stage();
        stage.setTitle("Market");
        stage.setScene(new Scene(root, 465, 600));
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
        devCardGridController.update(gui.getReducedModel().getReducedGameBoard().getGrid());
        Stage stage = new Stage();
        stage.setTitle("Development card grid");
        stage.setScene(new Scene(root, 700, 800));
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
            this.gui.getClientConnection().send(new StartGameGameMessage());
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
        gui.getClientConnection().send(new PlayerChangesDeposit(deposit));
    }

    public void btnCancelClick(MouseEvent event) {
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
        // TODO ravi
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

        // TODO disable productions and other buttons
    }

    public void setEnableNoDeposit() {
        IntStream.range(NUM_SHELFES, resourceControllers.length)
                .filter(i -> resourceControllers[i] != null)
                .forEach(i -> resourceControllers[i].setEnable());

        IntStream.range(0, SIZE_SUPPLY)
                .filter(i -> supplyControllers[i] != null)
                .forEach(i -> supplyControllers[i].setEnable());

        // TODO enable productions and other buttons
    }

    public void showWarehouseButtons() {
        btnConfirm.setVisible(true);
        btnCancel.setVisible(true);
    }

    public void moveFaithMarker(int position) {
        faithMarkerController.moveFaithMarker(position);
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
        /*
        if(rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            mainAlert.setHeaderText("It's your turn!");
        } else {
            mainAlert.setHeaderText("It is now " + rg.getCurrentPlayer()  + "'s turn!");
        }
         */
        mainAlert.showAndWait();
        if(rg.getTurnPhase() == TurnPhase.FIRST_TURN) {
            showDiscardExcessLeaderCardMenu();
        }
    }

    public void showDiscardExcessLeaderCardMenu() {
        ReducedGame rg = gui.getReducedModel().getReducedGame();
        if(!rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            return; // ADD ALERT BOX
        } else if (rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2 > 0) {
            hideAlert();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/discard_excess_leader_card.fxml"));
                Parent root = null;
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
            gui.getClientConnection().send(new EndTurnGameMessage());
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/choose_bonus_resources.fxml"));
                Parent root = null;
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

}
