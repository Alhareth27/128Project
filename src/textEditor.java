import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Text;

public class textEditor extends JFrame implements ActionListener {

    JTextArea AreaText;
    JScrollPane Scroll;
    JSpinner Fontspinner;
    JLabel fontText;
    JButton fontcolor;
    JButton bold;
    JButton italic;
    JComboBox fontlist;
    JMenuBar MenuBar;
    JMenu menu;
    JMenuItem Open;
    JMenuItem Exit;
    JMenuItem Save;
    JMenuItem Undo;
    JMenuItem Redo;
    int countofBold = 1;
    int countofItalic = 1;
    public ArrayDeque<String> stack;
    public UndoManager undoManager;
    private TextVersions textState;

    textEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(1000, 1000);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        // Container contentPane = this.getContentPane();
        // Container contentPane = getContentPane();

        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

        AreaText = new JTextArea();
        AreaText.setWrapStyleWord(true);
        AreaText.setLineWrap(true);
        AreaText.setFont(new Font("Arial", Font.PLAIN, 30));

        Scroll = new JScrollPane(AreaText);
        Scroll.setPreferredSize(new Dimension(900, 950));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontText = new JLabel("Font: ");
        Fontspinner = new JSpinner();
        Fontspinner.setPreferredSize(new Dimension(50, 50));
        Fontspinner.setValue(30);
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
        // undoManager = new UndoManager();
        // AreaText.getDocument().addUndoableEditListener(e -> {
        // undoManager.addEdit(e.getEdit());
        // });
        // Action undoAction = new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // if (undoManager.canUndo()) {
        // undoManager.undo();
        // }
        // }
        // };
        // getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke,
        // "undoKeystroke");
        // getRootPane().getActionMap().put("undoKeystroke", undoAction);
        bold = new JButton("Bold");
        bold.addActionListener(this);
        italic = new JButton("Italic");
        italic.addActionListener(this);
        textState = new TextVersions(AreaText);

        // stack = new ArrayDeque<String>();
        // AreaText.getDocument().addDocumentListener(new DocumentListener() {
        // @Override
        // public void insertUpdate(DocumentEvent e) {
        // update();
        // throw new UnsupportedOperationException("Unimplemented method
        // 'insertUpdate'");
        // }

        // @Override
        // public void removeUpdate(DocumentEvent e) {
        // update();
        // throw new UnsupportedOperationException("Unimplemented method
        // 'removeUpdate'");
        // }

        // @Override
        // public void changedUpdate(DocumentEvent e) {
        // update();
        // throw new UnsupportedOperationException("Unimplemented method
        // 'changedUpdate'");
        // }
        // });

        // --------------------------------
        MenuBar = new JMenuBar();
        menu = new JMenu("File");
        Open = new JMenuItem("Open");
        Save = new JMenuItem("Save");
        Exit = new JMenuItem("Exit");
        Undo = new JMenuItem("Undo");
        Redo = new JMenuItem("Redo");
        menu.add(Open);
        menu.add(Save);
        menu.add(Exit);
        menu.add(Undo);
        menu.add(Redo);
        MenuBar.add(menu);
        Open.addActionListener(this);
        Save.addActionListener(this);
        Exit.addActionListener(this);
        Undo.addActionListener(this);
        Redo.addActionListener(this);

        // -----------------------------------
        this.setJMenuBar(MenuBar);
        this.add(bold);
        this.add(italic);
        this.add(fontText);
        this.add(Fontspinner);
        this.add(fontcolor);
        this.add(fontlist);
        this.add(Scroll);
        this.setVisible(true);

        // public met update() {
        // Character currentText = AreaText.getText().getChars(ERROR, ALLBITS, null,
        // ABORT);
        // ;
        // stack.push(currentText);
        // }

        // public void Undoevent() {
        // if (!stack.isEmpty()) {
        // stack.pop();
        // }
        // if (!stack.isEmpty()) {
        // String previousText = stack.peek();
        // AreaText.setText(previousText);
        // } else {
        // AreaText.setText(".");
        // }
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
        // }
        // if (e.getSource() == Undo) {
        // Undoevent();
        // }
        // if (e.getSource() == Undo) {
        // if (undoManager.canUndo()) {
        // undoManager.undo();
        // }
        // }
        if (e.getSource() == Undo) {
            textState.undo();
        }
        if (e.getSource() == Redo) {
            textState.redo();
        }
        if (e.getSource() == bold) {
            countofBold++;
            Font currentFont = AreaText.getFont();
            if (countofBold % 2 == 0) {
                AreaText.setFont(currentFont.deriveFont(Font.BOLD));
            } else {
                AreaText.setFont(currentFont.deriveFont(Font.PLAIN));
            }
        }
        if (e.getSource() == italic) {
            countofItalic++;
            Font currentFont = AreaText.getFont();
            if (countofItalic % 2 == 0) {
                AreaText.setFont(currentFont.deriveFont(Font.ITALIC));
            } else {
                AreaText.setFont(currentFont.deriveFont(Font.PLAIN));
            }
        }
    }

    public static void main(String[] args) {
        new textEditor();
    }
}
