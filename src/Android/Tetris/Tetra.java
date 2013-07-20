package Android.Tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import java.util.HashSet;

/**
 * Represents a Tetra piece for the tetris game.  A Tetra has a Set<Block> representing
 * it's Blocks. It contains methods to move each block in unison as a single
 * cohesive unit. Each Block in the Set is rotated around a defined center
 * Coordinate.
 * @author Zach Cotter
 */
public class Tetra {

    public static final int NUMBER_OF_TETRAS = 7;
    public static final char[] TETRA_IDENTIFIERS = {'o',
                                                    'i',
                                                    'l',
                                                    'j',
                                                    't',
                                                    'z',
                                                    's'};
    private final Block[] O_BLOCKS = {new Block(4,
                                                0,
                                                Color.GREEN),
                                      new Block(5,
                                                0,
                                                Color.GREEN),
                                      new Block(4,
                                                1,
                                                Color.GREEN),
                                      new Block(5,
                                                1,
                                                Color.GREEN)};
    private final Block[] I_BLOCKS = {new Block(3,
                                                0,
                                                Color.BLUE),
                                      new Block(4,
                                                0,
                                                Color.BLUE),
                                      new Block(5,
                                                0,
                                                Color.BLUE),
                                      new Block(6,
                                                0,
                                                Color.BLUE)};
    private final Block[] L_BLOCKS = {new Block(3,
                                                1,
                                                Color.MAGENTA),
                                      new Block(4,
                                                1,
                                                Color.MAGENTA),
                                      new Block(5,
                                                1,
                                                Color.MAGENTA),
                                      new Block(5,
                                                0,
                                                Color.MAGENTA)};
    private final Block[] J_BLOCKS = {new Block(3,
                                                1,
                                                Color.CYAN),
                                      new Block(4,
                                                1,
                                                Color.CYAN),
                                      new Block(5,
                                                1,
                                                Color.CYAN),
                                      new Block(3,
                                                0,
                                                Color.CYAN)};
    private final int ORANGE = Color.rgb(255,
                                         200,
                                         0);
    private final Block[] T_BLOCKS = {new Block(3,
                                                1,
                                                ORANGE),
                                      new Block(4,
                                                1,
                                                ORANGE),
                                      new Block(5,
                                                1,
                                                ORANGE),
                                      new Block(4,
                                                0,
                                                ORANGE)};
    private final int PINK = Color.rgb(255,
                                       175,
                                       175);
    private final Block[] Z_BLOCKS = {new Block(3,
                                                0,
                                                PINK),
                                      new Block(4,
                                                0,
                                                PINK),
                                      new Block(4,
                                                1,
                                                PINK),
                                      new Block(5,
                                                1,
                                                PINK)};
    private final Block[] S_BLOCKS = {new Block(3,
                                                1,
                                                Color.RED),
                                      new Block(4,
                                                0,
                                                Color.RED),
                                      new Block(4,
                                                1,
                                                Color.RED),
                                      new Block(5,
                                                0,
                                                Color.RED)};
    private final Coordinate O_CENTER = new Coordinate(4,
                                                       0);
    private final Coordinate I_CENTER = new Coordinate(4,
                                                       0);
    private final Coordinate L_CENTER = new Coordinate(5,
                                                       1);
    private final Coordinate J_CENTER = new Coordinate(3,
                                                       1);
    private final Coordinate T_CENTER = new Coordinate(4,
                                                       1);
    private final Coordinate Z_CENTER = new Coordinate(4,
                                                       1);
    private final Coordinate S_CENTER = new Coordinate(4,
                                                       1);
    //essential fields
    private HashSet<Block> tetra;
    private Coordinate center;

    /**
     * Constructs a Tetra that has no blocks and has a center at (-1,-1)
     */
    public Tetra() {
        tetra = new HashSet<Block>();
        center = new Coordinate(-1,
                                -1);
    }

