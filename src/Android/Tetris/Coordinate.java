package Android.Tetris;



/**
 * Represents integer cartesian coordinates.
 * @author Zach Cotter
 */
public class Coordinate{
    private int x;
    private int y;

    /**
     * Creates a new coordinate (-1,-1).
     */
    public Coordinate() {
        x = -1;
        y = -1;
    }

    /**
     * Creates a new coordinate (x,y)
     * @param x int representing x value of new coordinate
     * @param y int representing y value of new coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Accessor for x value
     * @return int representing x value of this
     */
    public int getX() {
        return x;
    }

    /**
     * Accessor for y value
     * @return int representing y value of this
     */
    public int getY() {
        return y;
    }

    /**
     * Mutator for x value
     * @param x int representing new x value of this
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Mutator for y value
     * @param y int representing new y value of this
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns a String representing this.
     * @return A String representation of this coordinate: (x,y)
     */
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    /**
     * Determines if this is equivalent to the given object, if it is even an
     * instanceof Coordinate
     * @param obj Object to be compared to this.
     * @return Whether or not the objects are equivalent
     */
    public boolean equals(Object obj) {
        if(obj instanceof Coordinate){
            Coordinate o = (Coordinate) obj;
            return getX() == o.getX() && getY() == o.getY();
        }
        return false;
    }
}