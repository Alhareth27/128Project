import javax.swing.*;
import java.util.Stack;

public class TextVersions {

    // private Stack<> undoStack;
    private JTextPane paneArea;

    public TextVersions(textEditor editor) {
        this.paneArea = editor.getPaneArea();
        // this.undoStack = new Stack<>();
        saveState();
    }

    public void saveState() {
        TextWithStyle textWithStyle = TextWithStyle.fromJTextPane(paneArea);
        // undoStack.push(textWithStyle);
    }

    // public void undo() {
    // if (!undoStack.isEmpty()) {
    // TextWithStyle textWithStyle = undoStack.pop();
    // textWithStyle.applyToJTextPane(paneArea);
    // }
    // }
}
