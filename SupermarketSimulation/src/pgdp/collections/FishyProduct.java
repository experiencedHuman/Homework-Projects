package pgdp.collections;

public class FishyProduct {
    final private String name;
    final private int price;

    public FishyProduct(String name, int price) {
        if (name == null)
            ExceptionUtil.illegalArgument("name should not be null");
        this.name = name;

        if (price <= 0)
            ExceptionUtil.illegalArgument("price should not be less than or equal 0");
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    //returns name of the product followed by its price
    public String toString() {
        return "Product name: "+name+" , product price: "+price;
    }
}
