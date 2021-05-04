package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.BeforeClass;

public class PlayerTest {

    private static Player player;

    @BeforeClass
    static public void createPlayer() {

        GameSettings gameSettings = new GameSettingsTest().buildGameSettings();
        player = new Player(gameSettings);

        Assert.assertNotNull(player);

    }

    


}
