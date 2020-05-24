package pgdp.collections;

public class Checkout {

    private Queue<PenguinCustomer> queue;
    private Queue<FishyProduct> bandBeforeCashier;
    private Queue<FishyProduct> bandAfterCashier;

    public Checkout() {
        queue = new LinkedQueue<>();
        bandBeforeCashier = new LinkedQueue<>();
        bandAfterCashier = new LinkedQueue<>();
    }

    public Queue<PenguinCustomer> getQueue() {
        return this.queue;
    }

    public Queue<FishyProduct> getBandBeforeCashier() {
        return bandBeforeCashier;
    }

    public Queue<FishyProduct> getBandAfterCashier() {
        return bandAfterCashier;
    }

    public int queueLength() {
        return queue.size();
    }

    public void serveNextCustomer() {
        if (queue.isEmpty())
            return;
        PenguinCustomer customer = queue.dequeue();
        customer.placeAllProductsOnBand(bandBeforeCashier);
        int totalCost = 0;
        while (!bandBeforeCashier.isEmpty()) {
            FishyProduct product = bandBeforeCashier.dequeue();
            //for each product processed we calculate total cost
            totalCost += product.getPrice();
            bandAfterCashier.enqueue(product);
        }
        customer.takeAllProductsFromBand(bandAfterCashier);
        customer.pay(totalCost);
    }

    //returns the customer line
    //for each customer we return its name, money and products
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Customers line: [");
        //since we iterate customers in the queue and dequeue them along the way
        //we enqueue them right back in, in a copy queue and update the 'old' empty one in the end
        Queue<PenguinCustomer> customerCopy = new LinkedQueue<>();
        while (!queue.isEmpty()) {
            PenguinCustomer customer = queue.dequeue();
            output.append(customer.toString());
            customerCopy.enqueue(customer);
            if (!queue.isEmpty())
                output.append(", ");
        }
        output.append("]");
        queue = customerCopy;
        return output.toString();
    }

    //helper method
    //adds @customer to this checkout's @queue
    public void addCustomerToQueue(PenguinCustomer customer) {
        queue.enqueue(customer);
    }
}
