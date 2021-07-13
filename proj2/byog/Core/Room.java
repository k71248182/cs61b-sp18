package byog.Core;

/** Room class represented by rectangles.
 * A room has four walls. */
public class Room extends Shapes {

    protected static final int MAX = 8;
    protected static final int MIN = 4;

    /** Constructor */
    public Room(int width, int height, Position p) {

        this.height = height;
        this.width = width;
        position = p;

        walls = new Wall[4];
        walls[0] = new Wall(1, height, p);
        walls[1] = new Wall(width, 1, p);
        walls[2] = new Wall(1, height, p.shift(width - 1, 0));
        walls[3] = new Wall(width, 1, p.shift(0, height - 1));
    }

    public int numExit() {
        return 0;
    }
}
