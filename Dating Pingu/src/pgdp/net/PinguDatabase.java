package pgdp.net;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PinguDatabase {
    static Path dataFile = Path.of("db", "penguins.csv");
    private static final String FILE_ERROR = "Problem occurred while looking up into the database!";

    boolean add(DatingPingu datingPingu) {
        try {
            Optional<DatingPingu> pinguOptional = lookupById(datingPingu.getId());
            if (pinguOptional.isPresent())
                return false;
            String newRecord = "\n"+datingPingu.toCsvRow();
            Files.write(dataFile,newRecord.getBytes(),StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    Optional<DatingPingu> lookupById(long id) {
        Optional<DatingPingu> pingu;
        try {
            pingu = Files.lines(dataFile)
                         .filter(record -> Long.parseLong(record.substring(0,record.indexOf(","))) == id)
                         .findFirst() //or findAny() daa daa
                         .map(DatingPingu::parse);
            if (pingu.isPresent())
                return pingu;
        } catch (NumberFormatException canNotCastToLong) {
            return Optional.empty();

        } catch (IOException e) {
            System.out.println(FILE_ERROR);
        }
        return Optional.empty();
    }

    List<DatingPingu> findMatchesFor(SearchRequest request) {
        List<DatingPingu> romanticPingus = new ArrayList<>();
        try {
            return Files.lines(dataFile).filter(record -> {
                String[] pinguData = record.split(",");
                int pinguAge = Integer.parseInt(pinguData[3]);
                String pinguSexualOrientation = pinguData[2];
                List<String> pinguHobbys = List.of(pinguData[4].split(" "));
                boolean commonHobby = pinguHobbys.stream()
                                                 .anyMatch(pinguHobby -> request.getHobbies()
                                                     .stream()
                                                     .anyMatch(otherPinguHobby -> otherPinguHobby.equals(pinguHobby)));
                return  pinguAge >= request.getMinAge()
                     && pinguAge <= request.getMaxAge()
                     && pinguSexualOrientation.equals(request.getSexualOrientation())
                     && commonHobby;
            }).map(DatingPingu::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(FILE_ERROR);
        }
        return romanticPingus;
    }
}
