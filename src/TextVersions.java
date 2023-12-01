import javax.swing.*;
import java.util.Stack;

public class TextVersions {
    private JTextArea textArea;
    private Stack<String> undoStack;
    private Stack<String> redoStack;

    public TextVersions(JTextArea textArea) {
        this.textArea = textArea;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        saveState();
    }

    public void saveState() {
        undoStack.push(textArea.getText());
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(textArea.getText());
            textArea.setText(undoStack.pop());
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(textArea.getText());
            textArea.setText(redoStack.pop());
        }
    }
}
