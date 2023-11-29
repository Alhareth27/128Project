import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class textEditor extends JFrame implements ActionListener {

    JTextArea AreaText;
    JScrollPane Scroll;
    JSpinner Fontspinner;

    textEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(1000, 1000);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        AreaText = new JTextArea();
        AreaText.setWrapStyleWord(true);
        AreaText.setLineWrap(true);
        AreaText.setFont(new Font("Arial", Font.PLAIN, 100));

        Scroll = new JScrollPane(AreaText);
        Scroll.setPreferredSize(new Dimension(950, 950));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Fontspinner = new JSpinner();
        Fontspinner.setPreferredSize(new Dimension(50, 100));
        Fontspinner.setValue(100);
        // Fontspinner.addChangeListener(new ChangeListener() {
        // public void Change(ChangeEvent e) {

        // AreaText.setFont(new Font(AreaText.getFont().getFamily(), Font.PLAIN, (int)
        // Fontspinner.getValue()));
        // }

        // });
        this.add(Scroll);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new textEditor();
    }
}