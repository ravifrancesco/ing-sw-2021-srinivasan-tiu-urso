package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.*;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class InputChecker {

    // Message

    public static ClientMessage createGame(String[] input) {
        try {
            int numberOfPlayers = Integer.parseInt(input[1]);
            return new CreateGameLobby(numberOfPlayers);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage showGames(String[] input) {
        return new AskGameLobbies();
    }

    public static ClientMessage joinGame(String[] input) {
        try {
            String id = input[1];
            System.out.println("Joining lobby " + id + "...");
            return new JoinGameLobby(id);
        } catch (Exception e) {
            System.out.println("Something went wrong while joining a game");
            return null;
        }
    }

    public static ClientMessage leaveLobby(String[] in) {
        return new LeaveLobby();
    }

    public static ClientMessage quit(String[] in) {
        return new QuitGame();
    }

    public static ClientMessage discardExcessCard(String[] in) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayerDiscardsExcessLeaderCards(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage endTurn(String[] in) {
        return new EndTurnGameMessage();
    }

    public static ClientMessage discardCard(String[] in) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayerDiscardsLeaderCard(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage getFromMarket(String[] in, CLI cli) {
        try {
            int move = Integer.parseInt(in[1]);
            ReducedPlayer currentPlayer = cli.getReducedModel().getReducedPlayer();
            return new PlayerGetsFromMarket(move, new ArrayList<>());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage storeFromSupply(String[] in) {
        try {
            int supplyPos = Integer.parseInt(in[1]);
            int depositPos = Integer.parseInt(in[2]);
            return new PlayerStoresFromSupply(supplyPos, depositPos);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage storeFromSupplyToExtraDeposit(String[] in) {
        try {
            int supplyPos = Integer.parseInt(in[1]);
            int extraDepositIndex = Integer.parseInt(in[2]);
            int extraDepositPos = Integer.parseInt(in[3]);
            return new PlayerStoresFromSupplyToExtraDeposit(supplyPos, extraDepositIndex, extraDepositPos);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage getInitialResources(String[] in) {
        try {
            Resource initialResource = resourceParser(in[1]);
            if(initialResource == null) { throw new IllegalArgumentException(); }
            int pos = Integer.parseInt(in[2]);
            return new GetInitialResourcesGameMessage(initialResource, pos);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage loadGameSettings(String[] in) {
        String path = in[1];
        // is this the correct usage?
        GameSettings gameSettings = new GameSettings(path);
        gameSettings.loadSettings(path);
        return new LoadGameSettingsGameMessage(gameSettings);
    }

    public static ClientMessage changeDeposit(String[] in, CLI cli) {
        try {
            return new PlayerChangesDeposit(swapDeposit(cli));
        } catch (Exception e) {
            System.out.println("Failed swapping the deposit");
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static Resource[] swapDeposit(CLI cli) {
        System.out.println("Please insert the pair of indexes to swap, input DONE when finished.");
        Resource[] newDeposit = new Resource[6];
        IntStream.range(0, 6).forEach(i -> newDeposit[i] = cli.getReducedModel().getReducedPlayer().getDashboard().getDeposit()[i]);
        String[] input;
        int firstIndex;
        int secondIndex;
        Resource temp;
        do {
            input = cli.readCommand().toUpperCase().split(" ");
            if(input[0].equals("DONE")) {
                System.out.println("Swaps finished, sending request to server");
                break;
            } else {
                System.out.println("Swapping...");
                firstIndex = Integer.parseInt(input[0]);
                secondIndex = Integer.parseInt(input[1]);
                temp = newDeposit[firstIndex];
                newDeposit[firstIndex] = newDeposit[secondIndex];
                newDeposit[secondIndex] = temp;
            }
        } while (true);
        return newDeposit;
    }

    public static ClientMessage playLeaderCard(String[] in) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayLeaderCardGameMessage(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage activateDashboardProduction(String[] in, CLI cli) {
        try {

        } catch (Exception e) {
            System.out.println("Error whilst activating dashboard production");
        }
        return new ActivateDashboardProductionGameMessage(new ResourceContainer(), new HashMap<>(), new HashMap<>());
    }

    public static ClientMessage activateDevelopmentProduction(String[] in) {
        // TODO handle & implement argument management
        try {
            int card = Integer.parseInt(in[1]);
            return new ActivateDevelopmentProductionGameMessage(card, new ResourceContainer(), new HashMap<>(), new HashMap<>());
        } catch (Exception e) {
            System.out.println("Error whilst activating development production");
            return null;
        }
    }

    public static ClientMessage activateLeaderProduction(String[] in) {
        // TODO handle & implement argument management
        try {
            int card = Integer.parseInt(in[1]);
            return new ActivateLeaderProductionGameMessage(card, new ResourceContainer(), new HashMap<>(), new HashMap<>());
        } catch (Exception e) {
            System.out.println("Error whilst activating development production");
            return null;
        }
    }

    /**
     * Gets a string with the name of a resource and returns the actual resource object.
     * @param in the resource to parse
     * @return the resource object
     */
    private static Resource resourceParser(String in) {
        return switch (in.toUpperCase()) {
            case "STONE" -> Resource.STONE;
            case "SERVANT" -> Resource.SERVANT;
            case "SHIELD" -> Resource.SHIELD;
            case "GOLD" -> Resource.GOLD;
            default -> null;
        };
    }


    public static ClientMessage startGame(String[] in) {
        return new StartGameGameMessage();
    }

    public static ClientMessage buyDevelopmentCard(String[] in, CLI cli) {
        try {
            int row = Integer.parseInt(in[1]);
            int column = Integer.parseInt(in[2]);
            int position = Integer.parseInt(in[3]);
            ResourceContainer resourceToPay = chooseResources(cli);
            return new BuyDevelopmentCardGameMessage(row, column, resourceToPay, position);
        } catch (Exception e) {
            System.out.println("Error while buying development card");
            return null;
        }
    }

    public static ResourceContainer chooseResources(CLI cli) {
        ResourceContainer rc = new ResourceContainer();
        System.out.println("Please choose with which resources you wish to pay the price: ");
        String[] input;
        String command;
        do {
            input = cli.readCommand().toUpperCase().split(" ");
            command = input[0];
            switch (command) {
                case "DEPOSIT" -> handleDepositSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
                case "LOCKER" -> handleLockerSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
                case "EXTRADEPOSIT" -> handleExtraDepositSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
            }
            System.out.println("Resource Container at the moment has:");
            System.out.println("DEPOSIT INDEXES: " + rc.getSelectedDepositIndexes());
            System.out.println("LOCKER RESOURCES: " + rc.getSelectedLockerResources());
            System.out.println("EXTRADEPOSIT INDEXES: " + rc.getSelectedExtraDepositIndexes());
        } while (!command.equals("DONE"));
        return rc;
    }

    public static void handleDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int index = Integer.parseInt(input[1]);
            rc.addDepositSelectedResource(index, player.getDashboard().getDeposit());
        } catch (Exception e) {
            System.out.println("Error while adding from deposit to resource container");
        }
    }

    public static void handleExtraDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int lcPos = Integer.parseInt(input[1]);
            int lcIndex = Integer.parseInt(input[2]);
            rc.addExtraDepositSelectedResource(lcPos, lcIndex, player.getDashboard().getExtraDeposits());
        } catch (Exception e) {
            System.out.println("Error while adding from extra deposit to resource container");
        }
    }

    public static void handleLockerSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            Resource res = resourceParser(input[1]);
            int qty = Integer.parseInt(input[2]);
            rc.addLockerSelectedResource(res, qty, player.getDashboard().getLocker());
        } catch (Exception e) {
            System.out.println("Error while adding from locker to resource container");
        }
    }
}
