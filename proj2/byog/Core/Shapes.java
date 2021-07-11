package byog.Core;

/** An abstract shape class that may have subclasses
 * such as rooms, walls, and hallways.
 * All shapes in this case are rectangles.
 */
public abstract class Shapes {

    protected static final int MAXHALLWAY = 10;
    Position position;
    int height;
    int width;
    protected Wall[] walls;

    /** Return the height of the shape. */
    public int getHeight() {
        return height;
    }

    /** Return the width of the shape. */
    public int getWidth() {
        return width;
    }

    /** Return the position of the shape. */
    public Position getPosition() { return position; }

}
