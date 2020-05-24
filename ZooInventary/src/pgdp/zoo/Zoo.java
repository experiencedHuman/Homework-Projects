package pgdp.zoo;

public class Zoo {
    private Vivarium[] vivaria;

    public Zoo(Vivarium[] vivaria) {
        this.vivaria = vivaria;
    }

    public String toString() {
        String ret = "{";
        for (int index = 0; index < vivaria.length - 1; index++) {
            ret += vivaria[index].toString()+ ", ";
        }
        ret += vivaria[vivaria.length - 1].toString();
        return ret + "}";
    }

    public int getCosts() {
        int sum = 0;
        for (Vivarium vivi: vivaria) {
            sum += vivi.getCosts();
        }
        return sum;
    }

}