
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class textEditor extends JFrame implements ActionListener {

    private static final Dimension WINDOW_SIZE = new Dimension(1000, 1000);
    private static final Dimension SCROLL_BOUNDS = new Dimension(900, 750);
    private static final int DEFAULT_FONT_SIZE = 30;

    private JTextPane PaneArea;

    private JButton fontcolor;
    private JComboBox<String> fontList;

    private JButton bold;
    private JButton AutoCompleteButton;
    private JButton italic;

    private JMenuBar MenuBar;
    private JMenuItem Open;
    private JMenuItem Exit;
    private JMenuItem Save;
    private JMenuItem Undo;
    private JMenuItem Redo;

    // public UndoManager undoManager;
    // boolean actionIsPerformed = false;
    public static SizedStack<String> undoStack;
    public SizedStack<String> redoStack;

    KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    private FormattingOptions format;   
    
    textEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(WINDOW_SIZE);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        setUpTextArea();
        format = new FormattingOptions(PaneArea);

        setUpMenuBar();
        bold = setUpButton("Bold");
        AutoCompleteButton = setUpButton("AutoComplete");
        italic = setUpButton("Italics");
        fontcolor = setUpButton("Text Color");

        setUpFontSize();
        setUpFontStyle();

        addScrollBar(PaneArea);
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

            System.out.println("=================");
            System.out.println(Arrays.toString(undoStack.toArray()));
            System.out.println(Arrays.toString(redoStack.toArray()));
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

        // Enable word wrap
        StyledDocument doc = PaneArea.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attrs, 0.5f);
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);

        JScrollPane scrollPane = new JScrollPane(PaneArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        PaneArea.addKeyListener(new KeyListener() {
            //Unnecessary methods
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}

            public void keyTyped(KeyEvent e) { 
                if (!redoStack.isEmpty()) {
                    redoStack.clear(); //Newly typed keys will conflict with stored redos
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
                if ((int) fontSpinner.getValue() > 4) {
                    format.setFontSize((int) fontSpinner.getValue());
                }
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
        Open = setUpMenuItem("Open", menu);
        Save = setUpMenuItem("Save", menu);
        Exit = setUpMenuItem("Exit", menu);
        Undo = setUpMenuItem("Undo", menu);
        Redo = setUpMenuItem("Redo", menu);

        MenuBar.add(menu);
        this.setJMenuBar(MenuBar);
        Undo.setAccelerator(undoKeyStroke);
        // Redo.setAccelerator(redoKeyStroke);
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
        // Check if there is selected text and it's not empty
        if (PaneArea.getSelectedText() != null) {

            if (e.getSource() == fontcolor) {
                Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
                format.setTextColor(color);
            }
            else if (e.getSource() == fontList) {
                Font font = new Font((String) fontList.getSelectedItem(), Font.PLAIN, PaneArea.getFont().getSize());
                format.setFontStyle(font);
            }
            else if (e.getSource() == italic) {
                format.setItalic();
            }
            else if (e.getSource() == bold) {
                format.setBold();
            }
        }
        if (e.getSource() == Open) {
            format.openFile();
        }
        else if (e.getSource() == Exit) {
            System.exit(ABORT);
        }
        else if (e.getSource() == Save) {
            format.saveFile();
        }
        else if (e.getSource() == Undo) {
            String text = undoStack.pop();
            redoStack.push(text);
            PaneArea.setText(undoStack.peek());
        }
        else if (e.getSource() == Redo) {
            PaneArea.setText(redoStack.pop());
        }
        else if (e.getSource() == AutoCompleteButton) {
            String text = undoStack.get(undoStack.indexOf(undoStack.lastElement())); // Get the text and remove
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

    public static void main(String[] args) {
        new textEditor();
    }

    public JTextPane getPaneArea() {
        return PaneArea;
    }

}
