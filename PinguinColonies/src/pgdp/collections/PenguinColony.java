package pgdp.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class PenguinColony {

    private HashSet<Penguin> penguins;

    public PenguinColony(HashSet<Penguin> penguins) {
        this.penguins = penguins;
    }

    public HashSet<Penguin> getPenguins() {
        return penguins;
    }

    public void setPenguins(HashSet<Penguin> penguins) {
        this.penguins = penguins;
    }

    public void uniteColonies(PenguinColony otherColony) {
        penguins.addAll(otherColony.getPenguins()); //join penguins
        otherColony.setPenguins(new HashSet<>()); //empty the otherColony
    }

    public PenguinColony splitColony(Predicate<? super Penguin> pred) {
        //create new colony to save penguins that meet the condition
        HashSet<Penguin> newColony = new HashSet<>();
        Iterator<Penguin> colony = penguins.iterator();
        while (colony.hasNext()) {
            //we iterate through all penguins and check which meets the condition
            //add those penguins in the new colony
            Penguin penguin = colony.next();
            if (pred.test(penguin)) {
                newColony.add(penguin);
            }
        }
        penguins.removeAll(newColony);
        return new PenguinColony(newColony);
    }

    public Penguin findFirstFriend(LinkedList<Penguin> penguinFriends) {
        Iterator<Penguin> friends = penguinFriends.iterator();
        while (friends.hasNext()) {
            Penguin firstFriend = friends.next();
            if (penguins.contains(firstFriend))
                return firstFriend;
        }

        return null;
    }

    public boolean canFeedPenguinsWithProperty(Predicate<? super Penguin> pred, Set<Fish> fishes) {
        for (Penguin penguin : penguins) {
            if (!pred.test(penguin)) continue;
            if (!fishes.contains(penguin.getFavoriteFish())) {
                return false;
            }
        }
        return true;
    }

    public int computeSum(Function<? super Penguin, Integer> fun) {
        int sum = 0;
        for (Penguin penguin : penguins) {
            sum += fun.apply(penguin);
        }
        return sum;
    }

}
