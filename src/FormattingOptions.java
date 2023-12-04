import javax.swing.*;
import java.awt.Color;
import javax.swing.text.*;
import java.util.ArrayDeque;

public class FormattingOptions {

    private ArrayDeque<String> undoTextStack;
    private ArrayDeque<AttributeSet> undoAttributeStack;
    private JTextPane textPane;

    public FormattingOptions(JTextPane textPane) {
        undoTextStack = new ArrayDeque<String>();
        undoAttributeStack = new ArrayDeque<AttributeSet>();
        this.textPane = textPane;
        pushToUndoStack();
    }

    public void setTextColor() {
        pushToUndoStack();
        Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
        int[] selectedtext = getSelectedTextPositon();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, color);
        textPane.getStyledDocument().setCharacterAttributes(selectedtext[0], selectedtext[1], set, true);
    }

    public void setFontStyle(JTextPane textArea) {
    }

    public void undo() {
        // String text = undoTextStack.pop();
        // textPane.setText(text);
        // AttributeSet set = undoAttributeStack.pop();
        // textPane.setCharacterAttributes(set, true);
    }
    
    /**
     * Helper method that finds the selected text and returns that text's onset and length.
     * @param textPane A JTextPane object.
     * @return an Array, the first value being the text's onset & the second being the text's length
     */
    private int[] getSelectedTextPositon() {
        int start = textPane.getSelectionStart();
        int length = textPane.getSelectionEnd() - start;
        int[] array = {start, length};
        return array;
    }

    private void pushToUndoStack() {
        undoTextStack.push(textPane.getText());
        undoAttributeStack.push(textPane.getCharacterAttributes());
    }
    
}
