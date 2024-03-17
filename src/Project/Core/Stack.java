package Project.Core;

// Stack class from the algorithm class!
public class Stack {
	private final int maxSize;
    private int currentSize = 0;

    private final Object[] stack;

    public Stack(int maxSize) {
    	this.maxSize = maxSize;
    	stack = new Object[maxSize];
    }
    
    public int size() {
        return currentSize;
    }

    public int push(Object data) {
        stack[currentSize++] = data;
        return currentSize;
    }

    public Object pop() {
        if (isEmpty()) return null;
        currentSize--;
        Object data = stack[currentSize];
        stack[currentSize] = null;
        return data;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public Object peek() {
        return stack[currentSize - 1];
    }
}
