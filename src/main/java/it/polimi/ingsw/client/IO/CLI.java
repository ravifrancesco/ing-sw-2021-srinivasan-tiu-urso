package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CLI implements UI {

    private Scanner input;

    private ReducedModel reducedModel;

    public CLI() {
        this.input = new Scanner(System.in);
        this.reducedModel = new ReducedModel();
    }

    public String getIp() {
        System.out.println("Insert the server ip: ");
        System.out.print("> ");
        return input.nextLine();
    }

    public int getPort() {
        System.out.println("Insert the server port: ");
        System.out.print("> ");
        int port = input.nextInt();
        input.nextLine();
        return port;
    }

    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    public String readCommand() {
        System.out.println("Enter command:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void printErrorMessage(String error) {
        System.out.println(error);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String getNickname() {
        System.out.println("Insert nickname:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void showGameLobbies(ArrayList<GameLobbyDetails> activeGameLobbies) {
        if (activeGameLobbies.size() > 0) {
            activeGameLobbies.forEach(gameLobbyDetails ->
                    System.out.println(
                            "ID: " + gameLobbyDetails.id +
                                    "         CREATOR: " + gameLobbyDetails.creator +
                                    "         PLAYERS: " +
                                    gameLobbyDetails.connectedPlayers + "/" + gameLobbyDetails.maxPlayers));
        } else {
            printErrorMessage("No game lobbies found");
        }
    }

    public void showHand(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            System.out.println(nickname + "'s HAND: ");
            List<LeaderCard> leaderCardList = reducedPlayer.getHand();
            if (leaderCardList.size() != 0) {
                leaderCardList.forEach(card -> System.out.println(card.toString()));
            } else
            {
                System.out.println(reducedPlayer.getHandSize() + " cards");
            }
        } else {
            printErrorMessage("PLAYER " + nickname + "DOESN'T EXISTS");
        }
    }

    public void showDashboard(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            System.out.println(nickname + "'s DASHBOARD: ");
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            System.out.println(nickname + "'s victory points = " + reducedDashboard.getPlayerPoints());
            System.out.println(nickname + "'s played leader cards:");
            reducedDashboard.getPlayedLeaderCards().forEach(leaderCard -> System.out.println(leaderCard.toString()));
            System.out.print(nickname + "'s played development cards:");
            reducedDashboard.getPlayedDevelopmentCards().stream().map(stack -> stack.isEmpty() ? " | " : stack.peek().toString() + " | ").forEach(System.out::print);
            System.out.println();
            //TODO print supply
        } else {
            printErrorMessage("PLAYER " + nickname + "DOESN'T EXISTS");
        }
    }

    public void showFaithTrack(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            System.out.println(nickname + "'s FAITHTRACK: ");
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            int position = reducedDashboard.getPosition();
            IntStream.range(0, reducedDashboard.getFaithTrackVictoryPoints().length)
                    .forEach(pos -> System.out.print(pos==position ? " x " : " o "));
            System.out.println();
            // TODO print victory points and vatican reports
        } else {
            printErrorMessage("PLAYER " + nickname + "DOESN'T EXISTS");
        }
    }

    public void showWarehouse(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            System.out.println("DEPOSIT:");
            for (int i=0; i<reducedDashboard.getDeposit().length; i++) {
                System.out.print(reducedDashboard.getDeposit()[i] == null ? " EMPTY " : reducedDashboard.getDeposit()[i]);
                if (i == 0 || i == 2) {
                    System.out.println();
                }
            }
            System.out.println("\nLOCKER:");
            reducedDashboard.getLocker().entrySet().forEach(entry -> System.out.println("RESOURCE = " + entry.getKey() + " | QUANTITY = " + entry.getValue()));
            // TODO print extradeposit
        } else
        {
            printErrorMessage("PLAYER " + nickname + "DOESN'T EXISTS");
        }
    }

    public void showDVGrid() {
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        for (int i = 0; i < reducedGameBoard.getGrid().size(); i++) {
            System.out.print(reducedGameBoard.getGrid().get(i).isEmpty() ? " | " : reducedGameBoard.getGrid().get(i).peek().toString() + " | ");
            if ((i+1) % 4 == 0) {
                System.out.println();
            }
        }
    }

    public void showGameBoard() {
        // TODO
    }

    public void showMarket() {
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        for (int i = 0; i < reducedGameBoard.getMarblesGrid().length-1; i++) {
            System.out.print(reducedGameBoard.getMarblesGrid()[i].toString() + " | ");
            if ((i+1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println("FREE MARBLE: " + reducedGameBoard.getMarblesGrid()[reducedGameBoard.getMarblesGrid().length-1].toString());
    }
}
