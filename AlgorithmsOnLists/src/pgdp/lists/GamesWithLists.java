package pgdp.lists;

public class GamesWithLists {
  public static IntDoubleList reuniteLists(IntDoubleList[] arrayOfLists) {
    long arrLength = arrayOfLists.length;

    //joing all lists together to form one big list
    for (int i = 0; i < arrLength - 1; i++) {
//      if (arrayOfLists[i] == null)
//        continue;
        arrayOfLists[i].appendList(arrayOfLists[i+1]);
    }

    //mergeSort the list
    IntDoubleListElement head = sort(arrayOfLists[0].getFirstElement());

    //make list to save sorted elements
    IntDoubleList result = new IntDoubleList();
    result.setFirstElement(head);
    return result;
  }

  //iterative merge
  private static IntDoubleListElement itMerge(IntDoubleListElement leftHead, IntDoubleListElement rightHead) {

    //we need a mini list to make things little faster
    IntDoubleListElement head = null;
    IntDoubleListElement tail = null;

    if (canMerge(leftHead,rightHead)) {
      while (canMerge(leftHead,rightHead)) {
        //left element is smaller than the right so add him to tail and continue
        if (leftHead.getInfo() <= rightHead.getInfo()) {
          if (head == null) {
            head = leftHead;
            tail = leftHead;
          }else {
            tail.next = leftHead;
            tail = leftHead;
          }
          leftHead = leftHead.next;
          continue;
        }

        //the right element is smaller so add him to tail
        if (head == null) {
          head = rightHead;
          tail = rightHead;
        }else {
          tail.next = rightHead;
          tail = rightHead;
        }

        rightHead = rightHead.next;
      }

    } else { //we can not merge, meaning one of the pointers points to a null element
      if (leftHead == null)
        return rightHead;
      return leftHead;
    }

    //after merging, we have to check for remained elements in one of the two pointers
    if (leftHead == null){
      tail.next = rightHead;
      tail = rightHead;
    } else {
      tail.next = leftHead;
      tail = leftHead;
    }

    return head;
  }

  //helper method - returns true if both elements point to a not null element
  private static boolean canMerge(IntDoubleListElement a, IntDoubleListElement b) {
    return a != null && b != null;
  }

  //method from the Lecture EIDI - WS1920 (folien-9, seite 388)
  private static IntDoubleListElement sort(IntDoubleListElement head) {
    if (head == null || head.next == null)
      return head;

    IntDoubleListElement b = head.half();
    head = sort(head);
    b = sort(b);
    return itMerge(head,b); //call iterative merge
  }

  public static IntDoubleList partTheList(IntDoubleList list, int value) {
    if (list.size()==0 || list == null) {
      return new IntDoubleList();
    }

    //lists to save elements smaller than x and bigger than x
    IntDoubleList smallerX = new IntDoubleList();
    IntDoubleList biggerX = new IntDoubleList();

    int smallerXcounter = 0, biggerXcounter = 0;

    //pointer to iterate list
    IntDoubleListElement head = list.getFirstElement();
    while (head != null) {
      if (head.getInfo() < value) {
        //add elements smaller than x in smallerX
        smallerX.append(head.getInfo());
        smallerXcounter++;
      } else {
        //add elements bigger than x in biggerX
        biggerX.append(head.getInfo());
        biggerXcounter++;
      }
      head = head.next;
    }

    //check if one list is empty otherwise join them and return
    if (smallerXcounter == 0)
      return biggerX;
    else if (biggerXcounter == 0)
      return smallerX;
    else {
      smallerX.appendList(biggerX);
      return smallerX;
    }

  }

  public static IntDoubleList mixedList(IntDoubleList list) {

    //pointer to iterate list from start to end and from end to start
    IntDoubleListElement head = list.getFirstElement();
    IntDoubleListElement tail = list.getLastElement();

    int counter = 0, length = list.size();
    IntDoubleList result = new IntDoubleList();

    boolean oddLength = false;
    //determine whether the length of our list is an odd or even number
    if (length % 2 != 0)
      oddLength = true;

    while (counter < length/2) {
      //add elements that both pointer point to
      result.append(head.getInfo());
      result.append(tail.getInfo());

      //increment both pointers
      head = head.next;
      tail = tail.prev;
      ++counter;

      //if we reach middle and lenght is odd means we have to add only one elemnent and break
      if (counter == length/2 && oddLength){
        result.append(head.getInfo());
        break;
      }
    }

    return result;
  }
  
}
