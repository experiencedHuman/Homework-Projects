package pgdp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataScience {
  public static Stream<Penguin> getDataByTrackId(Stream<PenguinData> stream) {

    //first we map all locations with the same trackID into a map
    //key is trackID
    //value is the list of all Geo objects (Locations) that have the same trackID
    Map<String, List<Geo>> map = stream.collect(Collectors.groupingBy(PenguinData::getTrackID,
                                                                      Collectors.mapping(PenguinData::getGeom, Collectors.toList())));

    //from the map we use the stream from the entry set and from each entry we create a new Penguin object,
    // that will be mapped into the stream
    return map.entrySet()
              .stream()
              .map(e -> new Penguin(e.getValue(), e.getKey()));
  }

  public static void outputPenguinStream() {
    //get nr of penguins
    long nr = getDataByTrackId(CSVReading.processInputFile()).count();
    System.out.println(nr);


    getDataByTrackId(CSVReading.processInputFile()) //we read the file
            .sorted(new Comparator<Penguin>() { //we sort the elements based on their trackIDs
              @Override
              public int compare(Penguin o1, Penguin o2) {
                //We get the IDs for both penguins and compare them in increasing order
                //we get the IDs by using the IntStream that we get from the characters of the TrackID
                //we skip the first 4 characters as they are not relevant to the comparison
                //we then take the sum of the ascii values
                //this way we can correctly compare concatenation of both numbers and strings
                int idP1 = o1.getTrackID()
                             .chars()
                             .skip(4)
                             .sum();

                int idP2 = o2.getTrackID()
                             .chars()
                             .skip(4)
                             .sum();

                return idP1 - idP2;
              }
            })
            .forEach(e -> System.out.println(e.toStringUsingStreams()));
  }

  public static void outputLocationRangePerTrackid(Stream<PenguinData> stream) {
    //we create a map where each PenguinData object is mapped to a key TrackID
    //key is TrackID
    //value is the list of all PenguinData objects that have the same trackID
    Map<String, List<PenguinData>> data = stream.collect(Collectors.groupingBy(PenguinData::getTrackID,
                                                                              Collectors.mapping(a -> a, Collectors.toList())));

    //from the stream that entrySet() offers we calculate average values for all locations and print them afterwards as required
    data.entrySet().stream().forEach( e -> {
      String trackid = e.getKey();
      double minLong = e.getValue().stream().mapToDouble(p -> p.getGeom().getLongitude()).min().getAsDouble();
      double maxLong = e.getValue().stream().mapToDouble(p -> p.getGeom().getLongitude()).max().getAsDouble();
      double avgLong = e.getValue().stream().mapToDouble(p -> p.getGeom().getLongitude()).average().getAsDouble();

      double minLat = e.getValue().stream().mapToDouble(p -> p.getGeom().getLatitude()).min().getAsDouble();
      double maxLat = e.getValue().stream().mapToDouble(p -> p.getGeom().getLatitude()).max().getAsDouble();
      double avgLat = e.getValue().stream().mapToDouble(p -> p.getGeom().getLatitude()).average().getAsDouble();

      System.out.println(trackid);
      System.out.println("Min Longitude: "+minLong+" Max Longitude: "+maxLong+" Avg Longitude: "+avgLong+
              " Min Latitude: "+minLat+" Max Latitude: "+maxLat+" Avg Latitude: "+avgLat);
    });
  }

}