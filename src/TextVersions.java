import javax.swing.*;
import java.util.Stack;

public class TextVersions {
    private JTextPane PaneArea;
    private Stack<TextWithStyle> undoStack;
    TextWithStyle textWithStyle;

    public TextVersions(JTextPane PaneArea) {
        this.PaneArea = PaneArea;
        this.undoStack = new Stack<>();
        saveState();
    }

    public void saveState() {
        String text = PaneArea.getText();
        Style style = PaneArea.getLogicalStyle(); // Get the style information

        TextWithStyle textWithStyle = new TextWithStyle(text, style);
        undoStack.push(textWithStyle);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            TextWithStyle textWithStyle = undoStack.pop();
            String text = textWithStyle.getText();
            Style style = textWithStyle.getStyle();

            // Restore the text and style to your PaneArea
            PaneArea.setText(text);
            PaneArea.setStyle(style); // Apply the retrieved style
        }
    }
}
