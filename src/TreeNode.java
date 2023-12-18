
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in the prefix tree
 */
public class TreeNode {
    // True if this node represents the last character in a word based on the path
    // from the root of the tree to this node.
    public boolean isWord;

    // The node's children keyed by each child node's letter
    public Map<Character, TreeNode> children;

    public TreeNode() {
        children = new HashMap<>();
        isWord = false;
    }
}
