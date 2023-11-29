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
    JButton fontcolor;
    JComboBox fontlist;
    JMenuBar MenuBar;
    JMenu menu;
    JMenuItem Open;
    JMenuItem Exit;
    JMenuItem Save;

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
        Scroll.setPreferredSize(new Dimension(900, 950));
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
        fontcolor = new JButton("Color");
        fontcolor.addActionListener(this);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontlist = new JComboBox<>(fonts);
        fontlist.addActionListener(this);

        // --------------------------------
        MenuBar = new JMenuBar();
        menu = new JMenu("File");
        Open = new JMenuItem("Open");
        Save = new JMenuItem("Save");
        Exit = new JMenuItem("Exit");
        menu.add(Open);
        menu.add(Save);
        menu.add(Exit);
        MenuBar.add(menu);

        // -----------------------------------
        this.setJMenuBar(MenuBar);
        this.add(fontText);
        this.add(Fontspinner);
        this.add(fontcolor);
        this.add(fontlist);
        this.add(Scroll);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == fontcolor) {
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(null, "Choose a Color", Color.BLACK);
            AreaText.setForeground(color);
        }
        if (e.getSource() == fontlist) {
            Font font1 = new Font((String) fontlist.getSelectedItem(), Font.PLAIN, AreaText.getFont().getSize());
            AreaText.setFont(font1);
        }
    }

    public static void main(String[] args) {
        new textEditor();
    }
}