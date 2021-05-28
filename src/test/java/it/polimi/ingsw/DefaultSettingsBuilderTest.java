package it.polimi.ingsw;

import org.junit.Test;

public class DefaultSettingsBuilderTest {

    @Test
    public void saveSettingsTest() {
        DefaultSettingsBuilder defaultSettingsBuilder = new DefaultSettingsBuilder();
        defaultSettingsBuilder.getGameSettings().saveSettings("defaultGameSettings.properties");
    }
}
