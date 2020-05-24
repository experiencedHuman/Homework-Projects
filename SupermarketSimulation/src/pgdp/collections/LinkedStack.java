package pgdp.collections;

public class LinkedStack<T> implements Stack<T> {

    private List<T> stack;
    private int size = 0; //keeps count of the stack size

    @Override
    public void push(T obj) {
        if (isEmpty())
            stack = new List<>(obj);
        else
            //stack grows from right to left, so the very last element is in the far left
            stack = new List<>(obj,stack);

        size++;
    }

    @Override
    public T pop() {
        if (isEmpty())
            return null;

        //get the element and update stack
        T result = stack.getInfo();
        if (stack.getNext() != null)
            stack = stack.getNext();
        else
            stack = null;

        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return stack == null;
    }

}
