package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.marbles.MarbleColor;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerGetsFromMarket;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarketViewController {

    private final int NUM_OF_ROWS = 3;
    private final int NUM_OF_COLUMNS = 4;
    @FXML
    private GridPane marketGridPane;
    private ImageView freeMarbleIW;
    private ArrayList<ImageView> marblesIW;
    @FXML
    private Button button0;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button backButton;
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

        freeMarbleIW = new ImageView();
        marblesIW = new ArrayList<>();
        marketGridPane.add(freeMarbleIW, 5, 0);

        for (int i = 1; i < 1 + NUM_OF_ROWS; i++) {
            for (int j = 1; j < 1 + NUM_OF_COLUMNS; j++) {
                ImageView marbleImage = new ImageView();
                marketGridPane.add(marbleImage, j, i);
                marblesIW.add(marbleImage);
            }
        }

    }

    /**
     * Update method
     * @param marblesGrid the marbles grid
     * @param freeMarble the free marble
     */
    public void update(Marble[] marblesGrid, Marble freeMarble) {
        loadImage(freeMarbleIW, freeMarble);
        for (int i = 0; i < marblesGrid.length; i++) {
            loadImage(marblesIW.get(i), marblesGrid[i]);
        }
    }

    /**
     * Method to load an image
     * @param imageView the imageview to create
     * @param marble the marble related to the imageview
     */
    private void loadImage(ImageView imageView, Marble marble) {
        imageView.setImage(null);
        String color = marble.getMarbleColor().toString().toLowerCase();
        Image image = new Image(this.getClass().getResourceAsStream("/png/marbles/" + color + "_marble.png"));
        imageView.setImage(image);
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);
    }

    /**
     * Method to handle back pressed event
     */
    public void onBackPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to handle the click on button 0
     */
    @FXML
    public void button0Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[0], marbleGrid[1], marbleGrid[2], marbleGrid[3]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(0, wmrs));
    }

    /**
     * Method to handle the click on button 1
     */
    @FXML
    public void button1Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[4], marbleGrid[5], marbleGrid[6], marbleGrid[7]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(1, wmrs));
    }

    /**
     * Method to handle the click on button 2
     */
    @FXML
    public void button2Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[8], marbleGrid[9], marbleGrid[10], marbleGrid[11]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(2, wmrs));
    }

    /**
     * Method to handle the click on button 3
     */
    @FXML
    public void button3Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[0], marbleGrid[4], marbleGrid[8]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(3, wmrs));
    }

    /**
     * Method to handle the click on button 4
     */
    @FXML
    public void button4Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[1], marbleGrid[5], marbleGrid[9]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(4, wmrs));
    }

    /**
     * Method to handle the click on button 5
     */
    @FXML
    public void button5Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[2], marbleGrid[6], marbleGrid[10]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(5, wmrs));
    }

    /**
     * Method to handle the click on button 6
     */
    @FXML
    public void button6Pressed() {
        Marble[] marbleGrid = gui.getReducedModel().getReducedGameBoard().getMarblesGrid();
        List<Marble> marbles = Arrays.asList(marbleGrid[3], marbleGrid[7], marbleGrid[11]);
        ArrayList<Resource> wmrs = askWMR(marbles);
        gui.send(new PlayerGetsFromMarket(6, wmrs));
    }

    public ArrayList<Resource> askWMR(List<Marble> marbles) {
        List<Marble> whiteMarbles = marbles.stream().filter(m -> m.getMarbleColor().equals(MarbleColor.WHITE)).collect(Collectors.toList());
        Resource[] wmrsArray = gui.getReducedModel().getReducedPlayer().getActivatedWMR();
        if (wmrsArray.length == 0) {
            return new ArrayList<>();
        } else if (wmrsArray.length == 1) {
            List<Resource> temp = IntStream.range(0, whiteMarbles.size()).boxed().map(i -> wmrsArray[0]).collect(Collectors.toList());
            return new ArrayList<>(temp);
        } else {
            ArrayList<Resource> selectedWMR = new ArrayList<>();
            for (Marble whiteMarble : whiteMarbles) {
                selectedWMR.add(askResource(wmrsArray));
            }
            return selectedWMR;
        }
    }

    public Resource askResource(Resource[] wmrsArray) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("White Marble");
        alert.setHeaderText("Choose a resource!");

        ButtonType buttonTypeOne = new ButtonType(wmrsArray[0].name());
        ButtonType buttonTypeTwo = new ButtonType(wmrsArray[0].name());

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return wmrsArray[0];
        } else if (result.get() == buttonTypeTwo) {
            return wmrsArray[1];
        }
        return null;
    }

}
