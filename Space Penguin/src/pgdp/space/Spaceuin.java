package pgdp.space;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Spaceuin extends Thread {

    private Beacon start, destination;
    private FlightRecorder flightRecorder;
    private Set<Beacon> visited;
    private static SynchronizedList<Spaceuin> spaceuins = new SynchronizedList<>();

    public Spaceuin(Beacon start, Beacon destination, FlightRecorder flightRecorder) {
        this.start = start;
        this.destination = destination;
        this.flightRecorder = flightRecorder;
        visited = new HashSet<>();
    }

    @Override
    public void run() {
        try {
            dfs(start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dfs(Beacon beacon) throws InterruptedException {
        synchronized (beacon) {
            if (interrupted()) {}
            if (beacon.equals(destination)) {
                flightRecorder.recordArrival(destination);
                flightRecorder.tellStory();
                Iterator<Spaceuin> it = spaceuins.iterator();
                while (it.hasNext()) {
                    Spaceuin t = it.next();
                    if (!t.isInterrupted()) {
                        t.interrupt();
                    }
                }
                return;
            }

            //Sackgasse
            if (beacon.connections().isEmpty())
                return;

            //Schleife
            if (visited.contains(beacon))
                return;

            visited.add(beacon);
            flightRecorder.recordArrival(beacon);

            List<BeaconConnection> connections = beacon.connections();
            for (BeaconConnection connection : connections) {
                if (!visited.contains(connection.beacon())) {
                    if (connection.type() == ConnectionType.WORMHOLE) {
                        FlightRecorder recorderCopy = flightRecorder.createCopy();
                        visited.add(connection.beacon());
                        Spaceuin clone = new Spaceuin(connection.beacon(),destination,recorderCopy);
                        clone.start();
                        spaceuins.add(clone);
                        Iterator<Spaceuin> it = spaceuins.iterator();
                        while (it.hasNext()) {
                            Spaceuin t = it.next();
                            if (!t.isInterrupted() && !t.equals(clone)) {
                                t.interrupt();
                            }
                        }
                        continue;
                    }
                    flightRecorder.recordDeparture(beacon);
                    dfs(connection.beacon());
                    flightRecorder.recordArrival(beacon);
                }
            }
            flightRecorder.recordDeparture(beacon);
        }
    }

    @Override
    public synchronized String toString() {
        return super.toString();
    }
}