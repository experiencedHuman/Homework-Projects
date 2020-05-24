package pgdp.collections;

public class LinkedQueue<T> implements Queue<T> {
    private int size = 0; //keeps count of the queue size
    private List<T> queueHead; //head points to the first added element
    private List<T> queueTail; //tail points to the last added element

    @Override
    public void enqueue(T obj) {
        //when queue is empty we set both pointers to point at the single element
        if (isEmpty()) {
            queueHead = new List<>(obj);
            queueTail = queueHead;
        }
        //otherwise we update only tail to point to the last element
        else {
            queueTail.addNextElement(new List<>(obj));
            queueTail = queueTail.getNext();
        }
        size++;
    }

    @Override
    public T dequeue() {
        if (queueHead == null)
            return null;

        //since the queue is a FIFO and the head points to the first element
        //we take the first element and update head to point to the next element
        T res = queueHead.getInfo();;
        if (queueHead.getNext() == null) {
            queueHead = queueTail = null; //we reached end of queue so set both head and tail to null
        } else {
            queueHead = queueHead.getNext();
        }

        size--;
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return queueHead == null;
    }
}
