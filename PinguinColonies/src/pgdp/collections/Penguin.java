package pgdp.collections;

public class Penguin {
    private final int birthYear;
    private final String name;
    private final Gender gender;
    private Fish favoriteFish;

    public Penguin(int birthYear, String name, Gender gender, Fish favoriteFish) {
        this.birthYear = birthYear;
        this.name = name;
        this.gender = gender;
        this.favoriteFish = favoriteFish;
    }

    public boolean equals(Object other) {
        try {
            if (birthYear != ((Penguin)other).birthYear)
                return false;
            if (!name.equals(((Penguin)other).name))
                return false;
            if (!gender.equals(((Penguin)other).gender))
                return false;
        }catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hashValue = birthYear;
        if (name != null)
            for (Character letter : name.toCharArray())
                hashValue += letter;
        hashValue += gender == Gender.MALE ? 1 : 2;
        return hashValue;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Fish getFavoriteFish() {
        return favoriteFish;
    }

    public void setFavoriteFish(Fish favoriteFish) {
        this.favoriteFish = favoriteFish;
    }

}
