package byog.Core;

import java.io.Serializable;


public class VerticalHallway extends Shapes implements Serializable {

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
