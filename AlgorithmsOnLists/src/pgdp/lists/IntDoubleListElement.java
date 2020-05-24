package pgdp.lists;

public class IntDoubleListElement {
    private int info;

    public void setInfo(int inf) {
        info = inf;
    }

    public int getInfo() {
        return info;
    }

    public IntDoubleListElement next;
    public IntDoubleListElement prev;

    public IntDoubleListElement(int startInfo) {
        info = startInfo;
        next = null;
        prev = null;
    }
    
    public boolean isEqual(IntDoubleListElement other) {
        return other != null && info == other.info;
    }

    //method from the Lecture EIDI - WS1920 (folien-9, seite 389)
    public IntDoubleListElement half() {
        int n = length();
        IntDoubleListElement t = this;
        for (int i = 0; i < n / 2 - 1; i++) {
            t = t.next;
        }
        IntDoubleListElement result = t.next;
        t.next = null;
        return result;
    }

    //helper method- returns length of the list that @this element points to
    public int length() {
        int counter = 0;
        IntDoubleListElement temp = this;
        while (temp != null) {
            counter++;
            temp = temp.next;
        }
        return counter;
    }
}
