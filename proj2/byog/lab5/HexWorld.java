package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 40;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /** Return TETiles 2D-array that covers the whole canvas. */
    private static TETile[][] drawBackground() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithSameTiles(world, "floor");
        return world;
    }

    /**
     * Add a hexagon to the canvas if there is room.
     * @param hexagon the hexagon that you want to add.
     * @param canvas current tiles on the canvas
     */
    private static void addHexagon(Hexagon hexagon, TETile[][] canvas) {
        // Decide the tile type of this hexagon.
        TETile tile = randomTile();
        TETile[][] tilesHexagon = hexagon.draw(tile);
        if (!hexagon.fitIn(WIDTH, HEIGHT)) {
            return;
        }
        if (alreadyDrawn(canvas, tilesHexagon, hexagon.position)) {
            return;
        }
        System.out.println("Draw: " + hexagon.position.x +
                " " + hexagon.position.y);
        overwriteTiles(canvas, tilesHexagon, hexagon.position);
    }

    /** Add a tesselation of hexagons to canvas. */
    private static void addHexagons(Hexagon firstHexagon, TETile[][] canvas) {
        ArrayList<Hexagon> tesselation = hexagonTesselation(firstHexagon);
        for (Hexagon hexagon : tesselation) {
            addHexagon(hexagon, canvas);
        }
    }

    /** Create a tesselation of hexagons by expanding from the
     * first given hexagon. Return the list of hexagons.
     * Starting from the most adjacent ones.
     * Once a new hexagon is added, find the adjacent hexagons
     * of the added one, and so on. */
    private static ArrayList<Hexagon> hexagonTesselation(Hexagon firstHexagon) {

        ArrayList<Hexagon> centerHexagons = new ArrayList<>();
        ArrayList<Hexagon> uniqueHexagons = new ArrayList<>();

        centerHexagons.add(firstHexagon);
        uniqueHexagons.add(firstHexagon);

        for(int i = 0; i < centerHexagons.size(); i += 1) {
            ArrayList<Hexagon> adjacent = centerHexagons.get(i).adjacentHexagons();
            for (Hexagon h : adjacent) {
                if (h.exist(uniqueHexagons)) {
                    //System.out.println("Already added: " + h.position.x + " " + h.position.y);
                } else if (!h.fitIn(WIDTH, HEIGHT)) {
                    //System.out.println("Out of scope: " + h.position.x + " " + h.position.y);
                } else {
                    uniqueHexagons.add(h);
                    centerHexagons.add(h);
                    //System.out.println("added " + h.position.x + " " + h.position.y);
                }
            }
        }
        return uniqueHexagons;
    }

    /**
     * Fills the given 2D array of tiles with
     * the same given tile type. */
    private static void fillWithSameTiles(TETile[][] tiles, String tileType) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (tileType.equals("flower")) {
                    tiles[x][y] = Tileset.FLOWER;
                } else {
                    tiles[x][y] = Tileset.NOTHING;
                }
            }
        }
    }

    /** Update a part of the tiles with with provided tiles.
     * Need to provide the starting positions (relative
     * to the original tiles).
     * 0 position starts from the bottom left corner.
     * Tiles with nothing are considered transparent.
     * This method is destructive.
     * @param originalTiles original canvas 2D-array
     * @param overwriteTiles 2D array tiles to overwrite
     * @param startPosition starting position on the canvas
     */
    private static void overwriteTiles(TETile[][] originalTiles,
                                       TETile[][] overwriteTiles,
                                       Position startPosition) {
        for (int x = 0; x < overwriteTiles.length; x += 1) {
            for (int y = 0; y < overwriteTiles[0].length; y += 1) {
                if (!overwriteTiles[x][y].description().equals("nothing")) {
                    originalTiles[x + startPosition.x][y + startPosition.y] =
                            overwriteTiles[x][y];
                }
            }
        }
    }

    /** Return true if something already exists in the area
     * where you want to draw something. */
    private static boolean alreadyDrawn(TETile[][] originalTiles,
                                TETile[][] overwriteTiles,
                                Position startPosition) {
        for (int x = 0; x < overwriteTiles.length; x += 1) {
            for (int y = 0; y < overwriteTiles[0].length; y += 1) {
                if (!overwriteTiles[x][y].description().equals("nothing") &&
                        !originalTiles[x + startPosition.x][y + startPosition.y]
                                .description().equals("nothing")) {
                     return true;
                }
            }
        }
        return false;
    }

    /** Return true if something you want to draw can fit in the canvas. */
    private static boolean withinScope(TETile[][] canvas,
                                      TETile[][] overwriteTiles,
                                      Position startPosition) {
        if (startPosition.x < 0 ||
                startPosition.y < 0 ||
                startPosition.x + overwriteTiles.length > canvas.length ||
                startPosition.y + overwriteTiles[0].length > canvas[0].length) {
            return false;
        }
        return true;
    }

    /** Picks a RANDOM tile */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.SAND;
            case 5: return Tileset.TREE;
            case 6: return Tileset.WATER;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {

        // initialize a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // draw background
        TETile[][] canvas = drawBackground();

        // draw hexagons
        int size = 3;
        Position startPosition = new Position(20, 20);
        Hexagon firstHexagon = new Hexagon(size, startPosition);
        addHexagons(firstHexagon, canvas);

        ter.renderFrame(canvas);

    }

}
