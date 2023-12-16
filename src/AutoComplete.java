import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class AutoComplete {

    private PrefixTree tree;

    public AutoComplete() {
        tree = new PrefixTree();

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
    }

    /**
     * Returns a random word that starts with the given prefix
     */
    public String AutoCompleteWord(String prefix) {
        ArrayList<String> words = getAutoCompleteSuggestions(prefix);
        Random random = new Random();
        String suggestion = "";
        if (words != null) {
            if (words.size() == 1) {
                return words.get(0); //If thre'sonly one option, return that option.
            }
            suggestion = words.get(random.nextInt(words.size() - 1));
        }
        return suggestion;
    }

    /**
     * Method to get autocomplete suggestions based on a given word.
     *
     * @param word The word to get autocomplete suggestions for.
     * @return ArrayList of autocomplete suggestions for the given word.
     */
    private ArrayList<String> getAutoCompleteSuggestions(String word) {
        if (tree.contains(word)) {
            ArrayList<String> words = tree.getWordsForPrefix(word);
            return words;
        }
        return null;
    }

    /**
     * Loads a file from the res folder.
     **/
    private static File getFile(String resourceName) {
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

    public String toString() {
        return "An Autocomplete helper, containing " + tree.size() + "words";
    }
}
