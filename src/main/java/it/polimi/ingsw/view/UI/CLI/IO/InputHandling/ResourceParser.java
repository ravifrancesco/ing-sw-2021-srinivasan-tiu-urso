package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.model.full.table.Resource;

public class ResourceParser {
    /**
     * Parses a resource
     *
     * @param in the input string
     * @return the resource
     */
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
