
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in the prefix tree
 */
public class TreeNode {
    // True if this node represents the last character in a word based on the path
    // from the root of the tree to this node.
    public boolean isWord;

    // The letter this node represents
    // This isn't strickly necessary since you can get the letter from the key used
    // to link this node's parent,
    // but this can make things easier.
    public char letter;

    // The node's children keyed by each child node's letter
    public Map<Character, TreeNode> children;

    public TreeNode() {
        children = new HashMap<>();
        isWord = false;
    }
}
