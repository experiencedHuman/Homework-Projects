package pgdp.arrays;

public class ProbabilisticSearch extends  MiniJava {
    /**
     * Binary Search aus der Vorlesung leicht abgewandelt
     */
    public static int[] find (int[] a, int x) {
        return find0(a, x, 0, a.length-1, 1);
    }

    public static int[] find0 (int[] a, int x, int n1, int n2, int numberOfSteps) {
        int t = (n1+n2)/2;
        if (a[t] == x)
            return new int[]{t, numberOfSteps};
        else if (n1 >= n2)
            return new int[]{-1, numberOfSteps};
        else if (x > a[t])
            return find0 (a,x,t+1,n2, numberOfSteps + 1);
        else if (n1 < t)
            return find0 (a,x,n1,t-1, numberOfSteps + 1);
        else return new int[]{-1, numberOfSteps};
    }

    public static int[] probalisticSearch(int[] arr, int value) {
        if (arr == null || arr.length == 0){
            return new int[]{-1,0};
        }

        //min is the first element, max is the last element of the array
        int min = arr[0], max = arr[arr.length-1];

        //calculate index by applying formula and rounding up the result
        float maximumDifference = max - min;
        float elements = arr.length - 1;
        float div1 = maximumDifference / elements;
        float valDif = value - min;
        float position = valDif / div1;
        int index = Math.round(position);

        //if index is not in between array's bounds we return index not found in 0 steps
        if(index >= arr.length || index < 0){
            return new int[]{-1,0};
        }

        //if index is the last element and its value is bigger than max or if index is 0 and our value is smaller than min we return index not found in 0 steps
        if (index == arr.length - 1 && value > arr[index] || index == 0 && value < arr[index]) {
            return new int[]{-1,0};
        }

        //if value found in first step return index and step 1
        if (arr[index] == value){
            return new int[]{index,1};
        }

        //to count steps
        int calls = 0;

        //helper variables to calculate index jumps
        int power = 0, upperLimit = 0, lowerLimit = 0;
        boolean upperLimitFound = false, lowerLimitFound = false;

        while (true) {

            //value is found
            if (validIndex(arr.length,index) && arr[index] == value) {
                calls++;
                return new int[]{index, calls};
            }

            //value is smaller than the index we calculated so we go left
            if (arr[index] > value) {

                //if we already went left means at this step we call binarySearch
                if (lowerLimitFound) {
                    ++calls;
                    return find0(arr,value,lowerLimit+1,index-1, calls);
                }

                //otherwise go left into this new calculated index
                index = index - powerOf2(2,power);

                //if we can not go anymore left we mark this position and upperLimitFound as true
                if (index < 0 || arr[index] < value) {
                    upperLimit = index + powerOf2(2,power);
                    upperLimitFound = true;
                }

                if(index < 0)
                    index = 0; //index can not be smaller than 0

                ++power;
                ++calls;
            }


            //value is bigger than the index we calculated so we go right
            if (arr[index] < value) {

                //if we already went right(marked by upperLimitFound) means at this step we call binarySearch
                if (upperLimitFound) {
                    ++calls;
                    return find0(arr,value,index+1,upperLimit-1, calls);
                }

                //continue going right by increasing index
                index = index + powerOf2(2,power);

                //if we arrived end of array mark the current index and lowerLimitFound as true
                if (index >= arr.length || arr[index] > value) {
                    lowerLimit = index - powerOf2(2,power);
                    lowerLimitFound = true;
                }

                if (index >= arr.length) {
                    index = arr.length-1; //index can not be greater or equal than length of the array
                }
                ++power;
                ++calls;
            }

        }
    }
    
    public static void compareApproaches(int[] arr, int min, int max) {
        if (arr == null || arr.length == 0)
            return;

        //assuming min is smaller then max
        boolean correctInput = true;
        if (min > max)
            correctInput = false;

        //variables needed for the calls of binarySearch
        long maxCallsBinSea = 0;
        int valueWithMaxCallsBinSea = 0;
        long totalCallsBinSea = 0;

        //variables needed for the calls of probabilisticSearch
        long maxCallsProbSea = 0;
        int valueWithMaxCallsProbSea = 0;
        long totalCallsProbSea = 0;

        if (correctInput) {
            for (int value = min; value <= max; value++) {
                //save results for both searches
                int[] resBinSea = find(arr,value);
                int[] resProbSea = probalisticSearch(arr,value);

                //add total calls
                totalCallsBinSea += resBinSea[1];
                totalCallsProbSea += resProbSea[1];

                //find value with maxCalls for binarySearch
                if (maxCallsBinSea < resBinSea[1]) {
                    maxCallsBinSea = resBinSea[1];
                    valueWithMaxCallsBinSea = value;
                }

                //find value with maxCalls for probabilisticSearch
                if (maxCallsProbSea < resProbSea[1]) {
                    maxCallsProbSea = resProbSea[1];
                    valueWithMaxCallsProbSea = value;
                }
            }
        }

        MiniJava.writeLineConsole("Binäre Suche:");
        MiniJava.writeLineConsole("Maximale Anzahl an Aufrufen:");
        MiniJava.writeLineConsole(maxCallsBinSea);
        MiniJava.writeLineConsole("Wert bei dem die maximale Anzahl an Aufrufen auftritt:");
        MiniJava.writeLineConsole(valueWithMaxCallsBinSea);
        MiniJava.writeLineConsole("Anzahl der gesamten Aufrufe:");
        MiniJava.writeLineConsole(totalCallsBinSea);

        MiniJava.writeLineConsole("Probabilistische Suche:");
        MiniJava.writeLineConsole("Maximale Anzahl an Aufrufen:");
        MiniJava.writeLineConsole(maxCallsProbSea);
        MiniJava.writeLineConsole("Wert bei dem die maximale Anzahl an Aufrufen auftritt:");
        MiniJava.writeLineConsole(valueWithMaxCallsProbSea);
        MiniJava.writeLineConsole("Anzahl der gesamten Aufrufe:");
        MiniJava.writeLineConsole(totalCallsProbSea);

    }

    public static void main(String[] args) {
        // Not part of the exercise but can be helpful for debugging purposes

//        int[] exampleArray = new int[]{6, 20, 22, 35, 51, 54, 59, 74, 77, 80, 87, 94, 97};

//        int sum = 0;
//        for (int nr = 6; nr <= 97; nr++) {
//            int[] result = probalisticSearch(exampleArray,nr);
//            System.out.println("number "+nr+" found at index "+result[0]+", nr of calls: "+result[1]);
//            sum += result[1];
//        }
//        System.out.println("sum = "+sum);

//        int[] exampleArray = new int[]{0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,3,100};
//        int[] result = probalisticSearch(exampleArray,101);
//        System.out.println(result[0]);
//        System.out.println(result[1]);

//        compareApproaches(exampleArray,5,100);

//        compareApproaches(exampleArray,6,97);
    }


    //helper Method, basically does the same thing as Math.pow()
    private static int powerOf2(int number, int power){
        if (power == 0)
            return 1;
        if(power == 1)
            return number;
        int res = 1;
        for (int pow = 1; pow <= power; pow++) {
            res *= number;
        }
        return res;
    }

    //helper Method, returns true if index is in between array's bounds, otherwise false
    private static boolean validIndex(int arrayLength, int index) {
        return index >= 0 && index <= arrayLength - 1 ;
    }
}
