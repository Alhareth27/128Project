// import java.awt.Window.Type;

// import javax.swing.JTextPane;
// import javax.swing.text.AttributeSet;
// import javax.swing.text.StyledDocument;

// public class TextWithStyle {
// private String text;
// private AttributeSet style;

// public TextWithStyle(String text, AttributeSet style) {
// this.text = text;
// this.style = style;
// }

// public String getText() {
// return text;
// }

// public void setText(String text) {
// this.text = text;
// }

// public AttributeSet getStyle() {
// return style;
// }

// public void setStyle(AttributeSet style) {
// this.style = style;
// }

// // Static method to create a TextWithStyle object from a JTextPane
// public static TextWithStyle fromJTextPane(JTextPane textPane) {
// StyledDocument doc = textPane.getStyledDocument();
// System.out.println("===================");
// System.out.println(doc);
// System.out.println("===================");
// String text = textPane.getText();
// AttributeSet style = doc.getCharacterElement(0).getAttributes();
// return new TextWithStyle(text, style);
// }

// // Apply TextWithStyle to a JTextPane
// public void applyToJTextPane(JTextPane textPane) {
// textPane.setText(text);
// StyledDocument doc = textPane.getStyledDocument();
// doc.setCharacterAttributes(0, text.length(), style, false);
// }
// }
