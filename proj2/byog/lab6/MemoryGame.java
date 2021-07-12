package byog.lab6;

import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.sql.DriverPropertyInfo;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        Random random = new Random(seed);
        MemoryGame game = new MemoryGame(40, 40, random);
        game.startGame();
    }

    public MemoryGame(int width, int height, Random random) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.rand = random;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    /** Generate random string of letters of length n */
    public String generateRandomString(int n) {
        char[] randomChars = new char[n];
        for (int i = 0; i < n; i += 1) {
            int j = rand.nextInt(CHARACTERS.length);
            randomChars[i] = CHARACTERS[j];
        }
        String randomString = new String(randomChars);
        return randomString;
    }

    /** Take the string and display it in the center of the screen
     * If game is not over, display relevant game information at
     * the top of the screen*/
    public void drawFrame(String s) {

        StdDraw.clear(Color.BLACK);

        if (!gameOver) {
            Font infoFont = new Font("Arial", Font.BOLD, 20);
            StdDraw.setFont(infoFont);
            String roundInfo = "Round: " + round;
            StdDraw.text(0.1 * this.width, 0.95 * this.height, roundInfo);
            //StdDraw.line(0, height - 2, width, height - 2);
            StdDraw.setPenColor(StdDraw.WHITE);
        }

        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(0.5 * this.width, 0.5 * this.height, s);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.show();
    }

    /** Display the target string one character at a time.
     * Each character is visible on the screen for 1 second
     * and there is a brief 0.5 second break between characters
     * where the screen is blank.
     * @param letters
     */
    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i += 1) {
            char c = letters.charAt(i);
            String s = String.valueOf(c);
            // show each character for one second.
            drawFrame(s);
            StdDraw.pause(1000);
            // show blank screen for 0.5 second.
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    /** Read n letters of player input */
    public String solicitNCharsInput(int n) {
        String playerInput = "";
        while (playerInput.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                playerInput += StdDraw.nextKeyTyped();
            }
        }
        return playerInput;
    }

    public void startGame() {

        round = 1;
        gameOver = false;
        while (true) {
            drawFrame("Round: " + round);
            StdDraw.pause(1000);
            String randomString = generateRandomString(round);
            flashSequence(randomString);
            String inputString = solicitNCharsInput(round);
            drawFrame("You typed: " + inputString);
            StdDraw.pause(1000);
            if (!inputString.equals(randomString)) {
                drawFrame("Gave Over! You made it to round: " + round);
                gameOver = true;
                return;
            } else {
                drawFrame("Correct!");
                StdDraw.pause(1000);
            }
            round += 1;
        }
    }
}