    /**
     * Constructs a tetra specific to Tetris. Tetras are identified by a
     * letter that appears similar to the grouping their Blocks.
     * @param identifier char representing predefined target Tetra
     */
    public Tetra(char identifier) {
        if (identifier == 'o') {
            constructorHelper(O_BLOCKS,
                              O_CENTER);
        }
        else if (identifier == 'i') {
            constructorHelper(I_BLOCKS,
                              I_CENTER);
        }
        else if (identifier == 'l') {
            constructorHelper(L_BLOCKS,
                              L_CENTER);
        }
        else if (identifier == 'j') {
            constructorHelper(J_BLOCKS,
                              J_CENTER);
        }
        else if (identifier == 't') {
            constructorHelper(T_BLOCKS,
                              T_CENTER);
        }
        else if (identifier == 'z') {
            constructorHelper(Z_BLOCKS,
                              Z_CENTER);
        }
        else if (identifier == 's') {
            constructorHelper(S_BLOCKS,
                              S_CENTER);
        }
    }

    /**
     * Constructs a new Tetra from the given Blocks and center Coordinate.
     * @param blocks Blocks to be placed in this Tetra's Set<Block>
     * @param center Coordinate representing this tetra's center/axis of rotation
     */
    public Tetra(Block[] blocks,
                 Coordinate center) {
        constructorHelper(blocks,
                          center);
    }

    /**
     * Copy Constructor
     * @param tetra HashSet<Block> for new Tetra
     * @param center Coordinate center of new Tetra
     */
    public Tetra(HashSet<Block> tetra,
                 Coordinate center) {
        this.tetra = tetra;
        this.center = center;
    }

    private void constructorHelper(Block[] blocks,
                                   Coordinate center) {
        tetra = new HashSet<Block>();
        for (Block b : blocks) {
            tetra.add(b);
        }
        this.center = center;
    }

    /**
     * Instantiates a new Tetra with the same values as this. Calls copy
     * constructors of each field with as much depth as necessary to make
     * the new Tetra completely independent.
     * @return a new, identical Tetra
     */
    public Tetra cloneFoRealz() {
        HashSet<Block> cloneSet = new HashSet<Block>();
        for (Block b : tetra) {
            int x = b.getX();
            int y = b.getY();
            int color = b.getColor();

            cloneSet.add(new Block(x,
                                   y,
                                   color));
        }
        return new Tetra(cloneSet,
                         new Coordinate(1000,
                                        1000));
    }

    /**
     * Mutates the color of the blocks in this tetra
     * @param c Color the blocks will change to.
     */
    public void setColor(int c) {
        for (Block b : tetra) {
            b.setColor(c);
        }
    }

    /**
     * Accessor for center Coordinate of this.
     * @return Coordinate representing center/rotation point.
     */
    public Coordinate getCenter() {
        return center;
    }



    /**
     * Mutator for the Coordinate representing the center point of this.
     * @param center Coordinate representing new center point.
     */
    public void setCenter(Coordinate center) {
        this.center = center;
    }

    /**
     * Adds the provided Block to the Set.
     * @param newBlock Block to be added
     */
    public void addBlock(Block newBlock) {
        tetra.add(newBlock);
    }

    /**
     * Removes the provided block from the set, if it is present
     * @param target Block to be removed.
     */
    public void removeBlock(Block target) {
        tetra.remove(target);
    }

    /**
     * Accesses the Set containing this Tetra's Blocks.
     * @return Set<Block> containing this Tetra's Blocks, in no particular order.
     */
    public HashSet<Block> getTetra() {
        return tetra;
    }

    /**
     * Mutates the Set containing this Tetra's Blocks
     * @param tetra The new HashSet.
     */
    public void setTetra(HashSet<Block> tetra) {
        this.tetra = tetra;
    }

