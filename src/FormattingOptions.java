import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import javax.swing.text.*;
import java.awt.Font;
import java.io.PrintWriter;
// import java.util.ArrayDeque;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FormattingOptions {

    private static final int DEFAULT_FONT_SIZE = 30;
    private JTextPane textPane;

    public FormattingOptions(JTextPane textPane) {
        this.textPane = textPane;
    }

    public void setTextColor(Color color) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, color);
        setTextPaneAttributes(set);
        StyleConstants.setForeground(textPane.getInputAttributes(), Color.BLACK);
    }

    public void setFontStyle(Font font) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setFontFamily(set, font.getName());
        setTextPaneAttributes(set);
        StyleConstants.setFontFamily(textPane.getInputAttributes(), "Arial");
    }

    public void setFontSize(int size) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setFontSize(set, size);
        setTextPaneAttributes(set);
        StyleConstants.setFontSize(textPane.getInputAttributes(), DEFAULT_FONT_SIZE);
    }

    public void setItalic() {
        SimpleAttributeSet oldSet = new SimpleAttributeSet();
        SimpleAttributeSet newSet = new SimpleAttributeSet();
        oldSet.addAttributes(textPane.getCharacterAttributes());
        boolean italic = StyleConstants.isItalic(oldSet) ? false : true;
        StyleConstants.setItalic(newSet, italic);
        setTextPaneAttributes(newSet);
    }

    public void setBold() {
        SimpleAttributeSet oldSet = new SimpleAttributeSet();
        SimpleAttributeSet newSet = new SimpleAttributeSet();
        oldSet.addAttributes(textPane.getCharacterAttributes());
        boolean bold = StyleConstants.isBold(oldSet) ? false : true;
        StyleConstants.setBold(newSet, bold);
        setTextPaneAttributes(newSet);
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
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            } finally {
                if (infile != null) {
                    infile.close();
                }
            }
        }
    }

    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter outfile = null;

            file = new File(fileChooser.getSelectedFile().getAbsolutePath());

            try {
                outfile = new PrintWriter(file);
                outfile.println(textPane.getText());

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } finally {
                outfile.close();
            }
        }
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
