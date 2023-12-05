import javax.lang.model.element.Element;
import javax.print.attribute.AttributeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Text;

public class textEditor extends JFrame implements ActionListener {

    private static final Dimension WINDOW_SIZE = new Dimension(1000, 1000);
    private static final Dimension SCROLL_BOUNDS = new Dimension(900, 750);
    private static final int DEFAULT_FONT_SIZE = 30;

    JTextArea textArea;
    JTextArea selectedArea;

    JButton fontcolor;
    JButton bold;
    JButton italic;
    JComboBox<String> fontList;
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

        // undoManager = new UndoManager();
        // textArea.getDocument().addUndoableEditListener(e -> {
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
        // textState = new TextVersions(textArea);

        // stack = new ArrayDeque<String>();
        // textArea.getDocument().addDocumentListener(new DocumentListener() {
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

        addScrollBar(textArea);
        this.setVisible(true);

        // public met update() {
        // Character currentText = textArea.getText().getChars(ERROR, ALLBITS, null,
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
        // textArea.setText(previousText);
        // } else {
        // textArea.setText(".");
        // }
    }

    private void setUpTextArea() {
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 30));
        textArea.addMouseListener(new MouseListener() {
            // Not necessary
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                if (textArea.getSelectedText() != null) { // See if they selected something
                    String s = textArea.getSelectedText();
                }
            }
        });
    }

    private void addScrollBar(JTextArea textArea) {
        JScrollPane Scroll = new JScrollPane(textArea);
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
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSpinner.getValue()));
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

    private static void boldSelectedText(JTextArea textArea) {
        styl doc = textArea.getStyledDocument();
        int selectionStart = textArea.getSelectionStart();
        int selectionEnd = textArea.getSelectionEnd();

        if (selectionStart != selectionEnd) {
            chara element = doc.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();

            StyleContext sc = StyleContext.getDefaultStyleContext();
            Style boldStyle = sc.addStyle("Bold", null);
            StyleConstants.setBold(boldStyle, true);

            if (StyleConstants.isBold(as)) {
                doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, boldStyle, false);
            } else {
                doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, boldStyle, true);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == fontcolor) {
            Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
            textArea.setForeground(color);
        }
        if (e.getSource() == fontList) {
            Font font1 = new Font((String) fontList.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize());
            textArea.setFont(font1);
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
                            textArea.append(line);

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
                    outfile.println(textArea.getText());

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
        // if (e.getSource() == bold) {
        // countofBold++;
        // Font currentFont = textArea.getFont();
        // if (countofBold % 2 == 0) {
        // textArea.setFont(currentFont.deriveFont(Font.BOLD));
        // } else {
        // textArea.setFont(currentFont.deriveFont(Font.PLAIN));
        // }
        // }
        if (e.getSource() == italic) {
            countofItalic++;
            Font currentFont = textArea.getFont();
            if (countofItalic % 2 == 0) {
                textArea.setFont(currentFont.deriveFont(Font.ITALIC));
            } else {
                textArea.setFont(currentFont.deriveFont(Font.PLAIN));
            }

        }
        if (e.getSource() == bold && textArea.getSelectedText() != null) {
            countofBold++;
            Font currentFont = textArea.getFont();

            // Get selected text
            String selectedText = textArea.getSelectedText();

            // Check if the selected text is already bold
            boolean isBold = (currentFont.getStyle() & Font.BOLD) != 0;

            if (countofBold % 2 == 0 && !isBold) {
                // If countofBold is even and the selected text is not bold, make it bold
                textArea.replaceSelection("<html><b>" + selectedText + "</b></html>");
            } else if (countofBold % 2 != 0 && isBold) {
                // If countofBold is odd and the selected text is bold, make it plain
                textArea.replaceSelection(selectedText);
            }
        }

    }

    public static void main(String[] args) {
        new textEditor();
    }
}
