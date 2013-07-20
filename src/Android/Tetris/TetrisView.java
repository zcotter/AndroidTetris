package Android.Tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

/**
 * @author Zach Cotter
 */
public class TetrisView extends View {

    public static final int GRID_HEIGHT = 20;
    public static final int GRID_WIDTH = 10;
    private static final int TIMER_DELAY_DECREMENT_PER_THOUSAND_VALUE = 20;
    private static final int POINTS_PER_BLOCK = 10;
    private static final int POINTS_PER_TOTAL_CLEAR = 500;
    private static final int POINTS_PER_CLEAR = 100;
    public static int panelHeight;
    public static int panelWidth;
    public static int middle;
    public static int blockHeight;
    public static int blockWidth;
    private boolean gameOver;
    private boolean gameInProgress;
    private boolean paused;
    private Tetra current;
    private Block[][] grid;
    private TetrisCountdownTimer timer;
    private Context theContext;
    private int score;
    private int lastThousandForScore;
    private int timerCountDown;

    /**
     * Android's implementation of timer is a little different so I had to make
     * a few changes.
     */
    private class TetrisCountdownTimer extends CountDownTimer {

        public TetrisCountdownTimer(long millisInFuture,
                                    long countDownInterval) {
            super(millisInFuture,
                  countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            step();
        }

        @Override
        public void onFinish() {
            this.cancel();
            initTimer();
        }
    }

    private void initTimer() {
        timer = new TetrisCountdownTimer(Long.MAX_VALUE,
                                         timerCountDown);
        timer.start();
    }

    private void step() {
        if (!paused) {
            attemptToMoveCurrent(0);
            checkGameOver();
            invalidate();
        }
    }

    public TetrisView(Context context,
                      int xSize,
                      int ySize) {

        super(context);
        theContext = context;
        panelHeight = ySize;
        panelWidth = xSize;
        gameInProgress = false;
        paused = false;
        gameOver = false;
        timerCountDown = 800;
        middle = panelWidth / 2;
        blockHeight = panelHeight / GRID_HEIGHT;
        blockWidth = panelWidth / GRID_WIDTH;
        invalidate();
        current = generateTetra();
        initTimer();
        grid = new Block[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                grid[x][y] = null;
            }
        }
        score = 0;
        lastThousandForScore = 0;

    }
    
    private float downX;
    private float downY;

    public void pause() {
        paused = true;
        timer = null;
    }

