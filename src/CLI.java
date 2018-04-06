import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Qasim Nawaz
 */
public class CLI {

    /**
     * Loads words (lines) from the given file and inserts them into
     * a dictionary.
     *
     * @param f the file from which the words will be loaded
     * @return the dictionary with the words loaded from the given file
     * @throws IOException if there was a problem opening/reading from the file
     */
    static DictionaryTree loadWords(File f) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String word;
            DictionaryTree d = new DictionaryTree();
            while ((word = reader.readLine()) != null) {
                d.insert(word);
            }
            return d;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Loading dictionary ... ");
        DictionaryTree d = loadWords(new File(args[0]));
        System.out.println("done");
        System.out.println("LONGEST WORD: " +d.longestWord());
        System.out.println("HEIGHT: " + d.height());
        System.out.println("MAX BRANCH: " + d.maximumBranching());
        System.out.println("SIZE: " +d.size());
        System.out.println("NUM LEAVES: " +d.numLeaves());
        System.out.println("ALL WORDS: " +d.allWords());
        System.out.println("Enter prefixes for prediction below.");

        try (BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("---> " + d.predict(fromUser.readLine()));
            }
        }
    }
}