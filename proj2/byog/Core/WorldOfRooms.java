package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Hexagon;

import java.util.Random;

public class WorldOfRooms {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 40;
    private static final long SEED = 283123;

    public static void main(String[] args) {

        Random random = new Random(SEED);

        // initialize a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // create the canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT, random);
        //canvas.drawBackground();

        canvas.addRandomRooms();
        //canvas.addHorizontalHallways();
        //canvas.addVerticalHallways();
        canvas.addHallways();
        canvas.closeOpenHallways();

        // show the world
        ter.renderFrame(canvas.getTiles());
    }
}
