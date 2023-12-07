
import java.util.ArrayList;
import java.util.Map;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links
 * to child nodes (up
 * to 26, one per letter). Each child node represents a letter. A path from a
 * root's child node down
 * to a node where isWord is true represents the sequence of characters in a
 * word.
 */
public class PrefixTree {
    private TreeNode root;

    // Number of words contained in the tree
    private int size;

    public PrefixTree() {
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node If
     * the word, is
     * already in the tree, then this has no effect.
     * 
     * @param word
     */
    public void add(String word) {
        TreeNode CurrentNode = root;
        for (char i : word.toCharArray()) {
            if (!contains(word)) {
                CurrentNode.children.putIfAbsent(i, new TreeNode());
            }
            CurrentNode = CurrentNode.children.get(i);
        }
        if (!CurrentNode.isWord) {
            CurrentNode.isWord = true;
            size++;

        }
    }

    /**
     * Checks whether the word has been added to the tree
     * 
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word) {
        TreeNode currentNode = root;
        for (char i : word.toCharArray()) {
            currentNode = currentNode.children.get(i);

            if (currentNode == null) {

                return false;
            }
        }
        return currentNode.isWord;

    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is
     * a word itself). The
     * order of the list can be arbitrary.
     * 
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix) {
        TreeNode CurrentNode = root;
        ArrayList<String> ArrayofWords = new ArrayList<>();
        for (char c : prefix.toCharArray()) {
            CurrentNode = CurrentNode.children.get(c);
        }
        RecursiveCall(ArrayofWords, CurrentNode, prefix);
        return ArrayofWords;
    }

    public void RecursiveCall(ArrayList<String> ArrayofWords, TreeNode root, String Singleword) {
        if (root.isWord) {
            ArrayofWords.add(Singleword);
        }
        for (Map.Entry<Character, TreeNode> Child : root.children.entrySet()) {
            RecursiveCall(ArrayofWords, Child.getValue(), Singleword + Child.getKey());
        }
    }

    /**
     * @return the number of words in the tree
     */
    public int size() {
        return size;
    }

}
