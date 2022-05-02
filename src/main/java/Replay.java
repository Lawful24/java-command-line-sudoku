import java.util.Calendar;
import java.util.Deque;

/**
 * Stores the steps log of a sudoku game.
 * 
 * @author Laszlo Tarkanyi
 */
public class Replay {
    // Instance variables

    private String date;
    private Deque<Step> game;

    // Constructor

    public Replay(Deque<Step> game) {
        this.date = Calendar.getInstance().getTime().toString();
        this.game = game;
    }

    // Accessors
    
    public String getDate() {
        return this.date;
    }

    public Deque<Step> getGame() {
        return this.game;
    }
}
