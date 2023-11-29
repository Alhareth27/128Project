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
    JLabel fontText;
    JColorChooser fontColor;

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
        Scroll.setPreferredSize(new Dimension(800, 950));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontText = new JLabel("Font: ");
        Fontspinner = new JSpinner();
        Fontspinner.setPreferredSize(new Dimension(50, 50));
        Fontspinner.setValue(100);
        Fontspinner.setLocation(30, 0);

        Fontspinner.addChangeListener(new ChangeListener() {
            @Override

            public void stateChanged(ChangeEvent e) {
                AreaText.setFont(new Font(AreaText.getFont().getFamily(), Font.PLAIN, (int) Fontspinner.getValue()));
            }

        });

        this.add(Scroll);
        this.setVisible(true);
        this.add(fontText);
        this.add(Fontspinner);
    }

    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new textEditor();
    }
}