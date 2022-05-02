import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.Stack;

/**
 * Responsible for managing the flow of the program and the player input while playing.
 * 
 * @author László Tárkányi
 */
public class Game {
    // Instance variables

    private Board solution;
    private Board gameBoard;
    private Deque<Step> steps;
    private Stack<Step> undoStack;
    private int mistakesCount;
    private static final int MAX_MISTAKES = 3;
    private final String[] GAME_ACTIONS = {"\n[row,column,value] Enter your next step", "[u] Undo", "[r] Redo", "[q] Or quit to main menu, progress will not be saved\n"};
    private final static int ASCII_LOWER_BOUNDARY = 48; // 0
    private final static int ASCII_HIGHER_BOUNDARY = 57; // 9
    private final static int ASCII_COMMA_CODE = 44; // ,

    // Constructor

    public Game(Board solution, Board gameBoard) {
        this.solution = solution;
        this.gameBoard = gameBoard;
        this.steps = new ArrayDeque<Step>();
        this.undoStack = new Stack<Step>();
        this.mistakesCount = 0;
    }

    // Accessors

    public Board getSolution() {
        return this.solution;
    }

    public Board getGameBoard() {
        return this.gameBoard;
    }

    public Deque<Step> getSteps() {
        return this.steps;
    }

    public int getMistakesCount() {
        return this.mistakesCount;
    }

    // Class methods

    /**
     * Leads the player through a game of sudoku, step by step.
     * 
     * @param scanner The input device of the terminal.
     */
    public void play(Scanner scanner) {
        while (!isGameOver()) {
            printActions();
            boolean invalidInput = true;

            while(invalidInput) {
                String stepInput = scanner.next();

                switch (stepInput) {
                    case "u":
                        if (!this.steps.isEmpty()) {
                            invalidInput = false;
                            this.chaChaSlide(true);
                        } else {
                            System.out.println("Undo not possible yet, try adding numbers first.");
                        }
                        break;

                    case "r":
                        if (!this.undoStack.isEmpty()) {
                            invalidInput = false;
                            this.chaChaSlide(false);
                        } else {
                            System.out.println("Redo not possible yet, try undoing steps first.");
                        }
                        break;
                    
                    case "q":
                        return;
                        
                    default:
                        // validate input
                        int charCount = 0;
                        int commaCount = 0;

                        while (charCount < stepInput.length()) {
                            int charCode = stepInput.charAt(charCount);

                            if ((charCode < ASCII_LOWER_BOUNDARY || charCode > ASCII_HIGHER_BOUNDARY) && charCode != ASCII_COMMA_CODE) {
                                break;
                            } else if (charCode == ASCII_COMMA_CODE) {
                                commaCount++;
                            }
                            charCount++;
                        }

                        if (charCount == stepInput.length() && commaCount == 2) {
                            String[] split = stepInput.split(",");
                            invalidInput = false;
                            executeStep(split);
                        } else {
                            System.out.println("Invalid input, try again.");
                        }
                        break;
                }
            }
        }
        ReplayManagerSingleton.getInstance().saveReplayToFile(this.steps);
    }

    /**
     * Prints the board in its current state and the options of the player.
     */
    private void printActions() {
        System.out.println();
        this.gameBoard.printBoard();
        System.out.println("Mistakes: " + this.mistakesCount + "/" + MAX_MISTAKES);
        System.out.println(GAME_ACTIONS[0]);
        if (!this.steps.isEmpty()) {
            System.out.println(GAME_ACTIONS[1]);
        }
        if (!this.undoStack.isEmpty()) {
            System.out.println(GAME_ACTIONS[2]);
        }
        System.out.println(GAME_ACTIONS[3]);
    }

    /**
     * Modifies the board according to the player's choice of coordinates and 
     * 
     * @param parsedInput The handled input of the player.
     */
    private void executeStep(String[] parsedInput) {
        int inputRow = Integer.parseInt(parsedInput[0]);
        int inputColumn = Integer.parseInt(parsedInput[1]);
        int inputValue = Integer.parseInt(parsedInput[2]);

        // validate the input
        if (inputRow <= this.gameBoard.getComplexity() && inputColumn <= this.gameBoard.getComplexity() && inputValue <= this.gameBoard.getComplexity() && inputRow > 0 && inputColumn > 0 && inputValue > 0) {
            Coordinate inputCoordinate = new Coordinate(inputRow - 1, inputColumn - 1);

            // check whether the selected field is empty or not
            if (gameBoard.getValue(inputCoordinate) == 0) {
                // create snapshot and initialise new step
                Board snapShot = new Board(this.gameBoard.getComplexity());
                snapShot.copyValues(this.gameBoard);
                Step step = new Step(snapShot, inputCoordinate, inputValue);
                this.steps.add(step);
                this.undoStack.clear();

                // check the player's value against the solution
                if (solution.getValue(inputCoordinate) == inputValue) {
                    this.gameBoard.updateField(inputCoordinate, inputValue);
                    System.out.println("Successful step.\n");
                } else {
                    this.mistakesCount++;
                    System.out.println("Gah! Mistake!\n");
                }
            } else {
                System.out.println("Field already filled, try again.");
            }
        } else {
            System.out.println("Invalid value, try again.");
        }
    }

    /**
     * Handles undo and redo actions.
     * 
     * @param isUndo Is the chosen action an undo or a redo?
     */
    private void chaChaSlide(boolean isUndo) {
        Board snapShot = new Board(this.gameBoard.getComplexity());
        snapShot.copyValues(this.gameBoard);
        Step blankStep;
        Step current;

        if (isUndo) {
            // undo last step
            blankStep = this.steps.removeLast();
            current = new Step(snapShot, blankStep.getInputCoordinate(), blankStep.getInputValue());
            this.undoStack.push(current);
        } else {
            // redo last undone step
            blankStep = this.undoStack.pop();
            current = new Step(snapShot, blankStep.getInputCoordinate(), blankStep.getInputValue());
            this.steps.addLast(current);
        }
        this.gameBoard.copyValues(blankStep.getBoardSnapshot());
    }

    /**
     * Checks whether the game had met its end conditions or not.
     */
    public boolean isGameOver() {
        if (new Validator(this.gameBoard.getComplexity()).hasEmptySquare(this.gameBoard)) {
                if (this.mistakesCount >= MAX_MISTAKES) {
                    System.out.println("Too many mistakes, unfortunate.");
                    return true;
                } else {
                    return false;
                }
        } else if (solution.equals(gameBoard)) {
            System.out.println(" _       _       _   _           _                         _ _ ");
            System.out.println("( )  _  ( )     (_ )(_ )        (_ )                      ( ) )");
            System.out.println("| | ( ) | |  __  | | | |   _ _   | |   _ _ _   _   __    _| | |");
            System.out.println("| | | | | |/ __ || | | |  (  _ | | | / _  ) ) ( )/ __ |/ _  | |");
            System.out.println("| (_/  _) |  ___/| | | |  | (_) )| |( (_| | (_) |  ___/ (_| |_)");
            System.out.println("| __/|___/  ____)___)___) |  __/(___) __ _)|__  ||____)|__ _)  ");
            System.out.println("                          | |             ( )_| |           (_)");
            System.out.println("                          (_)              |___/               ");
            return true;
        } else{
            // *frantic screaming*
            System.out.println("An error has occurred.");
            return false;
        }
    }
}