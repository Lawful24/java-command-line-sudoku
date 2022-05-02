import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

/**
 * Prints text on the console that the user is supposed to interact with.
 * Also controls the main menu logic and navigation.
 * 
 * @author László Tárkányi
 */
public class UserInterface {
    private final static String[] MENU_OPTIONS = {"[1] New Game", "[2] Replays", "[q] Quit program"};
    private final static String[] DIFF_OPTIONS = {"[1] Easy", "[2] Intermediate", "[3] Expert", "[4] Gamer", "[b] Back to menu"};
    private enum Difficulty {
        EASY (0.65),
        INTERMEDIATE (0.5),
        EXPERT (0.22),
        GAMER (0);

        private final double percentage;

        Difficulty (double percentage) {
            this.percentage = percentage;
        }
    }
    private final static int MAX_COMPLEXITY = 25;
    private final static int ASCII_LOWER_BOUNDARY = 48; // 0
    private final static int ASCII_HIGHER_BOUNDARY = 57; // 9
    private final static int REPLAY_DELAY = 3000; // ms

    /**
     * Prints the welcome screen to the console and initiates the main menu.
     * 
     * @param scanner The input device of the terminal.
     */
    public static void startUp(Scanner scanner) {
        // yikes
        System.out.println("                             mm                                   ");
        System.out.println(" m@***@m@                  *@@@            *@@@                   ");
        System.out.println("m@@    *@                    @@              @@                   ");
        System.out.println("*@@@m    *@@@  *@@@     m@**@@@    m@@*@@m   @@  m@@* *@@@  *@@@  ");
        System.out.println("  *@@@@@m  @@    @@   m@@    @@   @@*   *@@  @@ m@      @@    @@  ");
        System.out.println("      *@@  !@    @@   @!@    @!   @@     @@  !@m@@      !@    @@  ");
        System.out.println("@@     @@  !@    @!   *!@    @!   @@     !@  !@ *@@m    !@    @!  ");
        System.out.println("!     *@!  !@    !!   !!!    !!   !@     !!  !!!!!      !@    !!  ");
        System.out.println("!!     !!  !!    !!   *:!    !:   !!!   !!!  :! *!!!    !!    !!  ");
        System.out.println(":!: : :!   :: !: :!:   : : : ! :   : : : : : : :  : :   :: !: :!: ");
        System.out.println("Welcome to Lawful's command-line Sudoku game.\n");
        presentMenuInterface(scanner);
    }

    /**
     * Draws the main menu on the terminal.
     * 
     * @param scanner The input device of the terminal.
     */
    public static void presentMenuInterface(Scanner scanner) {
        String keyPress = "";

        while (!keyPress.equals("q")) {
            for (String option : MENU_OPTIONS) {
                System.out.println(option);
            }
            keyPress = scanner.next();

            // Handle user input choice
            switch (keyPress) {
                case "1":
                    letUserChooseDifficulty(scanner);
                    break;

                case "2":
                    if (ReplayManagerSingleton.getInstance().getReplays() != null) {
                        letUserChooseReplay(scanner);
                    } else {
                        System.out.println("Try playing some games first.");
                    }
                    break;

                case "q":
                    System.out.println("Thanks for playing.");
                    return;

                default:
                    System.out.println("Invalid input, try again.");
                    break;
            }
        }
    }

    /**
     * Allows the user to pick the difficulty of the puzzle
     * and navigates to the next user prompt.
     * 
     * @param scanner The input device of the terminal.
     */
    private static void letUserChooseDifficulty(Scanner scanner) {
        String keyPress = "";
        
        while (!keyPress.equals("b")) {
            System.out.println("\nChoose a difficulty:\n");
        
            for (String option : DIFF_OPTIONS) {
                System.out.println(option);
            }
            keyPress = scanner.next();

            // Handle user input choice
            switch (keyPress) {
                case "1" -> {
                    // easy difficulty chosen
                    initGame(Difficulty.EASY, scanner);
                    return;
                }
                case "2" -> {
                    // intermediate difficulty chosen
                    initGame(Difficulty.INTERMEDIATE, scanner);
                    return;
                }
                case "3" -> {
                    // expert difficulty chosen
                    initGame(Difficulty.EXPERT, scanner);
                    return;
                }
                case "4" -> {
                    // gamer difficulty chosen
                    initGame(Difficulty.GAMER, scanner);
                    return;
                }
                case "b" -> {
                    // init back sequence
                    System.out.println("Going back.");
                    return;
                }
                default -> System.out.println("Invalid input, try again.");
            }
        }
    }

