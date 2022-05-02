/**
 * A class that represents a sudoku game board.
 * 
 * @author László Tárkányi
 */
public class Board {
    // Instance variables
    
    private int[][] fields;
    private int complexity;
    private final int DEFAULT_COMPLEXITY = 9;

    // Constructors

    /**
     * Default constructor builds a board with 9x9 fields
     */
    public Board() {
        this.fields = new int[DEFAULT_COMPLEXITY][DEFAULT_COMPLEXITY];
        this.complexity = DEFAULT_COMPLEXITY;
    }

    public Board(int complexity) {
        this.fields = new int[complexity][complexity];
        this.complexity = complexity;
    }

    // Accessors

    public int[][] getFields() {
        return fields;
    }

    public int getValue(Coordinate c) {
        return this.fields[c.getRow()][c.getColumn()];
    }

    public int getComplexity() {
        return complexity;
    }

    // Mutators

    public void setFields(int[][] fields) {
        this.fields = fields;
    }

    public void updateField(Coordinate c, int value) {
        this.fields[c.getRow()][c.getColumn()] = value;
    }

    // Class Methods

    /**
     * Nullifies a random field in the game board.
     *
     */
    public void popRandomValue() {
        int randomField = (int)((Math.random() * ((this.complexity * this.complexity)) - 1) + 1);
        Coordinate randomCoordinate = new Coordinate((randomField / this.complexity), (randomField % this.complexity));
        while (this.getValue(randomCoordinate) == 0) {
            randomField = (int)((Math.random() * ((this.complexity * this.complexity)) - 1) + 1);
            randomCoordinate.setRow((randomField / this.complexity));
            randomCoordinate.setColumn((randomField % this.complexity));
        }
        this.updateField(randomCoordinate, 0);

    }

    /**
     * Removes a given amount of values from the puzzle board.
     * 
     * @param numOfCluesFromDifficulty the amount of numbers to be removed
     */
    public void setPuzzleDifficulty(int numOfCluesFromDifficulty) {
        for (int i = 0; i < (this.getComplexity() * this.getComplexity()) - numOfCluesFromDifficulty; i++) {
            this.popRandomValue();
        }
    }

    /**
     * Gets the elements of the original board and inserts them into the current one.
     *
     * @param original the board object to be copied from
     */
    public void copyValues(Board original) {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                Coordinate cursor = new Coordinate(i, j);
                this.updateField(cursor, original.getValue(cursor));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            Board o = (Board) obj;

            for (int i = 0; i < this.complexity; i++) {
                for (int j = 0; j < this.complexity; j++) {
                    Coordinate cursor = new Coordinate(i, j);

                    if (this.getValue(cursor) != o.getValue(cursor)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prints the game board.
     */
    public void printBoard() {
        for (int i = 0; i < complexity; i++) {
            for (int j = 0; j < complexity; j++) {
                if (complexity < 10) {
                    if (fields[i][j] != 0) {
                        System.out.print("[" + fields[i][j] + "]");
                    } else {
                        System.out.print("[ ]");
                    }
                } else {
                    if (fields[i][j] != 0) {
                        if (fields[i][j] < 10) {
                            System.out.print("[ " + fields[i][j] + "]");
                        } else {
                            System.out.print("[" + fields[i][j] + "]");
                        }
                    } else {
                        System.out.print("[  ]");
                    }
                    
                }
                
                if (j % Math.sqrt(complexity) == Math.sqrt(complexity) - 1) {
                    System.out.print(" ");
                }
            }

            if (i % Math.sqrt(complexity) == Math.sqrt(complexity) - 1) {
                System.out.println();
            }
            System.out.println();
        }
    }
}