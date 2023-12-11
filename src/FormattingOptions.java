import javax.swing.*;
import javax.swing.text.*;

import java.awt.Color;
import java.awt.Font;

public class FormattingOptions {

    private static final int DEFAULT_FONT_SIZE = 30;
    private JTextPane textPane;

    public FormattingOptions(JTextPane textPane) {
        this.textPane = textPane;
    }

    /**
     * Sets any selected text to the privided color and displays it.
     */
    public void setTextColor(Color color) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, color);
        setTextPaneAttributes(set);
        StyleConstants.setForeground(textPane.getInputAttributes(), Color.BLACK);
    }

    /**
     * Sets any selected text to the given font on the text pane.
     */
    public void setFontStyle(Font font) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setFontFamily(set, font.getName());
        setTextPaneAttributes(set);
        StyleConstants.setFontFamily(textPane.getInputAttributes(), "Arial");
    }

    /**
     * Sets any selected text to the selected size on the text pane.
     */
    public void setFontSize(int size) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setFontSize(set, size);
        setTextPaneAttributes(set);
        StyleConstants.setFontSize(textPane.getInputAttributes(), DEFAULT_FONT_SIZE);
    }

    /**
     * Italicizes the selected text from the text pane if it isn't, 
     * or unitalicizes it if the text is italicized.
     */
    public void setItalic() {
        SimpleAttributeSet oldSet = new SimpleAttributeSet();
        SimpleAttributeSet newSet = new SimpleAttributeSet();
        oldSet.addAttributes(textPane.getCharacterAttributes());
        boolean italic = StyleConstants.isItalic(oldSet) ? false : true;
        StyleConstants.setItalic(newSet, italic);
        setTextPaneAttributes(newSet);
    }

    /**
     * Bolds the selected text from the text pane if it's not bold,
     * or unbolds it if the text is bolded.
     */
    public void setBold() {
        SimpleAttributeSet oldSet = new SimpleAttributeSet();
        SimpleAttributeSet newSet = new SimpleAttributeSet();
        oldSet.addAttributes(textPane.getCharacterAttributes());
        boolean bold = StyleConstants.isBold(oldSet) ? false : true;
        StyleConstants.setBold(newSet, bold);
        setTextPaneAttributes(newSet);
    }
    
    /**
     * Helper method that sets the JTextPane's character attribute set over the selected text
     */
    private void setTextPaneAttributes(SimpleAttributeSet set) {
        int start = textPane.getSelectionStart();
        int length = textPane.getSelectionEnd() - start;
        textPane.getStyledDocument().setCharacterAttributes(start, length, set, false);
    }
}
