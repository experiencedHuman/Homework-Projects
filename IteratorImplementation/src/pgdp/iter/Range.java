package pgdp.iter;

import java.util.Iterator;

public class Range implements Iterable<Integer> {
    final private int begin, end, stride;

    public Range(int begin, int end, int stride) {
        this.begin = begin;
        this.end = end;
        if (stride <= 0)
            Util.badArgument("stride should not be less than or equal 0");
        this.stride = stride;
    }

    public Range(int begin, int end) {
        this.begin = begin;
        this.end = end;
        stride = 1;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int distance = begin, counter = 0;

            //return true as long as @distance is smaller or equal than @end when increased by @stride when we have an increasing range
            //return true as long as @distance is bigger or equal than @end when decreased by @stride when we have a decreasing range
            @Override
            public boolean hasNext() {
                if (distance < end && begin < end)
                    return distance + stride <= end;
                else if (distance > end && begin > end)
                    return distance + stride >= end;
                else if (distance == end && counter == 0){
                    return true;
                } else return false;
            }

            @Override
            public Integer next() {
                if (!hasNext())
                    Util.noSuchElement("Range has no more elements");
                if (begin == end) {
                    distance += stride;
                    return begin;
                }
                else if (begin < end) {
                    distance += counter;
                    counter = stride;
                    return distance;
                }
                else {
                    distance += counter;
                    counter = -stride; //we decrease, so we have to add a negative stride to the distance
                    return distance;
                }
            }
        };
    }

}
