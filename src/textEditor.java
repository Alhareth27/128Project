import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.undo.UndoManager;

public class textEditor extends JFrame implements ActionListener {

    private static final Dimension WINDOW_SIZE = new Dimension(1000, 1000); 
    private static final Dimension SCROLL_BOUNDS = new Dimension(900, 750);
    private static final int DEFAULT_FONT_SIZE = 30;

    JTextPane textPane;

    JButton fontcolor;
    JComboBox<String> fontList;

    JButton bold;
    JButton italic;

    JMenuBar MenuBar;
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
    private FormattingOptions format;

    textEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(WINDOW_SIZE);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        // Container contentPane = this.getContentPane();
        // Container contentPane = getContentPane();

        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

        setUpTextArea();
        format = new FormattingOptions(textPane);

        // undoManager = new UndoManager();
        // textPane.getDocument().addUndoableEditListener(e -> {
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
        // textState = new TextVersions(textPane);

        // stack = new ArrayDeque<String>();
        // textPane.getDocument().addDocumentListener(new DocumentListener() {
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
        setUpMenuBar();
        bold = setUpButton("Bold");
        italic = setUpButton("Italics");
        fontcolor = setUpButton("Text Color");

        setUpFontSize();
        setUpFontStyle();

        addScrollBar(textPane);
        this.setVisible(true);



        
        // public met update() {
        // Character currentText = textPane.getText().getChars(ERROR, ALLBITS, null,
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
        // textPane.setText(previousText);
        // } else {
        // textPane.setText(".");
        // }
    }

    private void setUpTextArea() {
        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 30));
        textPane.addMouseListener(new MouseListener() {
            //Unnecessary methods
            public void mouseClicked(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            
            public void mouseReleased(MouseEvent e) {
                if (textPane.getSelectedText() != null) { // See if they selected something 
                    String s = textPane.getSelectedText();
                    System.out.println(s);
                }
            }
        });
    }

    private void addScrollBar(JTextPane textPane) {
        JScrollPane Scroll = new JScrollPane(textPane);
        Scroll.setPreferredSize(new Dimension(SCROLL_BOUNDS));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(Scroll);
    }

    private void setUpFontSize() {
        JLabel fontText = new JLabel("Font Size: ");
        JSpinner fontSpinner = new JSpinner();
        fontSpinner.setPreferredSize(new Dimension(50, 50));
        fontSpinner.setValue(DEFAULT_FONT_SIZE);
        fontSpinner.setLocation(30, 0);

        fontSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textPane.setFont(new Font(textPane.getFont().getFamily(), Font.PLAIN, (int) fontSpinner.getValue()));
            }
        });
        this.add(fontText);
        this.add(fontSpinner);
    }

    private void setUpFontStyle() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontList = new JComboBox<String>(fonts);
        fontList.addActionListener(this);
        this.add(fontList);
    }

    private void setUpMenuBar() {
        MenuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        Open = setUpMenuItem("Open", menu);
        Save = setUpMenuItem("Save", menu);
        Exit = setUpMenuItem("Exit", menu);
        Undo = setUpMenuItem("Undo", menu);
        Redo = setUpMenuItem("Redo", menu);
        MenuBar.add(menu);
        this.setJMenuBar(MenuBar);
    }

    private JMenuItem setUpMenuItem(String text, JMenu menu) {
        JMenuItem option = new JMenuItem(text);
        option.addActionListener(this);
        menu.add(option);
        return option;
    }

    private JButton setUpButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        this.add(button);
        return button;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == fontcolor) {
            format.setTextColor();
        }
        else if (e.getSource() == fontList) {
            Font font1 = new Font((String) fontList.getSelectedItem(), Font.PLAIN, textPane.getFont().getSize());
            textPane.setFont(font1);
        }
        else if (e.getSource() == Open) {
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
                            textPane.setText(textPane.getText() + line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } finally {
                    infile.close();
                }
            }
        }
        else if (e.getSource() == Exit) {
            System.exit(ABORT);
        }
        else if (e.getSource() == Save) {
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
        // }
        // if (e.getSource() == Undo) {
        // Undoevent();
        // }
        // if (e.getSource() == Undo) {
        // if (undoManager.canUndo()) {
        // undoManager.undo();
        // }
        // }
        else if (e.getSource() == Undo) {
            textState.undo();
            // format.undo();
        }
        else if (e.getSource() == Redo) {
            textState.redo();
        }
        else if (e.getSource() == bold) {
            countofBold++;
            Font currentFont = textPane.getFont();
            if (countofBold % 2 == 0) {
                textPane.setFont(currentFont.deriveFont(Font.BOLD));
            } else {
                textPane.setFont(currentFont.deriveFont(Font.PLAIN));
            }
        }
        else if (e.getSource() == italic) {
            countofItalic++;
            Font currentFont = textPane.getFont();
            if (countofItalic % 2 == 0) {
                textPane.setFont(currentFont.deriveFont(Font.ITALIC));
            } else {
                textPane.setFont(currentFont.deriveFont(Font.PLAIN));
            }
        }
    }

    public static void main(String[] args) {
        new textEditor();
    }
}
