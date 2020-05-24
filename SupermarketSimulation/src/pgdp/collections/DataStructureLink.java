package pgdp.collections;

public class DataStructureLink<T> {

    private DataStructureConnector<T> dsc1, dsc2;

    public DataStructureLink(DataStructureConnector<T> dsc1, DataStructureConnector<T> dsc2) {
        this.dsc1 = dsc1;
        this.dsc2 = dsc2;
    }

    public boolean  moveNextFromAToB() {
        if (!dsc1.hasNextElement())
            return false;
        T removedElement = dsc1.removeNextElement();
        dsc2.addElement(removedElement);
        return true;
    }

    public void moveAllFromAToB() {
        if (!dsc1.hasNextElement())
            return;
        while (dsc1.hasNextElement()) {
            T removedElement = dsc1.removeNextElement();
            dsc2.addElement(removedElement);
        }
    }
}
