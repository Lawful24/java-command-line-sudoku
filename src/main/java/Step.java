/**
 * Stores the details of the user's actions in each turn of the sudoku game.
 * 
 * @author László Tárkányi
 */
public class Step {
    // Instance variables

    private final Board boardSnapshot;
    private final Coordinate inputCoordinate;
    private final int inputValue;

    // Constructor

    public Step(Board boardSnapshot, Coordinate inputCoordinate, int inputValue) {
        this.boardSnapshot = boardSnapshot;
        this.inputCoordinate = inputCoordinate;
        this.inputValue = inputValue;
    }

    // Accessors

    public Board getBoardSnapshot() {
        return boardSnapshot;
    }

    public Coordinate getInputCoordinate() {
        return inputCoordinate;
    }

    public int getInputValue() {
        return inputValue;
    }
}
