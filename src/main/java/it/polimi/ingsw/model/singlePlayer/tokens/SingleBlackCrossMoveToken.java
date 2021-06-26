package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Game;

public class SingleBlackCrossMoveToken implements Token {

    @Override
    public boolean useToken(Game game) {

        Dashboard dashboard = game.getPlayers()
                .get(game.getCurrentPlayer()).getDashboard();

        dashboard.getFaithTrack().moveLorenzoIlMagnificoMarker(2);

        game.resetTokens();

        return dashboard.checkGameEnd();

    }

}
