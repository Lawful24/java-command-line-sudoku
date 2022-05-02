/**
 * A Coordinate represents a loaction in the sudoku game board.
 * Implemented for higher readability.
 * 
 * @author László Tárkányi
 */
public class Coordinate {
    // Instance variables

    private int row;
    private int column;

    // Constructors

    public Coordinate() {
        this.row = 0;
        this.column = 0;
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // Accessors

    public int getRow() { return row; }
    public int getColumn() { return column; }

    // Mutators

    public void setRow(int row) { this.row = row; }
    public void setColumn(int column) { this.column = column; }
}