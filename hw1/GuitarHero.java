import edu.princeton.cs.introcs.StdAudio;
import synthesizer.GuitarString;

import java.util.ArrayList;

/** A client that uses the synthesizer package allowing the user
 * to interactively play guitar string sounds.
 * supports a total of 37 notes on the chromatic scale from
 * 110Hz to 880H. */
public class GuitarHero {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static GuitarString[] guitarStrings = new GuitarString[37];

    /** Return the corresponding note for the given index.
     * @param i
     * @return
     */
    private static double indexToNote(int i) {
        return 440 * Math.pow(2, (i - 24) / 12);
    }

    /** Pluck a string depending on input char. */
    private static void pluckString(char c) {
        int i = keyboard.indexOf(c);
        if (i == -1) return;
        GuitarString string = guitarStrings[i];
        string.pluck();
    }

    /** Create 37 guitar strings, from 110Hz to 880Hz. */
    private static void createStrings() {
        for (int i = 0; i < keyboard.length(); i += 1) {
            double note = indexToNote(i);
            guitarStrings[i] = new GuitarString(note);
        }
    }

    /** compute the superposition of samples */
    private static void playSample() {
        double sample = 0;
        for (GuitarString s : guitarStrings) {
            sample += s.sample();
        }
        StdAudio.play(sample);
    }

    public static void main(String[] args) {

        createStrings();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                pluckString(key);
            }
            playSample();
            for (GuitarString s : guitarStrings) s.tic();
        }
    }

}
