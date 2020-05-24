package pgdp.collections;

public class PenguinCustomer {
    final private String name;
    private int money;
    private Stack<FishyProduct> products;

    public PenguinCustomer(String name, int initialMoney) {
        if (name == null)
            ExceptionUtil.illegalArgument("name of penguin customer can not be null");
        this.name = name;
        if (initialMoney < 0)
            ExceptionUtil.illegalArgument("inital money of penguin customer can not be negative");
        this.money = initialMoney;
        products = new LinkedStack<>();
    }

    public void addProductToBasket(FishyProduct product) {
        products.push(product);
    }

    public void placeAllProductsOnBand(Queue<FishyProduct> productQueue) {
        DataStructureLink<FishyProduct> dsl = new DataStructureLink<>(new StackConnector<>(products), new QueueConnector<>(productQueue));
        dsl.moveAllFromAToB();
    }

    public void takeAllProductsFromBand(Queue<FishyProduct> productQueue) {
        DataStructureLink<FishyProduct> dsl = new DataStructureLink<>(new QueueConnector<>(productQueue), new StackConnector<>(products));
        dsl.moveAllFromAToB();
    }

    public void pay(int cost) {
        if (cost < 0)
            ExceptionUtil.illegalArgument("cost of products should not be negative");
        if (money - cost < 0)
            ExceptionUtil.unsupportedOperation("penguin can not have debts. cost was higher than their money");
        money -= cost;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Stack<FishyProduct> getProducts() {
        return products;
    }

    public void goToCheckout(PenguinSupermarket supermarket) {
        //add @this customer to the checkout with the smallest queue of @supermarket
        supermarket.getCheckoutWithSmallestQueue().addCustomerToQueue(this);
    }

    //returns the name of the cusomer followed by the amount of his money followed by a list of his products
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Customer name: ");
        output.append(name);
        output.append(", money: ");
        output.append(money);
        output.append(" , Products: [");
        //since we iterate through his products and we pop elements from the original stack
        //we add them right back in into a copyStack and update the empty one in the end
        Stack<FishyProduct> copyOfProducts = new LinkedStack<>();
        while (!products.isEmpty()) {
            FishyProduct product = products.pop();
            output.append(product.getName());
            copyOfProducts.push(product);
            if (!products.isEmpty())
                output.append(", ");
        }
        output.append("]");
        products = copyOfProducts;
        return output.toString();
    }
}
