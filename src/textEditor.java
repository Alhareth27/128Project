import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
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
    JMenuItem Undo;
    private ArrayDeque<String> stack;

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
        stack = new ArrayDeque<String>();
        AreaText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
                throw new UnsupportedOperationException("Unimplemented method 'insertUpdate'");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
                throw new UnsupportedOperationException("Unimplemented method 'removeUpdate'");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
                throw new UnsupportedOperationException("Unimplemented method 'changedUpdate'");
            }
        });

        // --------------------------------
        MenuBar = new JMenuBar();
        menu = new JMenu("File");
        Open = new JMenuItem("Open");
        Save = new JMenuItem("Save");
        Exit = new JMenuItem("Exit");
        Undo = new JMenuItem("Undo");
        menu.add(Open);
        menu.add(Save);
        menu.add(Exit);
        menu.add(Undo);
        MenuBar.add(menu);
        Open.addActionListener(this);
        Save.addActionListener(this);
        Exit.addActionListener(this);
        Undo.addActionListener(this);

        // -----------------------------------
        this.setJMenuBar(MenuBar);
        this.add(fontText);
        this.add(Fontspinner);
        this.add(fontcolor);
        this.add(fontlist);
        this.add(Scroll);
        this.setVisible(true);

    }

    public void update() {
        String currentText = AreaText.getText();
        stack.push(currentText);
    }

    public void Undoevent() {
        if (!stack.isEmpty()) {
            stack.pop();
        }
        if (!stack.isEmpty()) {
            String previousText = stack.peek();
            AreaText.setText(previousText);
        } else {
            AreaText.setText(".");
        }
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
        if (e.getSource() == Open) {
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
                        while (infile.hasNextLine()) {
                            String line = infile.nextLine() + "\n";
                            AreaText.append(line);

                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } finally {
                    infile.close();
                }
            }
        }
        if (e.getSource() == Exit) {
            System.exit(ABORT);
        }
        if (e.getSource() == Save) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter outfile = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    outfile = new PrintWriter(file);
                    outfile.println(AreaText.getText());

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } finally {
                    outfile.close();
                }
            }

        }
        if (e.getSource() == Undo) {
            Undoevent();
        }
    }

    public static void main(String[] args) {
        new textEditor();
    }
}
