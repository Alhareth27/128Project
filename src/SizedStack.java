import java.util.ArrayDeque;

public class SizedStack<T> extends ArrayDeque<T> {
    private int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public void push(T object) {
        // If the stack is too big, remove the oldest element to make room
        if (this.size() >= maxSize) {
            this.removeLast();
        }
        super.push(object);
    }

    @Override
    public String toString() {
        return super.toString() + ", with a max size of: " + maxSize;
    }
}