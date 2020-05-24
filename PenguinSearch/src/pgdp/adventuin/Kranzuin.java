package pgdp.adventuin;

import pgdp.color.ExceptionUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Kranzuin {

    public static final Kranzuin JULIAN = new Kranzuin();

    private Kranzuin(){ }

    public int beantworteFragen(List<String> questions) {
        if (questions == null)
            ExceptionUtil.unsupportedOperation("questions should not be null");
        if (questions.size() == 0)
            return Integer.MAX_VALUE;

        //check if we at least have one relevant question in our question list
        //return 15 + length of their longest
        //otherwise continue
        boolean atLeastOneRelevantQuestion = questions.stream().anyMatch(s -> s.matches(".*(Blätter|Aufgaben|Anzahl)+.*"));
        if (!atLeastOneRelevantQuestion) {
            int longestQuestion = questions.stream().max(Comparator.comparing(String::length)).get().length();
            return 15 + longestQuestion;
        }


        //create list of Long called numbers to save positive numbers we extract from all the questions
        List<Long> numbers = new LinkedList<>();

        //using a parallelStream to process faster (in parallel)
        //we keep only questions that include a specific word in them
        //we than eliminate all questions that contain negative numbers or 0s
        //after having filtered all questions that match requirements we extract numbers in them
        questions.stream().filter(s -> s.matches(".*(Blätter|Aufgaben|Anzahl)+.*"))
                .filter(s -> !s.matches(".*(-\\d+|0+).*")) //filter out questions that contain negative numbers or zeros
                .forEach(e -> {
                    String[] extractedNumbers = e.split("\\D+"); //we get an array of numbers as we split the string on non-digit parts

                    //we than go through the numbers of the question
                    //we skip the first one since is an empty string
                    //we map each string number to a long
                    //we keep only numbers that can fit into integers
                    //we box them so we can compare and sort
                    //and at last we collect them to a list
                    List<Long> positiveIntegers = Stream.of(extractedNumbers)
                            .skip(1)
                            .mapToLong(Long::parseLong)
                            .filter( x -> x <= Integer.MAX_VALUE) //filter-in only numbers that can be integers
                            .boxed()
                            .sorted()
                            .collect(Collectors.toList());

                    long size = positiveIntegers.size();
                    if (size == 0)
                        return; //we simply return and continue with the next questions if this question had no numbers in it

                        //we add extracted number to the list of numbers
                    else if (size == 1)
                        numbers.add(positiveIntegers.get(0));

                        //when more than two numbers were extracted
                        //we add their middle value to the list of numbers
                    else if (size > 2) {
                        long minL = positiveIntegers.get(0);
                        long maxL = positiveIntegers.get(positiveIntegers.size() - 1);
                        numbers.add((minL + maxL) / 2);
                    }

                });

        //we sort the list of all extracted numbers from all questions
        Collections.sort(numbers);

        //when their size is 0 we return the first random number that divides a length of a question without remainder
        if (numbers.size() == 0) {
            //we generate stream of ints between values 15 and Int.Max_Val
            IntStream stream = ThreadLocalRandom.current().ints(15, Integer.MAX_VALUE);

            //we keep those who divide length of any question (not important which) with remainder of zero and return the first number
            return stream.filter(x -> questions.stream().anyMatch(y -> x % y.length() == 0)).findFirst().getAsInt();
        } else {

            //when list of numbers is not empty we check for a number smaller than 15
            boolean smaller15 = numbers.stream().anyMatch( x -> x < 15);
            if (smaller15)
                return 1783;

            //all numbers are bigger than 15 so we return the smallest
            //list is already sorted in increasing order so the smallest is the first element
            long smallestNr = numbers.stream().findFirst().get();
            return (int) smallestNr;
        }

    }

}