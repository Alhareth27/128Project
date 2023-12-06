
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.chrono.JapaneseDate;
import java.util.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class textEditor extends JFrame implements ActionListener {

    private static final Dimension WINDOW_SIZE = new Dimension(1000, 1000);
    private static final Dimension SCROLL_BOUNDS = new Dimension(900, 750);
    private static final int DEFAULT_FONT_SIZE = 30;

    // JTextArea PaneArea;
    JTextPane PaneArea;

    JButton fontcolor;
    JButton bold;
    JButton italic;
    JComboBox<String> fontList;
    JMenuBar MenuBar;
    JMenuItem Open;
    JMenuItem InsertImage;
    JMenuItem Exit;
    JMenuItem Save;
    JMenuItem Undo;
    JMenuItem Redo;
    int countofBold = 1;
    int countofItalic = 1;
    public ArrayDeque<String> stack;
    public UndoManager undoManager;
    boolean actionIsPerformed = false;
    JMenu insertMenu;
    // public TextVersions TextState;
    public SizedStack<String> undoStack;
    KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

    textEditor() throws BadLocationException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(WINDOW_SIZE);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        // Container contentPane = this.getContentPane();
        // Container contentPane = getContentPane();

        setUpTextArea();

        // undoManager = new UndoManager();
        // textArea.getDocument().addUndoableEditListener(e -> {
        // undoManager.addEdit(e.getEdit());
        // });
        // Action undoAction = new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // if (e.getSource() == Undo) {

        // }
        // }
        // };

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

        addScrollBar(PaneArea);
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
        // TextState = new TextVersions(this);
        undoStack = new SizedStack<>(20);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable codeSnippet = () -> {
            String currentText = PaneArea.getText();

            // If the stack is empty or the top element is different from the current text,
            // push the current text
            if (undoStack.isEmpty() || !undoStack.peek().equals(currentText)) {
                undoStack.push(currentText);
            }

            System.out.println("=================");
            System.out.println(Arrays.toString(undoStack.toArray()));
            System.out.println("=================");
        };

        // Schedule the code snippet to run every 5 seconds
        executor.scheduleAtFixedRate(codeSnippet, 0, 1, TimeUnit.SECONDS);

        // new java.util.Timer().schedule(
        // new java.util.TimerTask() {
        // @Override
        // public void run() {
        // undoStack.push("hi");
        // System.out.println("-----UNDO STACK BITCH -----");
        // System.out.println(Arrays.toString(undoStack.toArray()));
        // System.out.println("-----UNDO STACK BITCH -----");
        // }
        // },
        // 5000);

    }

    public JTextPane setUpTextArea() {
        PaneArea = new JTextPane();
        PaneArea.setFont(new Font("Arial", Font.PLAIN, 30));
        //

        // Enable word wrap
        StyledDocument doc = PaneArea.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attrs, 0.5f);
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);

        JScrollPane scrollPane = new JScrollPane(PaneArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        // Add the scroll pane to your container or panel
        // container.add(scrollPane);
        PaneArea.addMouseListener(new MouseListener() {
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
                if (PaneArea.getSelectedText() != null) { // See if they selected something
                    String s = PaneArea.getSelectedText();
                }
            }
        });
        return PaneArea;
    }

    public void addScrollBar(JTextPane PaneArea) {
        JScrollPane Scroll = new JScrollPane(PaneArea);
        Scroll.setPreferredSize(new Dimension(SCROLL_BOUNDS));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(Scroll);
    }

    public void setUpFontSize() {
        JLabel fontText = new JLabel("Font Size: ");
        JSpinner fontSpinner = new JSpinner();
        fontSpinner.setPreferredSize(new Dimension(50, 50));
        fontSpinner.setValue(DEFAULT_FONT_SIZE);
        fontSpinner.setLocation(30, 0);

        fontSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                PaneArea.setFont(new Font(PaneArea.getFont().getFamily(), Font.PLAIN, (int) fontSpinner.getValue()));
            }
        });
        this.add(fontText);
        this.add(fontSpinner);
    }

    public void setUpFontStyle() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontList = new JComboBox<String>(fonts);
        fontList.addActionListener(this);
        this.add(fontList);
    }

    public void setUpMenuBar() {
        MenuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        insertMenu = new JMenu("Insert");
        InsertImage = setUpMenuItem("Image", insertMenu);
        Open = setUpMenuItem("Open", menu);
        Save = setUpMenuItem("Save", menu);
        Exit = setUpMenuItem("Exit", menu);
        Undo = setUpMenuItem("Undo", menu);
        MenuBar.add(menu);
        MenuBar.add(insertMenu);
        this.setJMenuBar(MenuBar);
        Undo.setAccelerator(undoKeyStroke);
    }

    public JMenuItem setUpMenuItem(String text, JMenu menu) {
        JMenuItem option = new JMenuItem(text);
        option.addActionListener(this);
        menu.add(option);
        return option;
    }

    public JButton setUpButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        this.add(button);
        return button;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == fontcolor) {
            Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
            PaneArea.setForeground(color);
            // TextState.saveState();

        }
        if (e.getSource() == fontList) {
            Font font1 = new Font((String) fontList.getSelectedItem(), Font.PLAIN, PaneArea.getFont().getSize());
            PaneArea.setFont(font1);
            // TextState.saveState();

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
                            PaneArea.getDocument().insertString(PaneArea.getDocument().getLength(), line, null);
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
                    outfile.println(PaneArea.getText());

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
            undoStack.pop();
            PaneArea.setText(undoStack.peek());
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
            // Font currentFont = PaneArea.getFont();
            // String s = PaneArea.getSelectedText();

            // // Get selected text
            // String selectedText = PaneArea.getSelectedText();
            // StyledDocument doc = PaneArea.getStyledDocument();
            // int selectionStart = PaneArea.getSelectionStart();
            // int selectionEnd = PaneArea.getSelectionEnd();
            // String bold = selectedText.toLowerCase();

            // // Check if the selected text is already bold
            // boolean isBold = (currentFont.getStyle() & Font.BOLD) != 0;

            String selectedText = PaneArea.getSelectedText();

            // Check if there is selected text and it's not empty
            if (selectedText != null) {
                StyledDocument doc = PaneArea.getStyledDocument();
                int selectionStart = PaneArea.getSelectionStart();
                int selectionEnd = PaneArea.getSelectionEnd();
                // Create a simple attribute set for styling
                SimpleAttributeSet italicAttrs = new SimpleAttributeSet();
                StyleConstants.setItalic(italicAttrs, true);
                SimpleAttributeSet Plain = new SimpleAttributeSet();
                StyleConstants.setItalic(Plain, false);

                // Apply the bold attribute to the selected text
                if (countofItalic % 2 == 0) {
                    doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, italicAttrs, false);
                } else {
                    doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart,
                            Plain, true);

                }

            }
            // TextState.saveState();

        }
        if (e.getSource() == bold && PaneArea.getSelectedText() != null) {
            countofBold++;
            // Font currentFont = PaneArea.getFont();
            // String s = PaneArea.getSelectedText();

            // // Get selected text
            // String selectedText = PaneArea.getSelectedText();
            // StyledDocument doc = PaneArea.getStyledDocument();
            // int selectionStart = PaneArea.getSelectionStart();
            // int selectionEnd = PaneArea.getSelectionEnd();
            // String bold = selectedText.toLowerCase();

            // // Check if the selected text is already bold
            // boolean isBold = (currentFont.getStyle() & Font.BOLD) != 0;

            String selectedText = PaneArea.getSelectedText();

            // Check if there is selected text and it's not empty
            if (selectedText != null) {
                StyledDocument doc = PaneArea.getStyledDocument();
                int selectionStart = PaneArea.getSelectionStart();
                int selectionEnd = PaneArea.getSelectionEnd();
                // Create a simple attribute set for styling
                SimpleAttributeSet boldAttrs = new SimpleAttributeSet();
                StyleConstants.setBold(boldAttrs, true);
                SimpleAttributeSet Plain = new SimpleAttributeSet();
                StyleConstants.setBold(Plain, false);

                // Apply the bold attribute to the selected text
                if (countofBold % 2 == 0) {
                    doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, boldAttrs, false);
                } else {
                    doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart,
                            Plain, true);

                }

            }
        }
        if (e.getSource() == InsertImage) {

        }

    }

    public static void main(String[] args) throws BadLocationException {
        new textEditor();
    }

    public JTextPane getPaneArea() {
        return PaneArea;
    }
}
