import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Deque;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Handles file I/O.
 * 
 * @author Laszlo Tarkanyi
 */
public class ReplayManagerSingleton {
    // Instance variables

    private ArrayList<Replay> replays;
    private final Gson gson;
    private static final String FILEPATH = "replays.json";
    private static final Type REPLAY_TYPE = new TypeToken<ArrayList<Replay>>() {}.getType();
    private static ReplayManagerSingleton instance;

    // Constructor

    private ReplayManagerSingleton() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        this.gson = builder.create();
        readReplaysFromFile();
    }
    
    public static ReplayManagerSingleton getInstance() {
        if(instance == null) {
            instance = new ReplayManagerSingleton();
        }
        return instance;
    }

    // Accessor

    public ArrayList<Replay> getReplays() {
        return this.replays;
    }

    // Class methods

    /**
     * Processes the steps log and appends it to the replay log file.
     * 
     * @param steps Game log storing the user's steps throughout the game.
     */
    public void saveReplayToFile(Deque<Step> steps) {
        try {
            File replayFile = new File(FILEPATH);
            FileWriter writer = new FileWriter(replayFile);

            if (this.replays == null) {
                this.replays = new ArrayList<Replay>();
            }
            this.replays.add(new Replay(steps));
            writer.write(gson.toJson(this.replays));
            writer.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    /**
     * Retrieves the game logs from the replay log file.
     * 
     * @return A list of all replays.
     */
    public ArrayList<Replay> readReplaysFromFile() {
        try {
            File replayFile = new File(FILEPATH);
            if (replayFile.exists() && !replayFile.isDirectory()) {
                FileReader reader = new FileReader(replayFile);
                this.replays = gson.fromJson(new JsonReader(reader), REPLAY_TYPE);
                reader.close();
            } else {
                this.replays = null;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return this.replays;
    }
}