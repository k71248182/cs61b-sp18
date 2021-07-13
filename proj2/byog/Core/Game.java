package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private Random random;

    /** Display main menu. */
    public void mainMenu() {

        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16) ;
        Font bigFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5 * WIDTH, 0.6 * HEIGHT, "CS61B: THE GAME");
        Font smallFont = new Font("Arial", Font.BOLD, 16);
        StdDraw.setFont(smallFont);
        StdDraw.text(0.5 * WIDTH, 0.35 * HEIGHT, "New Game (N)");
        StdDraw.text(0.5 * WIDTH, 0.3 * HEIGHT, "Load Game (L)");
        StdDraw.text(0.5 * WIDTH, 0.25 * HEIGHT, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        mainMenu();
        StdDraw.pause(2000);
        random = new Random(123);
        displayWorld();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        String seedString = input.substring(1, input.length() - 1);
        long seed = Long.parseLong(seedString);
        random = new Random(seed);
        TETile[][] finalWorldFrame = newWorld();

        return finalWorldFrame;
    }

    private TETile[][] newWorld() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT, random);
        canvas.createWorld();
        TETile[][] finalWorldFrame = canvas.getTiles();
        return finalWorldFrame;
    }

    private void displayWorld() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = newWorld();
        ter.renderFrame(world);
    }
}
