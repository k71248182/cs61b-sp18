package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private Random random;

    private void stdDrawSetup() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    /** Display main menu. */
    private void mainMenu() {
        StdDraw.clear(Color.BLACK);
        gameTitle();
        startingMenu();
        StdDraw.show();
    }

    private void askForSeed(String seedString) {
        StdDraw.clear(Color.BLACK);
        gameTitle();
        askingForSeed(seedString);
        StdDraw.show();
    }

    private void gameTitle() {
        Font bigFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.5 * WIDTH, 0.6 * HEIGHT, "CS61B: THE GAME");
    }

    private void startingMenu() {
        Font smallFont = new Font("Arial", Font.BOLD, 16);
        StdDraw.setFont(smallFont);
        StdDraw.text(0.5 * WIDTH, 0.35 * HEIGHT, "New Game (N)");
        StdDraw.text(0.5 * WIDTH, 0.3 * HEIGHT, "Load Game (L)");
        StdDraw.text(0.5 * WIDTH, 0.25 * HEIGHT, "Quit (Q)");
    }

    private void askingForSeed(String seedString) {
        Font smallFont = new Font("Arial", Font.BOLD, 16);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(0.5 * WIDTH, 0.3 * HEIGHT,
                "Please type in a seed (any integer): " + seedString);
        if (seedString.length() > 0) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.5 * WIDTH, 0.15 * HEIGHT,
                    "Press S to start the game.");
        }
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        stdDrawSetup();
        mainMenu();
        ArrayList<Character> validMainMenuInputs = validMainMenuInputs();
        char userInput = receiveInputKey(validMainMenuInputs);
        processMainMenuInput(userInput);
    }

    private ArrayList<Character> validMainMenuInputs() {
        ArrayList<Character> keys = new ArrayList<>();
        keys.add('N');
        keys.add('n');
        keys.add('L');
        keys.add('l');
        keys.add('Q');
        keys.add('q');
        return keys;
    }

    private ArrayList<Character> validSeedInputs() {
        ArrayList<Character> keys = new ArrayList<>();
        keys.add('S');
        keys.add('s');
        keys.add('0');
        keys.add('1');
        keys.add('2');
        keys.add('3');
        keys.add('4');
        keys.add('5');
        keys.add('6');
        keys.add('7');
        keys.add('8');
        keys.add('9');
        return keys;
    }

    /** Return one valid input key from the user.
     * Only characters in the provided list will be recognized. */
    private char receiveInputKey(ArrayList<Character> validInputs) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (validInputs.contains(key)) {
                return key;
            } else {
                continue;
            }
        }
    }

    /** Process one input key. */
    private void processMainMenuInput(char key) {
        if (key == 'N' || key == 'n') {
            long seed = receiveInputSeed();
            random = new Random(seed);
            displayWorld();
        } else if (key == 'L' || key == 'l') {
            return;
        } else if (key == 'Q' || key == 'q') {
            return;
        }
    }

    /** Read the seed from user inputs. */
    private long receiveInputSeed() {
        String seedString = "";
        boolean startGameButton = false;
        ArrayList<Character> validSeedInputs = validSeedInputs();

        // Display a message to user, asking for the seed.
        askForSeed(seedString);

        // Read all integer inputs before the user press "S".
        while (!startGameButton) {
            char userInput = receiveInputKey(validSeedInputs);
            if (userInput == 's' || userInput == 'S') {
                startGameButton = true;
            } else {
                seedString += userInput;
                askForSeed(seedString);
            }
        }

        // If no seed has been provided, repeat the process. Otherwise return seed.
        if (seedString.length() == 0) {
            return receiveInputSeed();
        } else {
            long seed = Long.parseLong(seedString);
            return seed;
        }
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
        // Fill out this method to run the game using the input passed in,
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
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = newWorld();
        ter.renderFrame(world);
    }
}
