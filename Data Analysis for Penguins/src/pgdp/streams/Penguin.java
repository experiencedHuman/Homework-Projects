package pgdp.streams;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Penguin {
  private List<Geo> locations;
  private String trackID;

  public Penguin(List<Geo> locations, String trackID) {
    this.locations = locations;
    this.trackID = trackID;
  }

  @Override
  public String toString() {
    return "Penguin{" +
        "locations=" + locations +
        ", trackID='" + trackID + '\'' +
        '}';
  }

  public List<Geo> getLocations() {
    return locations;
  }

  public String getTrackID() {
    return trackID;
  }

  public String toStringUsingStreams() {
    //we create a list of Geo objects where we store sorted Locations
    //we sort in decreasing order
    //first we compare Latitudes and if they are equal we compare Longitudes
    List<Geo> sortedLocations = locations.stream().sorted(new Comparator<Geo>() {
      @Override
      public int compare(Geo o1, Geo o2) {
        if (o1.getLatitude() != o2.getLatitude()) {
          if (o1.getLatitude() < o2.getLatitude())
            return 1;
          else
            return -1;
        }
        if (o1.getLongitude() < o2.getLongitude())
          return 1;
        else if (o1.getLongitude() > o2.getLongitude())
          return -1;
        else
          return 0;
      }
    }).collect(Collectors.toList());
    return "Penguin{locations="+sortedLocations.toString()+", trackID='"+trackID+"'}";
  }
}
