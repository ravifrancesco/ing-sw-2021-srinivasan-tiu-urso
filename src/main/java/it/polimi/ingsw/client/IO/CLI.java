package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CLI implements UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private Scanner input;

    private ReducedModel reducedModel;

    private ClientConnection clientConnection;

    public CLI() {
        this.input = new Scanner(System.in);
    }

    public void printColoredMessage(String message, String color) {
        System.out.println(color + message + ANSI_RESET);
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

    public void setReducedModel(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public String readCommand() {
        System.out.println("Enter command:");
        System.out.print("> ");
        return input.nextLine();
    }

    public String readLine() {
        return input.nextLine();
    }

    public void printErrorMessage(String error) {
        System.out.println(ANSI_RED + error + ANSI_RESET);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void startReadingThread() {
        // TODO how to kill this thread if the receivingThread dies?
        new Thread(() -> {
            while(true) {
                String command = this.readCommand();
                if(reducedModel.getReducedGame().getCurrentPlayer() != null) {
                    System.out.println("# It's " + reducedModel.getReducedGame().getCurrentPlayer() + "'s turn");
                }
                ClientMessage clientMessage = ClientMessageInputParser.parseInput(command, this);
                if (clientMessage != null) {
                    try {
                        clientConnection.send(clientMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // TODO ugly asf, need to find a way to show the "enter command" after server answers
                // and client is shown the response to its command
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @Override
    public void enterGamePhase() {
        // TODO
    }

    public void showHand(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            System.out.println(nickname + "'s HAND: ");
            List<LeaderCard> leaderCardList = reducedPlayer.getHand();
            if (leaderCardList.size() != 0) {
                IntStream.range(0, leaderCardList.size()).forEach(i -> System.out.println(ANSI_BLUE + i + ANSI_RESET + " " + leaderCardList.get(i).toString()));
            } else
            {
                System.out.println(reducedPlayer.getHandSize() + " cards");
            }
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
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
            System.out.println(nickname + "'s supply:");
            System.out.println(reducedPlayer.getDashboard().getSupply());
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
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
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
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
            reducedDashboard.getLocker().forEach((key, value) -> System.out.println("RESOURCE = " + key + " | QUANTITY = " + value));
            System.out.println("EXTRA DEPOSITS: ");
            IntStream.range(0, reducedDashboard.getExtraDeposits().length).forEach(i -> System.out.println("INDEX " + i + ":" + Arrays.toString(reducedDashboard.getExtraDeposits()[i])));
        } else
        {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
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

    @Override
    public void startUI(ClientConnection clientConnection, ReducedModel reducedModel) {
        setClientConnection(clientConnection);
        setReducedModel(reducedModel);
    }

    @Override
    public UIType getType() {
        return UIType.CLI;
    }
}
