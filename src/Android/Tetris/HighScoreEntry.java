package Android.Tetris;

/**
 *
 * @author Zach Cotter
 */
public class HighScoreEntry {

    private String name;
    private int score;

    public HighScoreEntry(String name,
                          int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }
}
