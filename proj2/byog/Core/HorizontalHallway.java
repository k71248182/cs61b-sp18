package byog.Core;

public class HorizontalHallway extends Shapes {

    /** Constructor */
    public HorizontalHallway(int length, Position p) {

        height = 3;
        width = length;
        position = p;

        walls = new Wall[2];
        walls[0] = new Wall(length, 1, p);
        walls[1] = new Wall(length, 1, p.shift(0,2));

    }
}
