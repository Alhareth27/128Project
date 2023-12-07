
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Main class that autocompletes words
 * This could be used for predictive text entry, like in a cell phone,
 * or for spell checking.
 * 
 * @author Bret
 */
public class AutoComplete {

    public static void main(String args[]) throws IOException {
        PrefixTree tree = new PrefixTree();

        File file = getFile("dictionary.txt"); // use smalldictionary.txt for testing if needed.
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            while (true) {
                String word = r.readLine(); // one word per line
                if (word == null) {
                    break;
                }
                tree.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
        while (true) {
            System.out.println("Enter text prefix to search, or 'stop'.");
            String text = in.readLine();
            if (text.trim().equalsIgnoreCase("stop")) {
                break;
            }
            ArrayList<String> words = tree.getWordsForPrefix(text);
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
