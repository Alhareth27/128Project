import javax.swing.text.Style;

public class TextWithStyle {
    private String text;
    private Style style; // Assuming Style is a class representing the style information

    public TextWithStyle(String text, Style style) {
        this.text = text;
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
