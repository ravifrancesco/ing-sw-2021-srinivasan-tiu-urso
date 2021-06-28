package it.polimi.ingsw.model;

import it.polimi.ingsw.model.full.table.Game;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.utils.GameSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;


public class GameTest {

    @Test
    public void gameConstructorTest() {
        Game game = new Game("1", 3);
        Assert.assertNotNull(game.getGameId());
        Assert.assertNotNull(game.getPlayers());
        Assert.assertNotNull(game.getGameBoard());
    }

    @Test
    public void loadGameSettingsTest() {
        Game game = new Game("1", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        Assert.assertNull(game.getGameSettings());
        game.loadGameSettings(gameSettings);
        Assert.assertNotNull(game.getGameSettings());
    }

    @Test
    public void resetTest() {
        // TODO find a better way to test this?
        Game game = new Game("1", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        game.loadGameSettings(gameSettings);
        Assert.assertFalse(game.getGameEnded());
        game.endGame();
        Assert.assertTrue(game.getGameEnded());
        game.reset();
        Assert.assertFalse(game.getGameEnded());
    }

    @Test
    public void checkWinner() {
        Game game = new Game("1", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        game.loadGameSettings(gameSettings);
        game.getPlayers().put("rbta-svg", new Player(gameSettings, "rbta-svg"));
        game.getPlayers().put("ravifrancesco", new Player(gameSettings, "ravifrancesco"));
        game.getPlayers().put("giuseppeurso", new Player(gameSettings, "giuseppeurso"));
        game.getPlayers().get("rbta-svg").getDashboard().placeLeaderCard(gameSettings.getLeaderCards()[0]);
        game.getPlayers().get("rbta-svg").getDashboard().placeLeaderCard(gameSettings.getLeaderCards()[1]);
        game.getPlayers().get("ravifrancesco").getDashboard().placeLeaderCard(gameSettings.getLeaderCards()[3]);
        game.getPlayers().get("ravifrancesco").getDashboard().placeLeaderCard(gameSettings.getLeaderCards()[4]);
        game.getPlayers().get("giuseppeurso").getDashboard().placeLeaderCard(gameSettings.getLeaderCards()[5]);

        game.getGameStatus();

        Player winner = game.checkWinner();
        Assert.assertEquals(winner, game.getPlayers().get("rbta-svg"));
    }

    @Test
    public void addPlayerTest() {
        Game game = new Game("1", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        game.loadGameSettings(gameSettings);
        Assert.assertEquals(game.getPlayers().values().size(), 0);
        game.addPlayer("rbta-svg", new Player(gameSettings, "rbta-svg"));
        Assert.assertEquals(game.getPlayers().values().size(), 1);


    }

    @Test
    public void getNextPlayerTest() {
        Game game = new Game("1", 3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        game.loadGameSettings(gameSettings);
        game.addPlayer("rbta-svg", new Player(gameSettings, "rbta-svg"));
        game.addPlayer("ravifrancesco", new Player(gameSettings, "ravifrancesco"));
        game.addPlayer("giuseppeurso", new Player(gameSettings, "giuseppeurso"));

        Assert.assertEquals(game.getNextPlayer(), "rbta-svg");
        Assert.assertEquals(game.getNextPlayer(), "ravifrancesco");
        Assert.assertEquals(game.getNextPlayer(), "giuseppeurso");
        Assert.assertEquals(game.getNextPlayer(), "rbta-svg");
        Assert.assertEquals(game.getNextPlayer(), "ravifrancesco");
        Assert.assertEquals(game.getNextPlayer(), "giuseppeurso");


    }


    @Test
    public void getTurnPhaseTest() {
        Game game = new Game("1",3);
        GameSettingsBuilder gameSettingsBuilder = new GameSettingsBuilder();
        GameSettings gameSettings = gameSettingsBuilder.build();
        game.loadGameSettings(gameSettings);

        Assert.assertNull(game.getTurnPhase());
        game.startUniquePhase(TurnPhase.FIRST_TURN);
        Assert.assertEquals(game.getTurnPhase(), TurnPhase.FIRST_TURN);



    }


}