/**
 * Contains the constraints to the sudoku problem.
 * 
 * @author László Tárkányi
 */
public class Validator {
    // Instance variables

    private static int complexity;
    private static int interval;

    // Constructor

    public Validator(int complexity) {
        Validator.complexity = complexity;
        Validator.interval = (int)Math.sqrt(complexity);
    }

    // Class methods

    /**
     * Checks whether the fully generated board is valid according to the rules of sudoku.
     * 
     * @param b The game board.
     * @return The board is valid or invalid.
     */
    public static boolean isCorrectlyGenerated(Board b) {
        return areSubMatricesCorrect(b) && areRowsCorrect(b) && areColumnsCorrect(b);
    }

    /**
     * Checks whether the numbers in all sub-matrices (or grids) are correct.
     * Takes the square root of the complexity of the puzzle and loops through
     * the sub-matrices.
     * Example: (9 -> 0, 3, 6); (16 -> 0, 4, 8, 12)
     * 
     * @param b The game board.
     * @return The sub-matrix is valid or invalid.
     */
    private static boolean areSubMatricesCorrect(Board b) {
        // store the corner of each sub-matrix as a starting point
        Coordinate cornerCoordinate = new Coordinate();

        // loop through the columns of the board, in intervals of the complexity's square root
        while (cornerCoordinate.getRow() < complexity) {
            // create new array every time we reach a new sub-matrix
            int [] sequence = new int[complexity];
            int sequenceIndex = 0;

            // loop through the rows of the sub-matrix
            for (int i = 0; i < interval; i++) {
                // loop through the columns of the sub-matrix
                for (int j = 0; j < interval; j++) {
                    // store values of the sub-matrix in an array
                    sequence[sequenceIndex] = b.getValue(new Coordinate(cornerCoordinate.getRow() + i, cornerCoordinate.getColumn() + j));
                    for (int k = 0; k < sequenceIndex; k++) {
                        // compare newly added element to the rest of the array
                        if (sequence[k] == sequence[sequenceIndex]) {
                            // duplicate found
                            return false;
                        }
                    }
                    sequenceIndex++;
                }
            }
            // skip to the next sub-matrix in a grid pattern
            cornerCoordinate.setColumn(cornerCoordinate.getColumn() + interval);
            if (cornerCoordinate.getColumn() == complexity) {
                cornerCoordinate.setRow(cornerCoordinate.getRow() + interval);
                cornerCoordinate.setColumn(0);
            }
        }
        return true;
    }

    /**
     * Checks whether the numbers in all rows are correct.
     * 
     * @param b The game board.
     * @return The row is valid or invalid.
     */
    private static boolean areRowsCorrect(Board b) {
        // loop through the rows of the board
        for (int i = 0; i < complexity; i++) {
            // create new array every time we reach a new row
            int[] sequence = new int[complexity];
            int sequenceIndex = 0;

            for (int j = 0; j < complexity; j++) {
                // store values of the row in an array
                sequence[sequenceIndex] = b.getValue(new Coordinate(i, j));
                for (int k = 0; k < sequenceIndex; k++) {
                    // compare newly added element to the rest of the array
                    if (sequence[k] == sequence[sequenceIndex]) {
                        return false; // duplicate found
                    }
                }
                sequenceIndex++;
            }
        }
        return true;
    }

    /**
     * Checks whether the numbers in all columns are correct.
     * 
     * @param b The game board.
     * @return The column is valid or invalid.
     */
    private static boolean areColumnsCorrect(Board b) {
        // loop through the column of the board
        for (int i = 0; i < complexity; i++) {
            // create new array every time we reach a new column
            int[] sequence = new int[complexity];
            int sequenceIndex = 0;

            for (int j = 0; j < complexity; j++) {
                // store values of the column in an array
                sequence[sequenceIndex] = b.getValue(new Coordinate(i, j));
                for (int k = 0; k < sequenceIndex; k++) {
                    // compare newly added element to the rest of the array
                    if (sequence[k] == sequence[sequenceIndex]) {
                        return false; // duplicate found
                    }
                }
                sequenceIndex++;
            }
        }
        return true;
    }

    /**
     * Check if the location is correct according to
     * the rules for a new number to be inserted into the board.
     * 
     * @param cursor The location to be inserted into.
     * @param num The number to be inserted.
     * @param b The game board.
     * @return The number can be inserted into the given location or not.
     */
    public boolean isLocationValid(Coordinate cursor, int num, Board b) {
        // find nearest sub-matrix corner
        Coordinate corner = new Coordinate(cursor.getRow() - (cursor.getRow() % interval), cursor.getColumn() - (cursor.getColumn() % interval));

        // check the sub-matrix of the location
        for (int i = corner.getRow(); i < corner.getRow() + interval; i++) {
            for (int j = corner.getColumn(); j < corner.getColumn() + interval; j++) {
                if (b.getValue(new Coordinate(i, j)) == num) {
                    return false;
                }
            }
        }

        // check the row of the location
        for (int i = 0; i < complexity; i++) {
            if (b.getValue(new Coordinate(cursor.getRow(), i)) == num) {
                return false;
            }
        }

        // check the column of the location
        for (int i = 0; i < complexity; i++) {
            if (b.getValue(new Coordinate(i, cursor.getColumn())) == num) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether the game board has an empty square or not.
     * 
     * @param b The game board.
     * @return The grid contains an empty square or not.
     */
    public boolean hasEmptySquare(Board b) {
        // loop through the rows of the board
        for (int i = 0; i < complexity; i++) {
            // loop through the columns of the board
            for (int j = 0; j < complexity; j++) {
                if (b.getValue(new Coordinate(i, j)) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}