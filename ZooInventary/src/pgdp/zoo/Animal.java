package pgdp.zoo;

public class Animal {

    private int foodCosts;
    private String name;

    //constructor
    public Animal(String name, int foodCosts) {
        this.name = name;
        this.foodCosts = foodCosts;
    }

    //getters
    public int getFoodCosts() {
        return foodCosts;
    }

    public String getName() {
        return name;
    }

    //returns string represantation of this object
    public String toString() {
        return "(name: " + name + ", foodCosts: " + foodCosts + ")";
    }
}
