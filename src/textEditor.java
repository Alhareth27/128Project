import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class textEditor extends JFrame implements ActionListener {

    private static final Dimension WINDOW_SIZE = new Dimension(1000, 1000);
    private static final Dimension SCROLL_BOUNDS = new Dimension(900, 750);
    private static final int DEFAULT_FONT_SIZE = 30;

    private JTextPane PaneArea;

    private JButton fontColor;
    private JComboBox<String> fontList;

    private JButton Bold;
    private JButton AutoCompleteButton;
    private JButton Italics;

    private JMenuBar MenuBar;
    private JMenuItem Open;
    private JMenuItem Exit;
    private JMenuItem Save;
    private JMenuItem Undo;
    private JMenuItem Redo;

    public static SizedStack<String> undoStack;
    public SizedStack<String> redoStack;

    private FormattingOptions format;   
    
    textEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(WINDOW_SIZE);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        setUpTextPane();
        format = new FormattingOptions(PaneArea);

        setUpMenuBar();
        Bold = setUpButton("Bold");
        AutoCompleteButton = setUpButton("AutoComplete");
        Italics = setUpButton("Italics");
        fontColor = setUpButton("Text Color");

        setUpFontSize();
        setUpFontStyle();

        addScrollBar();
        this.setVisible(true);

        undoStack = new SizedStack<>(20);
        redoStack = new SizedStack<>(20);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable codeSnippet = () -> {
            String currentText = PaneArea.getText();

            // If the stack is empty or the top element is different from the current text,
            // push the current text
            if (undoStack.isEmpty() || !undoStack.peek().equals(currentText)) {
                undoStack.push(currentText);
            }
        };

        // Schedule the code snippet to run every 5 seconds
        executor.scheduleAtFixedRate(codeSnippet, 0, 1, TimeUnit.SECONDS);
    }
    
    /**
     * Creates a new Text Pane, and adds necessary listeners to the text pane
     */
    private void setUpTextPane() {
        PaneArea = new JTextPane();
        PaneArea.setFont(new Font("Arial", Font.PLAIN, 30));
        
        PaneArea.addKeyListener(new KeyListener() {
            //Unnecessary methods
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}

            public void keyTyped(KeyEvent e) { 
                if (!redoStack.isEmpty()) {
                    redoStack.clear(); //Newly typed keys will conflict with stored redos if not cleared
                }
            }
        });
    }

    /**
     * Creates and adds a vertical scroll bar to the text pane
     */
    private void addScrollBar() {
        JScrollPane Scroll = new JScrollPane(PaneArea);
        Scroll.setPreferredSize(new Dimension(SCROLL_BOUNDS));
        Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(Scroll);
    }

    /**
     * Sets up the functionality of changing the font size of selected text, and displays it onto the text pane
     */
    private void setUpFontSize() {
        JLabel fontText = new JLabel("Font Size: ");
        JSpinner fontSpinner = new JSpinner();
        fontSpinner.setPreferredSize(new Dimension(50, 50));
        fontSpinner.setValue(DEFAULT_FONT_SIZE);
        fontSpinner.setLocation(30, 0);

        fontSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if ((int) fontSpinner.getValue() > 4) {
                    format.setFontSize((int) fontSpinner.getValue());
                }
            }
        });
        this.add(fontText);
        this.add(fontSpinner);
    }

    /**
     * Sets up the ability to change font stlyes of the text, and displays it on the text pane
     */
    private void setUpFontStyle() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontList = new JComboBox<String>(fonts);
        fontList.addActionListener(this);
        this.add(fontList);
    }

    /**
     * Sets up a menu bar to store various methods not related to directly modifying text.
     */
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

        //Adds keyboard shortcuts to undo and redo commands
        addKeyboardShortcut(Undo, KeyEvent.VK_Z);
        addKeyboardShortcut(Redo, KeyEvent.VK_Y);
    }

    /**
     * Creates and sets up a new JMenuItem with the given title, adds it to the provided JMenu, and returns it.
     */
    private JMenuItem setUpMenuItem(String text, JMenu menu) {
        JMenuItem option = new JMenuItem(text);
        option.addActionListener(this);
        menu.add(option);
        return option;
    }

    /**
     * Sets up a new JButton with the given text, adds it to the displayed window, and returns it.
     */
    private JButton setUpButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        this.add(button);
        return button;
    }

    /**
     * Binds the given KeyEvent key with the given JMenuItem.
     */
    private void addKeyboardShortcut(JMenuItem menuItem, int keyEventKey) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyEventKey, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        menuItem.setAccelerator(keyStroke);
    }

    /**
     * Checks through every implemented editing option when called, and does the according modification/action
     */
    public void actionPerformed(ActionEvent e) {
        // Check if there is selected text and it's not empty
        if (PaneArea.getSelectedText() != null) {

            if (e.getSource() == fontColor) {
                Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
                format.setTextColor(color);
            }
            else if (e.getSource() == fontList) {
                Font font = new Font((String) fontList.getSelectedItem(), Font.PLAIN, PaneArea.getFont().getSize());
                format.setFontStyle(font);
            }
            else if (e.getSource() == Italics) {
                format.setItalic();
            }
            else if (e.getSource() == Bold) {
                format.setBold();
            }
            return;
        }
        if (e.getSource() == Open) {
            openFile();
        }
        else if (e.getSource() == Exit) {
            System.exit(ABORT);
        }
        else if (e.getSource() == Save) {
            saveFile();
        }
        else if (e.getSource() == Undo) {
            if (undoStack.size() > 1) {
                redoStack.push(undoStack.pop());
                PaneArea.setText(undoStack.peek());
            }
        }
        else if (e.getSource() == Redo) {
            if (redoStack.size() >= 1) {
                String text =  redoStack.pop();
                undoStack.push(text);
                PaneArea.setText(text);

            }
        }
        else if (e.getSource() == AutoCompleteButton) {
            String text = undoStack.pop(); // Get the text and remove
            String[] wordsArray = text.split("\\s+");
            String[] lastword = new String[1];
            lastword[0] = wordsArray[wordsArray.length - 1];
            ArrayList<String> words = AutoComplete.getAutoCompleteSuggestions(lastword);
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                result.append(word).append("\n"); // Append each word with a newline
            }
            Random random = new Random();
            String[] resultarray = result.toString().split("\n");
            String suggestion = resultarray[random.nextInt(resultarray.length)];
            System.out.println("This:" + suggestion);

            // Append the result to the PaneArea
            String[] currentText = PaneArea.getText().toString().split(" ");
            StringBuilder newText = new StringBuilder();
            for (String c : currentText) {
                if (c.equals(lastword[0])) {
                    newText.append(suggestion).append(" ");
                } else {
                    newText.append(c).append(" ");
                }
            }
            PaneArea.setText(newText.toString());
        }
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
                    PaneArea.setText("");
                    while (infile.hasNextLine()) {
                        String line = infile.nextLine() + "\n";
                        PaneArea.getDocument().insertString(PaneArea.getDocument().getLength(), line, null);
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
            undoStack.clear();
            redoStack.clear();
        }
    }

    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            File file;
            PrintWriter outfile = null;

            file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".txt");

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

    public static void main(String[] args) {
        new textEditor();
    }
}
