import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class responsible for generating a complete board according to the constraints of
 * Sudoku and also create different difficulty levels.
 * 
 * @author László Tárkányi
 */
public class BoardFactory {
    /**
     * Source: https://lvngd.com/blog/generating-and-solving-sudoku-puzzles-python/
     * 
     * @param b The board of the game.
     * @param complexity The length of one side of the puzzle.
     * @return A boolean value referring to whether the game board has been fully populated or not.
     */
    public static boolean generateSolution(Board b, int complexity) {
        Validator validator = new Validator(complexity);
        List <Integer> nums = IntStream.rangeClosed(1, complexity)
            .boxed().collect(Collectors.toList());
        Coordinate cursor = new Coordinate();
        
        // loop for the number of elements to be inserted
        for (int i = 0; i < complexity * complexity; i++) {
            cursor = new Coordinate((i / complexity), (i % complexity));

            if (b.getValue(cursor) == 0) {
                // shuffle the list of numbers randomly
                Collections.shuffle(nums);

                // loop through the array of numbers from 1 to complexity
                for (int num : nums) {
                    if (validator.isLocationValid(cursor, num, b)) {
                        b.updateField(cursor, num);

                        if (!validator.hasEmptySquare(b)) {
                            return true;
                        } else {
                            if (generateSolution(b, complexity)) {
                                return true;
                            }
                        }
                    }
                }
                break;
            }
        }
        b.updateField(cursor, 0);
        return false;
    }
}