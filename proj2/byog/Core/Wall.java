package byog.Core;

import java.io.Serializable;

/** Wall class represented by rectangles with width == 1
 * or height == 1 */
public class Wall extends Shapes implements Serializable {

    /** Constructor */
    public Wall(int width, int height, Position p) {
        this.height = height;
        this.width = width;
        position = p;
    }

}
