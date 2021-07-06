package byog.lab5;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import javax.naming.ldap.ExtendedRequest;
import javax.swing.tree.FixedHeightLayoutCache;
import java.util.ArrayList;

/** A hexagon that has basic properties
 * such as size and starting position. */
public class Hexagon {
    public final Position position;
    public final int size;
    private final int height;
    private final int width;

    /** Constructor */
    public Hexagon(int s, Position p) {
        size = s;
        height = size * 2;
        width = 3 * size - 2;
        position = p;
    }

    /** Return rectangle tiles that contains the hexagon.
     * The hexagon will be filled with the same tile type.
     * @return rectangle tiles with hexagon in it.
     */
    public TETile[][] draw(TETile tile) {
        TETile[][] rectangleTiles = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (isHexagonTile(x, y)) {
                    rectangleTiles[x][y] = tile;
                } else {
                    rectangleTiles[x][y] = Tileset.NOTHING;
                }
            }
        }
        return rectangleTiles;
    }

    /** Helper function to decide whether a tile in the
     * rectangle should be filled for drawing the hexagon.
     * @param x x position relative to the starting position
     * @param y y position relative to the starting position
     * @return whether the tile belongs to the hexagon
     */
    private boolean isHexagonTile(int x, int y) {
        // bottom left corner
        if (x + y < this.size - 1) {
            return false;
        }

        // bottom right corner
        if (x - y > this.width - size) {
            return false;
        }

        // top left corner
        if (y - x > this.height - size) {
            return false;
        }

        // top right corner
        if (x + y > this.width + this.height - this.size - 1) {
            return false;
        }

        return true;
    }

    /** Return a list of six adjacent hexagons. */
    public ArrayList<Hexagon> adjacentHexagons() {

        int xLeft = position.x - (size + size - 1);
        int xMiddle = position.x;
        int xRight = position.x + (size + size - 1);

        int yTop = position.y + height;
        int yUpper = position.y + size;
        int yLower = position.y - size;
        int yBottom = position.y - height;

        Position top = new Position(xMiddle, yTop);
        Position upperLeft = new Position(xLeft, yUpper);
        Position lowerLeft = new Position(xLeft, yLower);
        Position bottom = new Position(xMiddle, yBottom);
        Position lowerRight = new Position(xRight, yLower);
        Position upperRight = new Position(xRight, yUpper);

        ArrayList<Hexagon> adjacentHexagons = new ArrayList<Hexagon>();
        adjacentHexagons.add(new Hexagon(size, top));
        adjacentHexagons.add(new Hexagon(size, upperLeft));
        adjacentHexagons.add(new Hexagon(size, lowerLeft));
        adjacentHexagons.add(new Hexagon(size, bottom));
        adjacentHexagons.add(new Hexagon(size, lowerRight));
        adjacentHexagons.add(new Hexagon(size, upperRight));

        return adjacentHexagons;
    }

    /** Return true if the hexagon already exists in the list. */
    public boolean exist(ArrayList<Hexagon> hexagons) {
        for(Hexagon hexagon : hexagons) {
            if (this.equal(hexagon)) {
                return true;
            }
        }
        return false;
    }

    /** Return true if the given hexagon has the
     * exact same size and position.
     * @param hexagon
     * @return
     */
    private boolean equal(Hexagon hexagon) {
        if (size == hexagon.size &&
                position.x == hexagon.position.x &&
                position.y == hexagon.position.y) {
            return true;
        }
        return false;
    }

    /** Return true if the hexagon can fit in a specified area.
     * To fit in, the position cannot be negative, and the hexagon
     * shall not go beyond the maximum height or width.
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public boolean fitIn(int maxWidth, int maxHeight) {
        if (position.x < 0 ||
                position.y < 0 ||
                position.x + width > maxWidth ||
                position.y + height > maxHeight) {
            return false;
        }
        return true;
    }

}
