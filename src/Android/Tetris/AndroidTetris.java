package Android.Tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Zach Cotter
 */
public class AndroidTetris extends Activity {

    /** Called when the activity is first created. */
    private TetrisView v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_over);
    }

    @Override
    public void onBackPressed() {
        homeButtonPressed(null);
    }

    public void startButtonPressed(View view) {
        v = new TetrisView(this,
                           getWindowManager().getDefaultDisplay().getWidth(),
                           getWindowManager().getDefaultDisplay().getHeight());

        setContentView(v);
        v.invalidate();
    }

    public void highScoreButtonPressed(View view) {
        setContentView(R.layout.high_scores);
        TextView t = (TextView) findViewById(R.id.high);
        HighScore table = new HighScore();
        t.setText(table.toString());
        t.invalidate();
    }

    public void controlsButtonPressed(View view) {
        setContentView(R.layout.controls);
    }

    public void homeButtonPressed(View view) {
        setContentView(R.layout.menu);
    }
    private int score;

    public void newEntryButtonPressed(View view) {
        EditText t = (EditText) this.findViewById(R.id.field);
        CharSequence name = t.getText();
        HighScore.postScore(new HighScoreEntry(name.toString(),
                                               score));
        setContentView(R.layout.menu);
    }

    public void gameOver(int highScore) {
        score = highScore;
        if (highScore == 0) {
            setContentView(R.layout.game_over);
        }
        else {
            setContentView(R.layout.new_high_score);
        }
    }
}
