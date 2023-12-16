import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class FileOptions {

    public static void openFile(JTextPane textPane) {
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
    
    public static void saveFile(JTextPane textPane) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter outfile = null;

            file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".txt");

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
}
