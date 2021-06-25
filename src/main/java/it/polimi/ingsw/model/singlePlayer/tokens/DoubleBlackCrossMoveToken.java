package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Game;

public class DoubleBlackCrossMoveToken implements Token {

    @Override
    public boolean useToken(Game game) {
        Dashboard dashboard = game.getPlayers()
                .get(game.getCurrentPlayer()).getDashboard();

        dashboard.getFaithTrack().moveLorenzoIlMagnificoMarker(2);

        return dashboard.checkGameEnd();

    }
}
