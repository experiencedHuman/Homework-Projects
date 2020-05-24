package pgdp.collections;

public interface DataStructureConnector<T> {
    boolean hasNextElement();
    void addElement(T obj);
    T removeNextElement();
}
