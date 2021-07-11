package byog.Core;

/** Position class to hold x and y positions */
public class Position {

    protected final int x;
    protected final int y;

    /** Constructor */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Return a new position which is both vertically
     * and horizontally shifted from the current position.
     * Non-destructive method.
     * @param xShift
     * @param yShift
     * @return
     */
    public Position shift(int xShift, int yShift) {
        Position p = new Position(x + xShift, y + yShift);
        return p;
    }
}
