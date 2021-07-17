package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final String SAVEFILE = "save.ser";
    private Random random;
    private String validMainMenuInputs = "NLQnlq";
    private String validSeedInputs = "0123456789Ss";
    private String validMoveInputs = "ASDWasdwQq";

    /** A class for main menu display */
    private static class MainMenu {

        /** Set up the canvas size */
        public static void setupMainMenuSize() {
            StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
        }

        /** Display main menu.
         * Game title
         * Game menu */
        public static void displayMainMenu() {
            StdDraw.clear(Color.BLACK);
            gameTitle();
            startingMenu();
            StdDraw.show();
        }

        /** Display seed information in the main menu.
         * Game title
         * Ask the user to provide a seed.
         * @param seedString
         */
        public static void displaySeedInfo(String seedString) {
            StdDraw.clear(Color.BLACK);
            gameTitle();
            askingForSeed(seedString);
            StdDraw.show();
        }

        /** Game title: format and content */
        private static void gameTitle() {
            Font bigFont = new Font("Arial", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.5 * WIDTH, 0.6 * HEIGHT, "CS61B: THE GAME");
        }

        /** Starting menu selections: format and content */
        private static void startingMenu() {
            Font smallFont = new Font("Arial", Font.BOLD, 16);
            StdDraw.setFont(smallFont);
            StdDraw.text(0.5 * WIDTH, 0.35 * HEIGHT, "New Game (N)");
            StdDraw.text(0.5 * WIDTH, 0.3 * HEIGHT, "Load Game (L)");
            StdDraw.text(0.5 * WIDTH, 0.25 * HEIGHT, "Quit (Q)");
        }

        /** Message to user asking for a seed:
         * If a number is provided, show it on the screen
         * and inform the user that "S" will start the game.
         * @param seedString
         */
        private static void askingForSeed(String seedString) {
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

    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        MainMenu.setupMainMenuSize();
        MainMenu.displayMainMenu();
        processMainMenuInput();
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

        String seedString = input.substring(1, input.length() - 1);
        long seed = Long.parseLong(seedString);
        random = new Random(seed);
        //TETile[][] finalWorldFrame = newWorld();

        //return finalWorldFrame;
        return null;
    }

    /** Return one valid input key from the user.
     * Only characters in the provided list will be recognized. */
    private char receiveInputKey(String validInputs) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            String keyString = String.valueOf(key);
            if (validInputs.contains(keyString)) {
                return key;
            } else {
                continue;
            }
        }
    }

    /** Process one input key for the main menu.
     * N to start a new game.
     * L to load existing game.
     * Q to quit. */
    private void processMainMenuInput() {
        //ArrayList<Character> validMainMenuInputs = validMainMenuInputs();
        char key = receiveInputKey(validMainMenuInputs);
        // Convert lower case char to upper case.
        key = Character.toUpperCase(key);
        switch (key) {
            case 'N': {
                long seed = receiveInputSeed();
                random = new Random(seed);
                Canvas canvas = newRandomWorld();
                displayWorld(canvas);
            }
            case 'L': {
                Canvas canvas = loadGame();
                if (canvas != null) {
                    displayWorld(canvas);
                }
            }
            case 'Q': System.exit(0);
            default: return;
        }
    }

    /** Read the seed from user inputs. */
    private long receiveInputSeed() {
        String seedString = "";
        boolean startGameButton = false;

        // Display a message to user, asking for the seed.
        MainMenu.displaySeedInfo(seedString);

        // Read all integer inputs before the user press "S".
        while (!startGameButton) {
            char userInput = receiveInputKey(validSeedInputs);
            if (userInput == 's' || userInput == 'S') {
                startGameButton = true;
            } else {
                seedString += userInput;
                MainMenu.displaySeedInfo(seedString);
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

    /** Display the world and allow the player to move around in it.*/
    private void displayWorld(Canvas canvas) {
        // Display the world
        ter.initialize(WIDTH, HEIGHT + 2, 0, 1);
        TETile[][] world = canvas.getTiles();
        ter.renderFrame(world);

        // Allow player movements.
        while (true) {
            char key = receiveInputKey(validMoveInputs);
            key = Character.toUpperCase(key);
            if (key == 'Q') {
                saveGame(canvas);
                System.exit(0);
            }
            canvas.moveOneStep(key);
            ter.renderFrame(canvas.getTiles());
        }
    }

    /** Save the game(canvas). */
    private void saveGame(Canvas canvas) {
        try {
            FileOutputStream file = new FileOutputStream(SAVEFILE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(canvas);
            out.close();
            file.close();
            System.out.println("Game has been saved.");
        }
        catch (IOException ex) {
            System.out.println("IOException is caught.");
        }
    }

    /** Load a previous saved world. */
    private Canvas loadGame() {
        try {
            FileInputStream file = new FileInputStream(SAVEFILE);
            ObjectInputStream in = new ObjectInputStream(file);
            Canvas canvas = (Canvas) in.readObject();
            in.close();
            file.close();
            System.out.println("Saved game is loaded.");
            return canvas;
        }
        catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            return null;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("IOException is caught.");
            return null;
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught.");
            return null;
        }
    }

    /** Create a new random world and add a player to it. */
    private Canvas newRandomWorld() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT, random);
        canvas.createWorld();
        canvas.addPlayer();
        return canvas;
    }
}
