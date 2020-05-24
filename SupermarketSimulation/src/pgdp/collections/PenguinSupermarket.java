package pgdp.collections;

public class PenguinSupermarket {
    private Checkout[] checkouts;

    public PenguinSupermarket(int checkoutsNumber) {
        if (checkoutsNumber <= 0)
            ExceptionUtil.illegalArgument("Checkout's number must be greater than 0");
        checkouts = new Checkout[checkoutsNumber];
        //initialize all checkouts
        for (int i = 0; i < checkoutsNumber; i++) {
            checkouts[i] = new Checkout();
        }
    }

    public Checkout[] getCheckouts() {
        return checkouts;
    }

    //return checkout with smallest queue
    public Checkout getCheckoutWithSmallestQueue() {
        Checkout ret = checkouts[0];
        for (int i = 0; i < checkouts.length; i++) {
            if (checkouts[i].queueLength() < ret.queueLength())
                ret = checkouts[i];
        }
        return ret;
    }

    public void closeCheckout(int index) {
        if (index == checkouts.length - 1 && checkouts.length == 1) {
            throw new RuntimeException("closing last checkout!");
        }
        if (index < 0 || index >= checkouts.length) {
            ExceptionUtil.illegalArgument("checkout at index "+index+" doesn't exist");
        }

        //we use a dataStructureLink by moving all customers from the checkout queue to a stack
        QueueConnector<PenguinCustomer> pqc = new QueueConnector<>(checkouts[index].getQueue());
        Stack<PenguinCustomer> emptyCustomerStack = new LinkedStack<>();
        StackConnector<PenguinCustomer> psc = new StackConnector<>(emptyCustomerStack);
        DataStructureLink dsl = new DataStructureLink(pqc,psc);
        dsl.moveAllFromAToB();

        //we close the checkout after having all customers moved to a temporary stack
        checkouts[index] = null;
        updateCheckouts(index);

        //we send each customer (from last to first) to the other checkouts of @this supermarket
        while (psc.hasNextElement()) {
            PenguinCustomer customer = psc.removeNextElement();
            customer.goToCheckout(this);
        }
    }

    //helper method
    //makes array of checkouts smaller after having closed one
    //copies all opened checkouts to a new array of a smaller size
    private void updateCheckouts(int index) {
        Checkout[] newArray = new Checkout[checkouts.length - 1];
        int counter = 0;
        for (int i = 0; i < checkouts.length; i++) {
            if (i == index) continue;
            newArray[counter++] = checkouts[i];
        }
        checkouts = newArray;
    }

    public void serveCustomers() {
        for (Checkout checkout : checkouts) {
            checkout.serveNextCustomer();
        }
    }
}
