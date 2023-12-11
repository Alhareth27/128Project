import java.util.ArrayDeque;

public class SizedStack<T> extends ArrayDeque<T> {
    private int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public void push(T object) {
        // If the stack is too big, remove elements until it's the right size.
        while (this.size() >= maxSize) {
            this.removeLast();
        }
        super.push(object);
    }
}