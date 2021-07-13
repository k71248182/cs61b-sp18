package byog.Core;


public class VerticalHallway extends Shapes {

    /** Constructor */
    public VerticalHallway(int length, Position p) {

        height = length;
        width = 3;
        position = p;

        walls = new Wall[2];
        walls[0] = new Wall(1, length, p);
        walls[1] = new Wall(1, length, p.shift(2, 0));

    }

}
