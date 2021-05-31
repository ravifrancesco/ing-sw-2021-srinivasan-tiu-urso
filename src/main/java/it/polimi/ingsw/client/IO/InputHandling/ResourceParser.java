package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.model.Resource;

public class ResourceParser {
    public static Resource parse(String in) {
        return switch (in.toUpperCase()) {
            case "STONE" -> Resource.STONE;
            case "SERVANT" -> Resource.SERVANT;
            case "SHIELD" -> Resource.SHIELD;
            case "GOLD" -> Resource.GOLD;
            default -> null;
        };
    }
}
