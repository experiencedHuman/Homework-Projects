package pgdp.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileSearch {

    public static Result searchFile(Path file, String[] searched) {
        Result result = new Result(file);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.lines(file).collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }

        for (int i = 0; i < lines.size(); i++) {
            for (String pattern : searched) {
                if (lines.get(i).contains(pattern)) {
                    result.addMatch(new Match(i + 1, lines.get(i)));
                    break;
                }
            }
        }
        return result;
    }

    public static Set<Result> searchDirectory(Path directory, String searched[]) {
        Set<Result> results = new HashSet<>();
        try {
            Files.walk(directory).filter(path -> Files.isRegularFile(path))
                                .forEach(path -> {
                                    Result res = searchFile(path, searched);
                                    if (res != null)
                                        results.add(res);
                                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    public static List<Result> listResults(String directory, String searched[]) {
        Path p = Path.of(directory);
        List<Result> sortedResults = new ArrayList<>(searchDirectory(p,searched));
        Collections.sort(sortedResults, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o2.getMatches().size() - o1.getMatches().size();
            }
        });
        return sortedResults;
    }

    public static void main(String[] args) {
        if (args.length < 2)
            throw new RuntimeException("more than 2 arguments must be given");

        String[] searched = args[1].split(" ");
        List<Result> results = listResults(args[0], searched);

        for (Result result : results) {
            System.out.println(result.toString());
        }
        
    }

}
