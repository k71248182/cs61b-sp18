package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.*;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int TOPMARGIN = 1;
    public static final int BOTTOMMARGIN = 2;
    private static final String SAVEFILE = "save.ser";
    protected Canvas canvas;
    protected String currentCursorInfo;
    protected boolean playWithInputString;
    private Random random;
    private String inputString;
    private GameScreen gameScreen;
    private String validMainMenuInputs = "NLQnlq";
    private String validSeedInputs = "0123456789Ss";
    private String validMoveInputs = "ASDWasdwQq:";

    /** Constructor of the Game class. */
    public Game() {
        gameScreen = new GameScreen();
        currentCursorInfo = "";
    }

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

    /** A class for gaming playing display */
    private class GameScreen {

        /** Set up the canvas size.
         * Add top and bottom margin to provide useful information.
         * @param ter
         */
        public static void setup(TERenderer ter) {
            int totalHeight = HEIGHT + TOPMARGIN + BOTTOMMARGIN;
            ter.initialize(WIDTH, totalHeight, 0, BOTTOMMARGIN);
        }

        /** Show canvas */
        public void showCanvas(TERenderer ter) {
            TETile[][] world = canvas.getTiles();
            StdDraw.clear(new Color(0, 0, 0));
            ter.renderFrame(world);
            showStandardInfo();
            showCursorInfo();
            StdDraw.show();
        }

        /** Additional information for the game player */
        private static void showStandardInfo() {
            Font tileFont = StdDraw.getFont();

            StdDraw.setPenColor(StdDraw.PINK);
            Font bottomFont = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(bottomFont);
            StdDraw.textLeft(0, 1, "save and quit (:q)");
            StdDraw.text(0.5 * WIDTH, 1, "← a  ↑ w  → d  ↓ s");

            StdDraw.setFont(tileFont);
        }

        private void showCursorInfo() {
            Font tileFont = StdDraw.getFont();

            StdDraw.setPenColor(StdDraw.PINK);
            Font cursorInfoFond = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(cursorInfoFond);
            StdDraw.textRight(WIDTH, 1, currentCursorInfo);

            StdDraw.setFont(tileFont);
        }

        private String cursorInfo() {
            double xDouble = StdDraw.mouseX();
            double yDouble = StdDraw.mouseY();
            int x = (int) Math.round(xDouble);
            int y = (int) Math.round(yDouble);
            Position p = new Position(Math.round(x), Math.round(y));
            String tileDesc = canvas.getTileDesc(p);
            return tileDesc;
        }

    }

    private class MouseTrack implements Runnable {
        public void run() {
            while (true) {
                try {
                    String newCursorInfo = gameScreen.cursorInfo();
                    if (!newCursorInfo.equals(currentCursorInfo)) {
                        gameScreen.showCanvas(ter);
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    System.out.println("Exception is caught.");
                }
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
        playWithInputString = true;
        inputString = input;
        processMainMenuInput();

        return canvas.getTiles();
    }

    /** Return one valid input key from the user.
     * This method will be waiting until a valid key is returned.
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

    /** Process the next char in the input string.
     * Equivalent to receiveInputKey method, but used
     * for playWithInputString method.
     * @param validInputs
     * @return
     */
    private char nextCharInString(String validInputs) {
        char key = inputString.charAt(0);
        String keyString = String.valueOf(key);
        if (validInputs.contains(keyString)) {
            inputString = inputString.substring(1);
            return key;
        } else {
            return nextCharInString(validInputs);
        }
    }

    /** Receive user input for main menu options and convert
     * it to upper case.
     * @return
     */
    private char receiveMainMenuInput() {
        char key;
        if (playWithInputString) {
            key = nextCharInString(validMainMenuInputs);
        } else {
            key = receiveInputKey(validMainMenuInputs);
        }
        key = Character.toUpperCase(key);
        return key;
    }

    /** Read the seed from user inputs. */
    private long receiveInputSeed() {
        String seedString = "";
        boolean startGameButton = false;

        // Display a message to user, asking for the seed.
        MainMenu.displaySeedInfo(seedString);

        // Read all integer inputs before the user press "S".
        while (!startGameButton) {
            char userInput;
            if (playWithInputString) {
                userInput = nextCharInString(validSeedInputs);
            } else {
                userInput = receiveInputKey(validSeedInputs);
            }
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

    /** Process one input key for the main menu.
     * N to start a new game. The user must provide a seed.
     * L to load existing game.
     * Q to quit. */
    private void processMainMenuInput() {
        char key = receiveMainMenuInput();
        switch (key) {
            case 'N': {
                long seed = receiveInputSeed();
                random = new Random(seed);
                canvas = newRandomWorld();
                if (!playWithInputString) {
                    playingGame();
                } else {
                    playingGameWithInputString();
                }
                break;
            }
            case 'L': {
                canvas = loadGame();
                if (canvas != null) {
                    if (!playWithInputString) {
                        playingGame();
                    } else {
                        playingGameWithInputString();
                    }
                }
                break;
            }
            case 'Q': System.exit(0);
            default: return;
        }
    }

    /** Display the world and allow the player to move around in it.*/
    private void playingGame() {
        gameScreen.setup(ter);

        //Thread mouseTrack = new Thread(new MouseTrack());
        //mouseTrack.start();
        boolean endSignal = false;

        while (true) {
            gameScreen.showCanvas(ter);
            char key = receiveInputKey(validMoveInputs);
            key = Character.toUpperCase(key);
            if (key == 'Q') {
                if (endSignal) {
                    saveGame(canvas);
                    System.exit(0);
                }
            } else {
                canvas.moveOneStep(key);
            }
            endSignal = (key == ':');
        }
    }

    /** Return the world exactly as it would be if
     * the user has typed the input keys manually.
     */
    private void playingGameWithInputString() {
        boolean endSignal = false;
        while (inputString.length() > 0) {
            char key = nextCharInString(validMoveInputs);
            key = Character.toUpperCase(key);
            if (key == 'Q') {
                if (endSignal) {
                    saveGame(canvas);
                    return;
                }
            } else {
                canvas.moveOneStep(key);
            }
            endSignal = (key == ':');
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
            System.out.println("IOException is caught. Game is not saved.");
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
