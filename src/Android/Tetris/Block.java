package Android.Tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents a Tetris block by position in the grid and color. Includes functions
 * to move the block within a hypothetical grid.
 * @author Zach Cotter
 */
public class Block {

    private int x;
    private int y;
    private int color;

    /**
     * Constructs a test block
     */
    public Block() {
        x = -1;
        y = -1;
        color = Color.BLACK;
    }

    /**
     * Constructs a block using the given parameters
     * @param x int representing x position of block in grid
     * @param y int representing y position of block in grid
     * @param color Color representing the color of the block.
     */
    public Block(int x,
                 int y,
                 int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Accesses x position of block in grid
     * @return int representing x position of block in grid
     */
    public int getX() {
        return x;
    }

    /**
     * Accesses y position of block in grid
     * @return int representing y position of block in grid
     */
    public int getY() {
        return y;
    }

    /**
     * Mutates the x position of the block in the grid
     * @param x int representing x position of block in grid
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Mutates the y position of the block int the grid
     * @param y int representing x position of block in grid
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Mutates color of block.
     * @param color Color of block.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Accesses color of block
     * @return Color of block
     */
    public int getColor() {
        return color;
    }

    /**
     * Moves the block down a row if the result is inbounds
     * @return whether or not the piece was moved
     */
    public boolean moveDown() {
        if (TetrisView.inbounds(x,
                                y + 1)) {
            y += 1;
            return true;
        }
        return false;
    }

    /**
     * Moves the block right a column if the result is inbounds
     * @return whether or not the piece was moved.
     */
    public boolean moveRight() {
        if (TetrisView.inbounds(x + 1,
                                y)) {
            x += 1;
            return true;
        }
        return false;
    }

    /**
     * Moves the block left a column if the result is inbounds
     * @return whether or not the piece was moved.
     */
    public boolean moveLeft() {
        if (TetrisView.inbounds(x - 1,
                                y)) {
            x -= 1;
            return true;
        }
        return false;
    }

    /**
     * Recursively calls moveDown() until it returns false.
     */
    public void moveDownToLowestPossible() {
        boolean keepGoing = false;
        while (keepGoing) {
            keepGoing = moveDown();
        }
    }

    /**
     * determines if this block is inbounds
     * @return Whether or not this block is currently inbounds
     */
    public boolean inbounds() {
        return TetrisView.inbounds(x,
                                   y);
    }

    /**
     * Rotates this block counter clockwise around the given grid space, if the
     * result is inbounds
     * @param centerPoint Coordinate of axis of rotation
     * @return whether or not the rotation was successful
     */
    public boolean rotateCCW(Coordinate centerPoint) {
        int targetX = centerPoint.getX() + (centerPoint.getY() - this.getY());
        int targetY = centerPoint.getY() + (this.getX() - centerPoint.getX());
        if (TetrisView.inbounds(targetX,
                                targetY)) {
            x = targetX;
            y = targetY;
            return true;
        }
        return false;
    }

    /**
     * Determines if this block can rotate counter clockwise around the given
     * grid space
     * @param centerPoint Coordinate of axis of rotation
     * @return whether or not the rotation would be successful
     */
    public boolean canRotateCCW(Coordinate centerPoint) {
        int targetX = centerPoint.getX() + (centerPoint.getY() - this.getY());
        int targetY = centerPoint.getY() + (this.getX() - centerPoint.getX());
        if (TetrisView.inbounds(targetX,
                                targetY)) {
            return true;
        }
        return false;
    }

    /**
     * Rotates this block clockwise around the given grid space, if the
     * result is inbounds
     * @param centerPoint Coordinate of axis of rotation
     * @return whether or not the rotation was successful
     */
    public boolean rotateCW(Coordinate centerPoint) {
        final int oldX = getX();
        final int oldY = getY();
        boolean keepTurning = rotateCCW(centerPoint);
        if (keepTurning) {
            keepTurning = rotateCCW(centerPoint);
        }
        if (keepTurning) {
            keepTurning = rotateCCW(centerPoint);
        }
        if (!keepTurning) {
            x = oldX;
            y = oldY;
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Determines if this block can rotate clockwise around the given
     * grid space
     * @param centerPoint Coordinate of axis of rotation
     * @return whether or not the rotation would be successful
     */
    public boolean canRotateCW(Coordinate centerPoint) {
        Block testBlock = new Block(this.getX(),
                                    this.getY(),
                                    this.getColor());
        if (testBlock.canRotateCCW(centerPoint)) {
            testBlock.rotateCCW(centerPoint);
            if (testBlock.canRotateCCW(centerPoint)) {
                testBlock.rotateCCW(centerPoint);
                if (testBlock.canRotateCCW(centerPoint)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Paint getPaint() {
        Paint p = new Paint();
        p.setColor(this.getColor());
        return p;
    }

    /**
     * Determines if the location given is equivalent to the location of this
     * block
     * @param otherX int representing X location of block that is being compared to this.
     * @param otherY int representing Y Location of block that is being compared to this.
     * @return whether or not the positions are equivalent
     */
    public boolean equals(int otherX,
                          int otherY) {
        return otherX == getX() && otherY == getY();
    }

    /**
     * Determines if this block is in the same position as the other Block, if
     * it even is a block
     * @param obj Block to be compared to.
     * @return Whether or not the given object is equivalent to this.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            Block o = (Block) obj;
            return equals(o.getX(),
                          o.getY());
        }
        return false;
    }

    /**
     * Determines if this block is in the same position as the other Block
     * @param other Block to be compared to.
     * @return Whether or not the given block is equivalent to this one.
     */
    public boolean equals(Block other) {
        return other.getX() == getX() && other.getY() == getY();
    }

    /**
     * Returns a String representing the position and color of this block
     * @return String representation of the position and color of this block
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ") " + color;
    }

    void paint(Canvas canvas) {
        int left = x * TetrisView.blockWidth;
        int right = left + TetrisView.blockWidth;
        int top = y * TetrisView.blockHeight;
        int bottom = top + TetrisView.blockHeight;
        canvas.drawRect(new Rect(left,
                                 top,
                                 right,
                                 bottom),
                        getPaint());
    }
}