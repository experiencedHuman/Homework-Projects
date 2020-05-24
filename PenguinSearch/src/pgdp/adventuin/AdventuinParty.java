package pgdp.adventuin;

import pgdp.color.RgbColor;

import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AdventuinParty {

    public static Map<HatType, List<Adventuin>> groupByHatType(List<Adventuin> adventuins) {
        return  adventuins.stream()
                          .collect(Collectors.groupingBy(Adventuin::getHatType));

    }

    public static void printLocalizedChristmasGreetings(List<Adventuin> adventuins) {
        //we sort adventuins based on their height in increasing order
        //and use system out to print greeting for everyone
        adventuins.stream().sorted(new Comparator<Adventuin>() {
            @Override
            public int compare(Adventuin o1, Adventuin o2) {
                return o1.getHeight() - o2.getHeight();
            }
        }).forEach(a -> System.out.println(a.getLanguage().getLocalizedChristmasGreeting(a.getName())));
    }

    public static Map<HatType, List<Adventuin>> getAdventuinsWithLongestNamesByHatType(List<Adventuin> adventuins) {

        //we first group adventuins by hat type into a map where key is the hattype and value all adventuins with that hattype
        //then we go through the entrySet stream and map adventuins to the same key
        //but to a different value in which from the original list of adventuins with same hattype
        //we keep only those whose namelength equals the longest namelength that an adventuin of that group can possibly have
        return adventuins.stream()
                         .collect(Collectors.groupingBy(Adventuin::getHatType))
                         .entrySet()
                         .stream()
                         .collect(Collectors.toMap( e -> e.getKey(), e -> e.getValue().stream()
                                 .filter(new Predicate<Adventuin>() {
                                     //we map names into integers and get the maximum so we can later compare all penguins
                                    int maxNameLength = e.getValue()
                                                         .stream()
                                                         .mapToInt(a -> a.getName().length())
                                                         .max()
                                                         .getAsInt();
                                   @Override
                                   public boolean test(Adventuin adventuin) {
                                       return adventuin.getName().length() == maxNameLength;
                                    }
                               }).collect(Collectors.toList())));

    }


    public static Map<Integer, Double> getAverageColorBrightnessByHeight(List<Adventuin> adventuins) {

        //we group penguins according to their height
        //key is height
        //value is average of the colors of the penguin
        return  adventuins.stream()
                          .collect(Collectors.groupingBy(e -> (int)Math.round(e.getHeight() / 10.0) * 10,
                                            Collectors.averagingDouble(e -> ( (0.2126 * e.getColor().getRed()) + (0.7152 * e.getColor().getGreen()) + (0.0722 * e.getColor().getBlue()) ) / 255.0)
                          ));
    }

    public static Map<HatType, Double> getDiffOfAvgHeightDiffsToPredecessorByHatType(List<Adventuin> adventuins) {

        //we first group the adventuins by hat type
        //then we go through the entries and calculate positive and negative differences
        return groupByHatType(adventuins).entrySet()
                  .stream().collect(Collectors.toMap(Entry::getKey, entry -> {
                    List<Adventuin> penguins = entry.getValue();

                    //we go through all the penguins in the list
                    //we map them to double values in which we calculate their height differences
                    //we get only those values who are positive
                    //at the end we get the sum
                    double positiveDifferences = IntStream.range(0, penguins.size())
                                                            .mapToDouble(i -> penguins.get(i).getHeight() - penguins.get(getCircularIndex(i - 1, penguins.size())).getHeight())
                                                            .filter(i -> i > 0.0)
                                                            .sum();

                    //we do the same as above for negative values
                    double negativeDifferences = IntStream.range(0, penguins.size() - 1)
                            .mapToDouble(i -> penguins.get(i).getHeight() - penguins.get(getCircularIndex(i - 1, penguins.size())).getHeight())
                            .filter(i -> i < 0.0)
                            .sum();


                    //calculate abosolute difference of average differences
                    return Math.abs((positiveDifferences / 2) - (negativeDifferences / 2));
                }));

    }


    //helper method for indexing stream/list elements
    //if index is negative it will return element from the end of the list
    private static int getCircularIndex(int index, int length) {
        if (index >= 0)
            return index;
        return index + length % (length + 1);
    }
}
