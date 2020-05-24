package pgdp.arrays;

public class GamesWithArrays {

  public static int[] otherSort(int[] arr, int[] arr2) {
    if(arr == null || arr2 == null || arr2.length > arr.length)
      throw new RuntimeException("undefined input");
    int[] sortedArray = new int[arr.length];
    boolean[] sorted = new boolean[arr.length]; //create boolean array to keep track of elements we will sort
    for (int i = 0; i < sorted.length; i++) { // fill the boolean array with false values
      sorted[i] = false;
    }

    int index = 0;
    for (int element2 : arr2) { // iterate through first array for each element of the second array
      for (int element1 = 0;  element1 < arr.length; element1++) {
        if(arr[element1] == element2){ //if element of first array is in the second we copy it to the sorted array
          sortedArray[index] = arr[element1];
          index++;
          sorted[element1] = true; //we keep a record of already sorted element by setting the value at the same index of our copy boolean array to true
        }
      }
    }

    for (int i = 0; i < sorted.length; i++) { //go through all elements in the boolean array
      if(!sorted[i]){ //find unsorted elements
        sortedArray[index] = arr[i]; //copy them to the sorted array
        index++;
      }

    }

    return sortedArray;
  }

  public static int[] fairFriends(int[] arr, int[] arr2) {

    if (arr == null && arr2 == null)
      throw new RuntimeException("undefined input");
    int sum1 = 0, sum2 = 0;

    //find total sum for both arrays, this way we know how many cookies we have in total
    if (arr != null && arr.length != 0){
      for (int cookie : arr) {
        sum1 += cookie;
      }

    }
    if (arr2 != null && arr2.length != 0) {
      for (int cookie : arr2) {
        sum2 += cookie;
      }
    }

    if (sum1 == 0){
      //if first friend has no cookies at all, we search for a cookie in the second friend's array
      for (int i = 0; i < arr2.length; i++) {
        if (sum2-arr2[i] == arr2[i]){ //we find a cookie that can be taken from the second friend and both friend have equal amount of cookies
          if (i > 0){ //simply check if we have more than one cookie in our array so we can acces
            return new int[]{arr2[i],arr2[i-1]};
          }
          if (i < arr2.length-1){
            return new int[]{arr2[i],arr2[i+1]};
          }
        }
      }
      return null; //no cookie found
    }
    if(sum2 == 0){ //if second friend has no cookies , take a cookie from first friend in that way that both friends have equal amount of cookies
      for (int i = 0; i < arr.length; i++) {
        if (sum1-arr[i] == arr[i]){
          if (i > 0){
            return new int[]{arr[i],arr[i-1]};
          }
          if (i < arr.length-1){
            return new int[]{arr[i],arr[i+1]};
          }
        }
      }
      //no cookie found - should never happen since there is always a possibility
      return null;
    }

    //both friends have some cookies that they can swap
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr2.length; j++) {


        //arr1[i] is cookie1 - a cookie from friend 1
        //arr2[j] is cookie2 - a cookie from friend 2

        //we "swap" cookies by manipulating total sum of cookies
        sum1 -= arr[i];
        sum1 += arr2[j];

        sum2 -= arr2[j];
        sum2 += arr[i];

        if (sum1 == sum2){ //first case: after swaping 2 cookies from both friends we achieve balance
          //swap in the original array
          int temp = arr[i];
          arr[i] = arr2[j];
          arr2[j] = temp;
          return new int[]{arr2[j],arr[i]}; //return the cookies that made the swap possible
        }

        //we let first friend keep all his cookies and give him extra another one that we take from the second friend
        sum1 += arr[i];
        sum2 -= arr[i];

        if (sum1 == sum2){ //second case: after stealing a cookie from second friend we achieve balance
          arr = addElement(arr,arr2[j]);
          arr2[j] = 0;
          arr2 = remove0s(arr2);
          return new int[]{arr[0],arr2[0]};
        }

        //we let second friend keep all his cookies and give him extra another one that we take from the first firend
        sum1 -= arr2[j];
        sum2 += arr2[j];

        sum1 -= arr[i];
        sum2 += arr[i];

        if (sum1 == sum2){ //third case: after stealing a cookie from first friend we achieve balance
          arr2 = addElement(arr2,arr[i]);
          arr[i]=0;
          arr = remove0s(arr);
          return new int[]{arr2[j],arr[i]};
        }
      }
    }

    return null;
  }

  //helper method- copies array given as parameter and returns an array that doesn't contain zeros
  private static int[] remove0s(int[] arr){
    int[] arrWithout0 = new int[arr.length-1];
    int counter = 0;
    for (int cookie : arr) {
      if(cookie != 0){
        arrWithout0[counter] = cookie;
        counter++;
      }

    }
    return arrWithout0;
  }

  //helper method - adds a new element into the array given as parameter and returns the same one with one element more
  private static int[] addElement(int[] arr, int element){
    int[] biggerArray = new int[arr.length+1];
    int counter = 0;
    for (int cookie : arr) {
      biggerArray[counter++] = cookie;
    }
    biggerArray[arr.length] = element;
    return biggerArray;
  }
  //end of helper methods

  public static boolean alpen(int[] arr) {
    if(arr == null || arr.length < 2 ){ //if array is null or has less than 2 elements we return false
      return false;
    }

    boolean goUp = true, goDown = false; //we declare & initialize 2 boolean variables to help us decide when the values are allowed to goUp(increase) and goDown(decrease)
    for (int i = 0; i < arr.length-1; i++) {

      if(goUp && arr[1] <= arr[0]){ //first case: array values start decreasing without increasing first
        return false;
      }

      if (goUp){ //array values up to this point have been increasing
        if (arr[i+1] < arr[i]){ // decrease is found
          goDown = true; //array values are only allowed to decrease
          goUp = false; //and not go up
          continue;

        } else if(arr[i+1] == arr[i]){ //a decrease or increase must be >= 1 so we have two values that are equal we stop and return false directly
          return false;
        }
      }
      if (goDown){ //array values now start decreasing
        if(arr[i+1] >= arr[i]){ //if increase after decrease is found we interrupt the program and return false
          return false;
        }
      }

      if(i == arr.length-2 && !goDown){ //if we reached end of array and values never decreased, return false
        return false;
      }
    }
    return true;
  }

  public static int[] plankton(int[] arr) {
    int lowestPrice, actualProfit, buyDay, sellDay;
    if(arr == null || arr.length < 2){//undefined since we can not buy and sell within a single day
      return new int[]{0,0,0};
    }
    else{
      lowestPrice = arr[0];
      actualProfit = 0;
      buyDay=0;
      sellDay = 0;
    }

    for (int i = 0; i < arr.length-1; i++) {
      int possibleSale = arr[i+1]-arr[i]; //calculate difference of prices between two consecutive days
      if (possibleSale > 0){

        //calculate new profit made from the day in index (i+1) and update the actual if necessary, otherwise continue
        int newProfit = arr[i+1]-lowestPrice;
        if (actualProfit > newProfit)
          continue;

        actualProfit = newProfit;
        sellDay = i+1; //when updating the actualProfti we also update that day we sold at
        continue;
      }

      //if new lower price at the (i+1)day is lower than lowest, then update lowest otherwise continue
      int lowerPrice = arr[i+1];
      if (lowestPrice < lowerPrice)
        continue;
      lowestPrice = lowerPrice;
      buyDay = i+1;//when updating lowestPrice means we buy in this day, therefore update buying day

    }
    if (sellDay == 0)
      buyDay = 0; //if we never sold, we should't have bought
    return new int[]{buyDay,sellDay,actualProfit}; //1,4,5
  }

  public static int pinguinFreunde(int[] arr) {
    if(arr == null || arr.length == 0) {
      return 0;
    }

    //we create an array of size 100 for each number that a pinguin may receive
    int[] pinguinGroups = new int[100];
    for (int pinguin : arr) { //we go through all pinguins in the array and order them into groups
      pinguinGroups[pinguin]++;
    }

    int expectedSize = 0; //saves biggest size of a pinguin group where an equal separation is possible
    boolean minSizeFound = false; //becomes true when we find how big expectedSize should be
    for (int i = 0; i < pinguinGroups.length; i++) {
      if (pinguinGroups[i] == 0) //we pass all groups that have 0 pinguins
        continue;
      if(pinguinGroups[i] == 1 ) // we know that a group of pinguins must contain minimaly two pinguins, in this case we return 0
        return 0;

      if(pinguinGroups[i] > expectedSize && !minSizeFound ){ //first pinguin group of size bigger than two sets the standard of the size that other groups must have, a bigger group would exclude the actual one
        expectedSize = pinguinGroups[i];
        minSizeFound = true;
      }

      if(pinguinGroups[i] % expectedSize != 0) //large pinguin groups must be disolve to smaller groups of expectedSize otherwise we return 0
        return 0;
    }

    return expectedSize;
  }
}
