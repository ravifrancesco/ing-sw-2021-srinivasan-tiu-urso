package it.polimi.ingsw.view.UI.CLI.IO;

import java.util.HashMap;
import java.util.stream.IntStream;

public class FaithTrackCLI {
    static final int gridRows = 4;
    static final int gridCols = 25;
    static final String[][] grid = new String[gridRows][gridCols];
    private static int currPos;

    /**
     * Class used to show a faith track
     */
    public FaithTrackCLI() {
        for(int i = 0; i < gridRows; i++) {
            for(int j = 0; j < gridCols; j++) {
                if (i == 1) {
                    grid[i][j] = Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET +
                            Constants.FT_EMPTY + "    " + Constants.ANSI_RESET +
                            Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET;
                } else {
                    grid[i][j] = "      ";
                }
            }
        }

        for(int j = 5; j <= 8; j++) {
            grid[0][j] = Constants.FT_SPACE_UNACTIVATED + "      " + Constants.ANSI_RESET;
        }
        for(int j = 12; j <= 16; j++) {
            grid[0][j] = Constants.FT_SPACE_UNACTIVATED + "      " + Constants.ANSI_RESET;
        }
        for(int j = 19; j <= 24; j++) {
            grid[0][j] = Constants.FT_SPACE_UNACTIVATED + "      " + Constants.ANSI_RESET;
        }
        for(int j = 0; j <= 24; j++) {
            grid[2][j] = Constants.BOLD + "  " + j + (j > 9 ? "  " : "   ");
        }

        HashMap<Integer, Integer> indexVp = new HashMap<>();
        indexVp.put(3, 1);
        indexVp.put(6, 2);
        indexVp.put(9, 4);
        indexVp.put(12, 6);
        indexVp.put(15, 9);
        indexVp.put(18, 12);
        indexVp.put(21, 16);
        indexVp.put(24, 20);

        for(int j = 0; j <= 24; j++) {
            if(indexVp.containsKey(j)) {
                grid[3][j] = Constants.BOLD + "  " + Constants.BOLD + Constants.ANSI_GREEN +indexVp.get(j) +
                        Constants.ANSI_RESET + (indexVp.get(j) > 9 ? "  " : "   ");
            } else {
                grid[3][j] = "      ";
            }
        }
        setPos(0);
        currPos = 0;
    }

    /**
     * Sets the current player position on the track (prints red background ansi codes)
     * @param i the position
     */
    public void setPos(int i) {
        restore(currPos);
        grid[1][i] = Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET +
                Constants.FT_POS + "    " + Constants.ANSI_RESET +
                Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET;
        currPos = i;
    }

    /**
     * Removes a previous position (re-prints background ansi-code) on the position
     * @param i the position to restore
     */
    public void restore(int i) {
        grid[1][i] = Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET +
                Constants.FT_EMPTY + "    " + Constants.ANSI_RESET +
                Constants.FT_EMPTY + Constants.BLACK + Constants.BOLD + "|" + Constants.ANSI_RESET;
    }

    /**
     * Colors the vatican report for when it is activated correctly
     * @param i the index for the vatican report
     */
    public void activateVR(int i) {
        switch(i) {
            case(1) -> IntStream.range(5, 9).forEach(j -> grid[0][j] = Constants.FT_SPACE_ACTIVATED + "      " + Constants.ANSI_RESET);
            case(2) -> IntStream.range(12, 17).forEach(j -> grid[0][j] = Constants.FT_SPACE_ACTIVATED + "      " + Constants.ANSI_RESET);
            case(3) -> IntStream.range(19, 25).forEach(j -> grid[0][j] = Constants.FT_SPACE_ACTIVATED + "      " + Constants.ANSI_RESET);
        }
    }

    /**
     * Colors the vatican report for when it is missed
     * @param i the index for the vatican report
     */
    public void missVR(int i) {
        switch(i) {
            case(1) -> IntStream.range(5, 9).forEach(j -> grid[0][j] = Constants.FT_SPACE_MISSED + "      " + Constants.ANSI_RESET);
            case(2) -> IntStream.range(12, 17).forEach(j -> grid[0][j] = Constants.FT_SPACE_MISSED + "      " + Constants.ANSI_RESET);
            case(3) -> IntStream.range(19, 25).forEach(j -> grid[0][j] = Constants.FT_SPACE_MISSED + "      " + Constants.ANSI_RESET);
        }
    }

    /**
     * Handles a vatican report and colors it the correct way
     * @param i the vatican report index
     * @param state the state index, 1 if missed 2 if activated
     */
    public void handleVr(int i, int state) {
        switch(state) {
            case(1) -> missVR(i);
            case(2) -> activateVR(i);
        }
    }

    /**
     * Shows the faith track itself
     */
    public void showFTCLI() {
        for(int i = 0; i < gridRows; i++) {
            for(int j = 0; j < gridCols; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

}
