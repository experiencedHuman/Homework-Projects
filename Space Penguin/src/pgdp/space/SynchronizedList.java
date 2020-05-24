package pgdp.space;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SynchronizedList<Spaceuin> {
    private List<Spaceuin> innerList = new LinkedList<>();
    private RW lock = new RW();
    private int index = 0;

    public void add(Spaceuin e) throws InterruptedException {
        lock.startWrite();
        innerList.add(index,e);
        index++;
        lock.endWrite();
    }

    public void remove(Spaceuin beacon) throws InterruptedException {
        lock.startWrite();
        index--;
        innerList.remove(beacon);
        lock.endWrite();
    }

    public Spaceuin get(int index) throws InterruptedException {
        lock.startRead();
        Spaceuin result = innerList.get(index);
        lock.endRead();
        return result;
    }

    public boolean contains(Spaceuin e) throws InterruptedException {
        lock.startRead();
        boolean result = innerList.contains(e);
        lock.endRead();
        return result;
    }

    public Iterator<Spaceuin> iterator() {
        return innerList.iterator();
    }

}