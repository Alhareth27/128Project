import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class AutoComplete {

    /**
     * Main method that autocompletes words
     * This could be used for predictive text entry, like in a cell phone,
     * or for spell checking.
     *
     * @param args Command line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(String args[]) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
        while (true) {
            System.out.println("Enter text prefix to search, or 'stop'.");
            String text = in.readLine();
            if (text.trim().equalsIgnoreCase("stop")) {
                break;
            }
            String[] word = { text };
            ArrayList<String> words = getAutoCompleteSuggestions(word);
            if (words.isEmpty()) {
                System.out.println("No words found with the prefix: " + text);
            } else {
                System.out.println("========== Results =============");
                words.forEach(System.out::println);
            }
            System.out.println();
        }
    }

    /**
     * Method to get autocomplete suggestions based on a given word.
     *
     * @param word The word to get autocomplete suggestions for.
     * @return ArrayList of autocomplete suggestions for the given word.
     */
    public static ArrayList<String> getAutoCompleteSuggestions(String[] word) {
        PrefixTree tree = new PrefixTree();

        // Load dictionary or words from file into the prefix tree
        File file = getFile("res/dictionary.txt"); // Replace with your dictionary file path
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String dictionaryWord;
            while ((dictionaryWord = reader.readLine()) != null) {
                tree.add(dictionaryWord);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get autocomplete suggestions for the provided word
        ArrayList<String> words = tree.getWordsForPrefix(word[0]);
        return words;
    }

    /**
     * Loads a file from the res folder.
     **/
    public static File getFile(String resourceName) {
        try {
            URL url = AutoComplete.class.getResource("/" + resourceName);
            if (url != null) {
                return new File(url.toURI());
            } else {
                System.out.println("Cannot find file with name " + resourceName);
                return null;
            }
        } catch (URISyntaxException syntaxException) {
            syntaxException.printStackTrace();
            return null;
        }
    }
}