    /**
     * Continues the game on next paint
     */
    public void unpause() {
        paused = false;
        timer = new TetrisCountdownTimer(Long.MAX_VALUE,
                                         timerCountDown);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY();
        }
        else if (action == MotionEvent.ACTION_UP) {
            float iX = downX;
            float fX = event.getX();
            float iY = downY;
            float fY = event.getY();
            if (Math.abs(iX - fX) < 40) {
                //check if down swipe
                if (Math.abs(fY - iY) > 100) {
                    attemptToMoveCurrentToMaximumDownwardPosition();
                }
                else {
                    if (fX > middle) {
                        attemptToRotateCurrent(true);
                    }
                    else if (fX < middle) {
                        attemptToRotateCurrent(false);
                    }
                }
            }
            else if (iX < fX) {
                attemptToMoveCurrent(1);
            }
            else {
                attemptToMoveCurrent(-1);
            }
            invalidate();
        }
        return true;
    }

    private boolean attemptToMoveCurrentToMaximumDownwardPosition() {
        boolean keepGoing = true;
        while (keepGoing) {
            keepGoing = attemptToMoveCurrent(0);
        }
        return keepGoing;
    }

    private boolean attemptToRotateCurrent(boolean clockwise) {
        if (clockwise) {
            current.rotateClockwise();
            boolean overlap = checkIfCurrentIntersectsPile();
            if (overlap) {
                current.rotateCounterClockwise();
            }
        }
        else {
            current.rotateCounterClockwise();
            boolean overlap = checkIfCurrentIntersectsPile();
            if (overlap) {
                current.rotateClockwise();
            }
        }
        return true;
    }

    private boolean checkIfCurrentIntersectsPile() {
        boolean overlap = false;
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                if (grid[x][y] != null) {
                    for (Block b : current.getTetra()) {
                        if (b.equals(grid[x][y])) {
                            overlap = true;
                            break;
                        }
                    }
                }
                if (overlap == true) {
                    break;
                }
            }
            if (overlap == true) {
                break;
            }
        }
        return overlap;
    }

    private Tetra generateTetra() {
        Random generator = new Random();
        int random = generator.nextInt(Tetra.NUMBER_OF_TETRAS);
        return new Tetra(Tetra.TETRA_IDENTIFIERS[random]);
    }

    public static boolean inbounds(int x,
                                   int y) {
        return ((x >= 0)
                && (x < TetrisView.GRID_WIDTH)
                && (y >= 0)
                && (y < TetrisView.GRID_HEIGHT));
    }

    private boolean checkGameOver() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            if (grid[x][0] != null) {
                gameOver = true;
                pause();
                gameInProgress = false;
                ((AndroidTetris)theContext).gameOver(checkHighScorer());
                return true;
            }
        }
        return false;
    }
    
    private int checkHighScorer() {
        HighScore table = new HighScore();
        if(score > table.getValueToQualify()){
            return score;
        }
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(20);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0,
                                 0,
                                 panelWidth,
                                 panelHeight),
                        p);
        if (gameOver) {
            
            return;
        }
        
        current.paint(canvas);

        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Block b = grid[x][y];
                if (b != null) {
                    b.paint(canvas);
                }
            }
        }
        
        int thousands = score / 1000;
        if (thousands > lastThousandForScore) {
            lastThousandForScore = thousands;
            timerCountDown -= TIMER_DELAY_DECREMENT_PER_THOUSAND_VALUE;
            timer = new TetrisCountdownTimer(Long.MAX_VALUE,
                                             timerCountDown);
            timer.start();
        }

        canvas.drawText(score + "",
                        5,
                        15,
                        textPaint);
    }

    /**
     * Abstracts singular horizontal and vertical movements of the Tetra
     * currently in motion. Parameter indicates lateral offset of target
     * location. A movement is made if and only if all Blocks of the Tetra
     * currently in motion can complete the motion in the same direction.
     * @param direction int representing lateral offset of target location
     * (ie -1==left, 0==down, 1==right)
     * @return whether or not movement was successful
     */
    private boolean attemptToMoveCurrent(int direction) {
        if (direction == 5) {
            return false;
        }
        boolean possible = true;
        boolean putInPile = false;
        /*
         * For each block in the tetra, the new x and y positions are
         * determined by separate algorithms.
         */
        for (Block b : current.getTetra()) {
            int currentX = b.getX();
            int currentY = b.getY();
            int targetX = currentX + direction;
            int targetY = -1;
            if (direction == 0) {
                targetY = currentY + 1;
            }
            else {
                targetY = currentY;
            }
            if (TetrisView.inbounds(targetX,
                                    targetY)) {
                boolean filled = (grid[targetX][targetY] != null);
                if (filled) {
                    possible = false;
                    if (direction == 0) {
                        putInPile = true;
                    }
                }

            }
            else {
                possible = false;
            }
            if (!((targetY >= 0) && (targetY < 20))) {
                putInPile = true;
            }
        }
        if (possible) {
            if (direction == 0) {
                current.moveDown();
            }
            if (direction == 1) {
                current.moveRight();
            }
            if (direction == -1) {
                current.moveLeft();
            }
            return true;
        }
        else if (putInPile) {
            addCurrentToPile();
            current = generateTetra();
            invalidate();
            return false;
        }
        else {
            return false;
        }
    }

    /**
     * Adds the current Tetra to the pile, then processes the pile.
     */
    private void addCurrentToPile() {
        for (Block b : current.getTetra()) {
            grid[b.getX()][b.getY()] = b;
            score += POINTS_PER_BLOCK;
        }
        dumpFullRows();
        checkGridEmptyForScore();
        invalidate();
    }

    private void checkGridEmptyForScore() {
        boolean emptyGrid = true;
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] != null) {
                    emptyGrid = false;
                    break;
                }
            }
            if (!emptyGrid) {
                break;
            }
        }
        if (emptyGrid) {
            score += POINTS_PER_TOTAL_CLEAR;
        }
    }

    /**
     * Erases rows of the grid that are full and moves rows above down as needed.
     */
    private void dumpFullRows() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            boolean rowFull = true;
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] == null) {
                    rowFull = false;
                    break;
                }
            }
            if (rowFull) {
                deleteRow(y);
                score += POINTS_PER_CLEAR;
            }
        }
    }

    /**
     * Deletes the given row and moves all rows above it down by one.
     * @param row int representing row to delete.
     */
    private void deleteRow(int row) {
        for (int x = 0; x < GRID_WIDTH; x++) {
            grid[x][row] = null;
        }
        shiftAllAboveDownOne(row);
    }

    /**
     * Shifts all rows above the given row down one.
     * @param row int representing highest row that is not moved down.
     */
    private void shiftAllAboveDownOne(int row) {
        for (int y = row; y >= 0; y--) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y] != null) {
                    grid[x][y].moveDown();
                    grid[x][y + 1] = grid[x][y];
                    grid[x][y] = null;
                }
            }
        }
    }
}
