package pgdp.zoo;

public class Vivarium {
    private int area, constructionYear;
    private Animal[] inhabitants;

    //constructor
    public Vivarium(Animal[] inhabitants, int area, int constructionYear) {
        this.inhabitants = inhabitants;
        this.area = area;
        this.constructionYear = constructionYear;
    }

    public String toString() {
        String ret = "[area: " + area + ", constructionYear: " + constructionYear + ", animals: ";
        for (int index = 0; index < inhabitants.length - 1; index++) {
            ret += inhabitants[index].toString() + ", ";
        }
        ret += inhabitants[inhabitants.length-1].toString();
        return ret + "]";
    }

    public int getCosts() {
        int sumFoodCosts = 0;

        //calculate food costs for all inhabitants
        for (Animal inhabitant: inhabitants) {
            sumFoodCosts += inhabitant.getFoodCosts();
        }
        return 900 + (area * 100) + (area * (2019 - constructionYear) * 5) + sumFoodCosts;
    }
}