    /**
     * Determines if all Blocks owned by this are inbounds
     * @return Whether or not they are all inbounds
     */
    public boolean inbounds() {
        for (Block b : tetra) {
            if (!b.inbounds()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves all of the blocks in this tetra down if and only if they can all
     * be moved down.
     * @return Whether or not a movement could be made.
     */
    public boolean moveDown() {
        HashSet<Block> newBlocks = new HashSet<Block>();
        for (Block b : tetra) {
            boolean success = b.moveDown();
            if (success) {
                newBlocks.add(b);
            }
            else {
                return false;
            }
        }
        this.setTetra(newBlocks);
        this.setCenter(new Coordinate(
                this.getCenter().getX(),
                this.getCenter().getY() + 1));
        return true;
    }

    /**
     * Moves all of the blocks in this tetra left if and only if they can all
     * be moved left.
     * @return Whether or not a movement could be made.
     */
    public boolean moveLeft() {
        Tetra newTetra = new Tetra();
        Block[] temp = tetra.toArray(new Block[1]);
        for (int count = 0; count < temp.length; count++) {
            Block b = temp[count];
            boolean success = b.moveLeft();
            if (success) {
                newTetra.addBlock(b);
            }
            else {
                return false;
            }
        }
        this.setTetra(null);
        this.setTetra(newTetra.getTetra());
        this.setCenter(new Coordinate(
                this.getCenter().getX() - 1,
                this.getCenter().getY()));
        return true;
    }

    /**
     * Moves all of the blocks in this tetra right if and only if they can all
     * be moved right.
     * @return Whether or not a movement could be made.
     */
    public boolean moveRight() {
        Tetra newTetra = new Tetra();
        for (Block b : this.getTetra()) {
            boolean success = b.moveRight();
            if (success) {
                newTetra.addBlock(b);
            }
            else {
                return false;
            }
        }
        this.setTetra(newTetra.getTetra());
        this.setCenter(new Coordinate(
                this.getCenter().getX() + 1,
                this.getCenter().getY()));
        return true;
    }

    /**
     * Determines if all of the blocks in the given Tetra are equivalent (for
     * this games purposes) to this. Equivalent does not necessarily mean they
     * are identical(reference the same memory location)
     * @param other The Tetra to compare this to.
     * @return True if the two Tetras are equivalent.
     */
    public boolean equals(Tetra other) {
        boolean equivalent = true;
        if (other.getTetra().size() != getTetra().size()) {
            return false;
        }
        //make more efficient
        for (Block b : tetra) {
            boolean found = false;
            for (Block otherBlock : tetra) {//FIX THIS
                if (b.equals(otherBlock)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                equivalent = false;
                break;
            }
        }
        return equivalent;
    }

    /**
     * Rotates each Block owned by this Tetra clockwise around the Tetra's
     * center point, if and only if all rotations would be successful.
     */
    public void rotateClockwise() {
        boolean possible = true;
        for (Block b : tetra) {
            if (b.canRotateCW(center) == false) {
                possible = false;
                break;
            }
        }
        if (possible) {
            for (Block b : tetra) {
                b.rotateCW(center);
            }
        }
    }

    /**
     * Rotates each Block owned by this Tetra counter-clockwise around the Tetra's
     * center point, if and only if all rotations would be successful.
     */
    public void rotateCounterClockwise() {
        boolean possible = true;
        for (Block b : tetra) {
            if (b.canRotateCCW(center) == false) {
                possible = false;
                break;
            }
        }
        if (possible) {
            for (Block b : tetra) {
                b.rotateCCW(center);
            }
        }
    }

    /**
     * Returns the maximum y location held by any Block owned by this Tetra.
     * @return int representing max y
     */
    public int findTopBound() {
        int max = 0;
        for (Block b : tetra) {
            if (b.getY() > max) {
                max = b.getY();
            }
        }
        return max;
    }

    /**
     * Returns minumum x location held by any Block owned by this Tetra.
     * @return int representing min x
     */
    public int findLeftBound() {
        int min = 100;
        for (Block b : tetra) {
            if (b.getX() < min) {
                min = b.getX();
            }
        }
        return min;
    }

    /**
     * Returns maximum x location held by any Block owned by this Tetra.
     * @return int representing max x
     */
    public int findRightBound() {
        int max = 0;
        for (Block b : tetra) {
            if (b.getX() > max) {
                max = b.getX();
            }
        }
        return max;
    }

    /**
     * Returns the minimum y location held by any Block owned by this Tetra.
     * @return int representing min y
     */
    public int findBottomBound() {
        int min = 100;
        for (Block b : tetra) {
            if (b.getY() < min) {
                min = b.getY();
            }
        }
        return min;
    }

    /**
     * Creates a String representing this Tetra's fields for testing and
     * error reporting purposes.
     * @return String representation as described above.
     */
    @Override
    public String toString() {
        String toString = "Tetra:\nCenter: " + center.toString() + "\n[\n";
        for (Block b : tetra) {
            toString += b.toString() + "\n";
        }
        return toString + "]";
    }


    public void paint(Canvas canvas) {
        for(Block b : tetra){
            b.paint(canvas);
        }
    }
}