    /**
     * Allows the user to select the size of the puzzle board.
     * 
     * @param scanner The input device of the terminal.
     * @return The length of one side of the puzzle.
     */
    private static int letUserChooseComplexity(Scanner scanner) {
        System.out.println("\n[1-" + MAX_COMPLEXITY +"] How many numbers should there be in a row? (perfect square values only)");
        System.out.println("[b] Back to main menu");

        String keyPress = "";

        // loop until the user chooses to navigate elsewhere
        while (!keyPress.equals("b")) {
            keyPress = scanner.next();
            int charCount = 0;

            // loop through the characters of the input string
            while (charCount < keyPress.length()) {
                int charCode = keyPress.charAt(charCount);

                // if character outside of range found, invalid input
                if (charCode < ASCII_LOWER_BOUNDARY || charCode > ASCII_HIGHER_BOUNDARY) {
                    break;
                }
                charCount++;
            }

            if (!keyPress.equals("b")) {
                // valid input path
                if (charCount == keyPress.length() && charCount > 0) {
                    // check whether the input is a perfect square
                    int inputNumber = Integer.parseInt(keyPress);

                    if (inputNumber > MAX_COMPLEXITY) {
                        System.out.println("Please choose a smaller number.");
                        continue;
                    }
                    double root = Math.sqrt(inputNumber);
                    double floor = Math.floor(root);
                    
                    if (root - floor == 0.0) {
                        return inputNumber;
                    } else {
                        System.out.println("Not a perfect square, try again.");
                    }
                } else {
                    System.out.println("Invalid value, try again.");
                }
            } else {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Initiates the user's interaction with the puzzle.
     * 
     * @param difficulty The level of difficulty chosen by the user.
     * @param scanner The input device of the terminal.
     */
    private static void initGame(Difficulty difficulty, Scanner scanner) {
        int complexity = 0;
        
        while (complexity == 0) {
            complexity = letUserChooseComplexity(scanner);
        }

        if (complexity != -1) {
            // initialise the game
            Board solution = new Board(complexity);
            // AN: Commented code is used for measuring generational algorithm performance
            /*
            long start = System.nanoTime();
            */
            BoardFactory.generateSolution(solution, complexity);
            /*
            long end = System.nanoTime();
            System.out.println((double) (end - start) / 1000000000);
            */
            Board puzzleBoard = new Board(complexity);
            puzzleBoard.copyValues(solution);
            puzzleBoard.setPuzzleDifficulty((int)((complexity * complexity) * difficulty.percentage));
            Game newGame = new Game(solution, puzzleBoard);
            newGame.play(scanner);
        }
    }

    /**
     * Presents the previously played games as a list and allows the user to select one of them.
     * 
     * @param scanner The input device of the terminal.
     */
    private static void letUserChooseReplay(Scanner scanner) {
        String keyPress = "";
        ArrayList<Replay> replays = ReplayManagerSingleton.getInstance().readReplaysFromFile();
        int numOfReplays = replays.size();

        // print all previously played games as dates
        for (int i = 0; i < numOfReplays; i++) {
            Replay currentReplay = replays.get(i);
            if (!currentReplay.getGame().isEmpty()) {
                int complexity = currentReplay.getGame().peek().getBoardSnapshot().getComplexity();
                System.out.println("[" + (i + 1) + "] " + currentReplay.getDate() + " || " + complexity + "x" + complexity + " || " + currentReplay.getGame().size() + " steps ");
            }
        }
        System.out.println("\n[1-" + numOfReplays + "] Choose the game to replay");
        System.out.println("[b] Back to main menu\n");

        // handle user input
        while (!keyPress.equals("b")) {
            keyPress = scanner.next();
            int charCount = 0;

            // validate input
            while (charCount < keyPress.length()) {
                int charCode = keyPress.charAt(charCount);

                if (charCode < ASCII_LOWER_BOUNDARY || charCode > ASCII_HIGHER_BOUNDARY) {
                    break;
                }
                charCount++;
            }

            if (charCount == keyPress.length()) {
                int inputNumber = Integer.parseInt(keyPress);

                if (inputNumber <= replays.size() && inputNumber > 0) {
                    playReplay(replays.get(inputNumber - 1).getGame());
                    return;
                } else {
                    System.out.println("Chosen number out of range.");
                }
            } else if (!keyPress.equals("b")) {
                System.out.println("Invalid input, try again.");
            }
        }
    }

    /**
     * A step-by-step presentation of a given game played by the user.
     * 
     * @param game The game log storing the steps.
     */
    private static void playReplay(Deque<Step> game) {
        while (!game.isEmpty()) {
            // remove steps from the front of the deque
            Step nextStep = game.removeFirst();
            nextStep.getBoardSnapshot().printBoard();
            System.out.println("Next step:\nRow: " + (nextStep.getInputCoordinate().getRow() + 1) + ", Column: " + (nextStep.getInputCoordinate().getColumn() + 1));
            System.out.println("Value: " + nextStep.getInputValue());
            System.out.println("----------------");

            // delay between showing steps
            try {
                Thread.sleep(REPLAY_DELAY);
            } catch (InterruptedException e) {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }

            // present final state of the game board
            if (game.isEmpty()) {
                
                Coordinate lastCoordinate = new Coordinate(nextStep.getInputCoordinate().getRow(), nextStep.getInputCoordinate().getColumn());
                nextStep.getBoardSnapshot().updateField(lastCoordinate, nextStep.getInputValue());
                nextStep.getBoardSnapshot().printBoard();
                System.out.println("End of replay.");
            }
        }
    }
}