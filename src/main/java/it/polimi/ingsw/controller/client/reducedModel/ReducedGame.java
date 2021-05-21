package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReducedGame {

    private String gameId;

    private final int numberOfPlayers;

    private String clientPlayer;

    private String currentPlayer;
    private String firstPlayer;

    private Map<String, ReducedGame> players;

    private TurnPhase turnPhase;

    public ReducedGame(String gameId, int numberOfPlayers, String clientPlayer) {
        this.gameId = gameId;
        this.numberOfPlayers = numberOfPlayers;
        this.clientPlayer = clientPlayer;
        this.players = new HashMap<>();
    }

}
