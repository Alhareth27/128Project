import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import javax.swing.text.*;
import java.awt.Font;
// import java.util.ArrayDeque;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FormattingOptions {

    // private ArrayDeque<String> undoTextStack;
    // private ArrayDeque<AttributeSet> undoAttributeStack;
    private JTextPane textPane;

    public FormattingOptions(JTextPane textPane) {
        // undoTextStack = new ArrayDeque<String>();
        // undoAttributeStack = new ArrayDeque<AttributeSet>();
        this.textPane = textPane;
        pushToUndoStack();
    }

    public void setTextColor(Color color) {
        pushToUndoStack();
        int[] selectedtext = getSelectedTextPositon();
        SimpleAttributeSet set = new SimpleAttributeSet();
        set.addAttributes(textPane.getCharacterAttributes());
        StyleConstants.setForeground(set, color);
        textPane.getStyledDocument().setCharacterAttributes(selectedtext[0], selectedtext[1], set, true);
    }

    public void setFontStyle(Font font) {
        int[] selectedtext = getSelectedTextPositon();
        String text = textPane.getSelectedText();
        SimpleAttributeSet set = new SimpleAttributeSet();
        set.addAttributes(textPane.getCharacterAttributes());
        StyleConstants.setFontFamily(set, font.getName());
        
        try {
            textPane.getStyledDocument().remove(selectedtext[0], selectedtext[1]);
            textPane.getStyledDocument().insertString(selectedtext[0], text, set);
        }
        catch(BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text Files", "txt");
        fileChooser.setFileFilter(filter);
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            Scanner infile = null;
            try {
                infile = new Scanner(file);
                if (file.isFile()) {
                    textPane.setText("");
                    while (infile.hasNextLine()) {
                        String line = infile.nextLine() + "\n";
                        textPane.getDocument().insertString(textPane.getDocument().getLength(), line, null);
                    }
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (BadLocationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } finally {
                if (infile != null) {
                    infile.close();
                }
            }
        }
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
        // undoTextStack.push(textPane.getText());
        // undoAttributeStack.push(textPane.getCharacterAttributes());
    }
    
}
