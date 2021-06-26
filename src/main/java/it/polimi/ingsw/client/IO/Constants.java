package it.polimi.ingsw.client.IO;

public class Constants {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_BLACK = "\u001b[30m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    public static final String ANSI_MAGENTA = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE = "\u001b[37m";

    public static final String ANSI_BG_BLUE = "\u001b[48;5;21m";
    public static final String ANSI_BG_RED = "\u001b[48;5;196m";
    public static final String ANSI_BG_GREY = "\u001b[48;5;245m";
    public static final String ANSI_BG_PURPLE = "\u001b[48;5;93m";
    public static final String ANSI_BG_WHITE = "\u001b[48;5;231m";
    public static final String ANSI_BG_YELLOW = "\u001b[48;5;226m";

    public static final String[] servant = {
            "  " + concatColored("   ", ANSI_BG_PURPLE) + "  ",
            " " +  concatColored("     ", ANSI_BG_PURPLE) + " ",
            " " +  concatColored("     ", ANSI_BG_PURPLE) + " ",
            "  " + concatColored("   ", ANSI_BG_PURPLE) + "  ",
            " " +  concatColored("     ", ANSI_BG_PURPLE) + " ", concatColored("       ", ANSI_BG_PURPLE) };

    public static final String[] shield = {
            "  " +  concatColored("   ", ANSI_BG_BLUE ) + "  ", // 2
            concatColored("       ", ANSI_BG_BLUE ), // 0
            concatColored("       ", ANSI_BG_BLUE ), // 0
            " " + concatColored("     ", ANSI_BG_BLUE ) + " ", // 1
            " " + concatColored("     ", ANSI_BG_BLUE ) + " ", // 1
            "  " + concatColored("   ", ANSI_BG_BLUE ) + "  " // 2
    };

    public static final String[] stone = {
            concatColored("    ", ANSI_BG_GREY) + "   ",
            concatColored("    ", ANSI_BG_GREY) + "   ",
            concatColored("      ", ANSI_BG_GREY) + " ",
            concatColored("       ", ANSI_BG_GREY),
            concatColored("       ", ANSI_BG_GREY),
            concatColored("       ", ANSI_BG_GREY)
    };

    public static final String[] gold = {
            "  " + concatColored("    ", ANSI_BG_YELLOW) + "  ",
            " " + concatColored("      ", ANSI_BG_YELLOW) + " ",
            " " + concatColored("      ", ANSI_BG_YELLOW) + " ",
            " " + concatColored("      ", ANSI_BG_YELLOW) + " ",
            " " + concatColored("      ", ANSI_BG_YELLOW) + " ",
            "  " + concatColored("    ", ANSI_BG_YELLOW) + "  ",
    };

    public static final String[] empty = {
            "       ",
            "       ",
            "       ",
            "       ",
            "       ",
            "       "
    };



    public static final String logo =
            """
                                                ███▄ ▄███▓    ▄▄▄         ▓█████      ██████    ▄▄▄█████▓    ██▀███      ██▓                               \s
                                               ▓██▒▀█▀ ██▒   ▒████▄       ▓█   ▀    ▒██    ▒    ▓  ██▒ ▓▒   ▓██ ▒ ██▒   ▓██▒                               \s
                                               ▓██    ▓██░   ▒██  ▀█▄     ▒███      ░ ▓██▄      ▒ ▓██░ ▒░   ▓██ ░▄█ ▒   ▒██▒                               \s
                                               ▒██    ▒██    ░██▄▄▄▄██    ▒▓█  ▄      ▒   ██▒   ░ ▓██▓ ░    ▒██▀▀█▄     ░██░                               \s
                                               ▒██▒   ░██▒    ▓█   ▓██▒   ░▒████▒   ▒██████▒▒     ▒██▒ ░    ░██▓ ▒██▒   ░██░                               \s
                                               ░ ▒░   ░  ░    ▒▒   ▓▒█░   ░░ ▒░ ░   ▒ ▒▓▒ ▒ ░     ▒ ░░      ░ ▒▓ ░▒▓░   ░▓                                 \s
                                               ░  ░      ░     ▒   ▒▒ ░    ░ ░  ░   ░ ░▒  ░ ░       ░         ░▒ ░ ▒░    ▒ ░                               \s
                                               ░      ░        ░   ▒         ░      ░  ░  ░       ░           ░░   ░     ▒ ░                               \s
                                                      ░            ░  ░      ░  ░         ░                    ░         ░                                 \s
                                                                                                                                                           \s
                                                                    ▓█████▄    ▓█████     ██▓                                                              \s
                                                                    ▒██▀ ██▌   ▓█   ▀    ▓██▒                                                              \s
                                                                    ░██   █▌   ▒███      ▒██░                                                              \s
                                                                    ░▓█▄   ▌   ▒▓█  ▄    ▒██░                                                              \s
                                                                    ░▒████▓    ░▒████▒   ░██████▒                                                          \s
                                                                     ▒▒▓  ▒    ░░ ▒░ ░   ░ ▒░▓  ░                                                          \s
                                                                     ░ ▒  ▒     ░ ░  ░   ░ ░ ▒  ░                                                          \s
                                                                     ░ ░  ░       ░        ░ ░                                                             \s
                                                                       ░          ░  ░       ░  ░                                                          \s
                                                                     ░                                                                                     \s
                     ██▀███      ██▓    ███▄    █     ▄▄▄           ██████     ▄████▄      ██▓    ███▄ ▄███▓   ▓█████     ███▄    █    ▄▄▄█████▓    ▒█████ \s
                    ▓██ ▒ ██▒   ▓██▒    ██ ▀█   █    ▒████▄       ▒██    ▒    ▒██▀ ▀█     ▓██▒   ▓██▒▀█▀ ██▒   ▓█   ▀     ██ ▀█   █    ▓  ██▒ ▓▒   ▒██▒  ██▒
                    ▓██ ░▄█ ▒   ▒██▒   ▓██  ▀█ ██▒   ▒██  ▀█▄     ░ ▓██▄      ▒▓█    ▄    ▒██▒   ▓██    ▓██░   ▒███      ▓██  ▀█ ██▒   ▒ ▓██░ ▒░   ▒██░  ██▒
                    ▒██▀▀█▄     ░██░   ▓██▒  ▐▌██▒   ░██▄▄▄▄██      ▒   ██▒   ▒▓▓▄ ▄██▒   ░██░   ▒██    ▒██    ▒▓█  ▄    ▓██▒  ▐▌██▒   ░ ▓██▓ ░    ▒██   ██░
                    ░██▓ ▒██▒   ░██░   ▒██░   ▓██░    ▓█   ▓██▒   ▒██████▒▒   ▒ ▓███▀ ░   ░██░   ▒██▒   ░██▒   ░▒████▒   ▒██░   ▓██░     ▒██▒ ░    ░ ████▓▒░
                    ░ ▒▓ ░▒▓░   ░▓     ░ ▒░   ▒ ▒     ▒▒   ▓▒█░   ▒ ▒▓▒ ▒ ░   ░ ░▒ ▒  ░   ░▓     ░ ▒░   ░  ░   ░░ ▒░ ░   ░ ▒░   ▒ ▒      ▒ ░░      ░ ▒░▒░▒░\s
                      ░▒ ░ ▒░    ▒ ░   ░ ░░   ░ ▒░     ▒   ▒▒ ░   ░ ░▒  ░ ░     ░  ▒       ▒ ░   ░  ░      ░    ░ ░  ░   ░ ░░   ░ ▒░       ░         ░ ▒ ▒░\s
                      ░░   ░     ▒ ░      ░   ░ ░      ░   ▒      ░  ░  ░     ░            ▒ ░   ░      ░         ░         ░   ░ ░      ░         ░ ░ ░ ▒ \s
                       ░         ░              ░          ░  ░         ░     ░ ░          ░            ░         ░  ░            ░                    ░ ░ \s
                                                                              ░                                                                            \s""";





    public static final String ANSI_GENERIC_WHITE = "\u001b[38;5;255m";
    public static final String BOLD = "\u001b[1m";


    public static String concatColored(String text, String color) {
        return color + text + ANSI_RESET;
    }

}
