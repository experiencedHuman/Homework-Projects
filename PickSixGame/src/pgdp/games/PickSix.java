package pgdp.games;

public class PickSix extends MiniJava {
  // sorting method from the lecture
  public static int[] sort(int[] a) {
    int[] b = new int[a.length];
    for (int i = 0; i < a.length; ++i) {
      // begin of insert
      int j = 0;
      while (j < i && a[i] > b[j]) ++j;
      // end of locate
      for (int k = i - 1; k >= j; --k) b[k + 1] = b[k];
      // end of shift
      b[j] = a[i];
      // end of insert
    }
    return b;
  } // end of sort

  private static int[][] gameCards; //matrix to save cards for players
  private static int[][] cardStack; //matrix to save stack of cards
  private static int[] playerPoints; //array to save points of players

  public static void main(String[] args) throws IllegalAccessException {

    gameCards = new int[2][10]; //2 players with 10 cards each
    cardStack = new int[4][5]; //4 stacks, each can accept at most 5 cards at a time
    playerPoints = new int[2]; // index [0] -> points of first player, [1] -> points of second player

    givePlayerCards(gameCards);

    //fill 4 stacks with one card each
    for (int row = 0; row < cardStack.length; row++) {
      for (int col = 0; col < 1; col++) {
        cardStack[row][col] = MiniJava.drawCard();
      }
    }

    //sort stack vertically increasing at column with index 0
    sortStack(0);

    outputStapel(cardStack);

    for (int round = 0; round < 10; round++) {

      int cardPlayer1 = playerSelectCard(1,gameCards); //card chosen by first player
      int cardPlayer2 = playerSelectCard(2,gameCards);//card chosen by second player

      //insert cards in that order that the playwer who chose the smallest card gets to insert first
      if(cardPlayer1 < cardPlayer2){
        insert(cardPlayer1,1);
        insert(cardPlayer2,2);

      }else {
        insert(cardPlayer2,2);
        insert(cardPlayer1,1);
      }
      outputStapel(cardStack);

    }

    outputResult(new int[]{playerPoints[0],playerPoints[1]});
  }

  //inserts card into cardStack
  private static void insert(int card, int fromPlayer) {

    for (int col = 0; col < 1; col++) {
      for (int row = 0; row < cardStack.length-1; row++) { //all these two for loops do is iterate the first column of our cardStack

        int difference = card - cardStack[row][col]; //calculate difference between cards

        if (difference < 0){ //rule 4
          playerPoints[fromPlayer-1] += calculatePoints(cardStack[0]); //player takes all cards of first stack
          makeRow0(row); //empty the row in which the player took all cards
          cardStack[row][0] = card; //add played card on top of first stack
          return;
        }

        if (card > cardStack[row+1][col] && row == cardStack.length-2){ //rule 1. adding the biggest card means we add it on top of the last stack
          addOnTop(card,row+1);
          return;
        }

        if(card > cardStack[row+1][col]) //we haven't found card with smalles difference yet so we continue
          continue;
        if(stackFull(cardStack[row])){ // rule 3
          playerPoints[fromPlayer-1] += calculatePoints(cardStack[row]); //player takes all 5 cards
          makeRow0(row); //empty the row in which player took the cards
          cardStack[row][0] = card; //add card on top of affected stack
        }

        //rule 2
        addOnTop(card,row);
        return;
      }
    }

  }

  //helper method - adds one card @element at the top of stack at @row
  private static void addOnTop(int element,int row){
    for (int i = 4; i > 0; i--) {
      cardStack[row][i] = cardStack[row][i-1];
    }
    cardStack[row][0] = element;
  }

  //helper method - if player has to take all cards we have to fill stack with zeros
  private static void makeRow0(int row){
    for (int i = 0; i < 5; i++) {
      cardStack[row][i] = 0;
    }
  }

  /**
   * checks if a specific stack is full
   * @param stack we want to test
   * @return true if stack full, otherwise false
   */
  private static boolean stackFull(int[] stack){
    for (int el : stack) {
      if (el == 0)
        return false;
    }
    return true;
  }

  /**
   * sorts a column of the cards stack based on index = column
   * @param index column we want to sort
   */
  private static void sortStack(int index){

    int[] col = new int[cardStack.length]; //create array col to keep values of a given column of the @cardStack
    int counter = 0;

    //copy all values of the column at @index into the col array
    for (int row = 0; row < cardStack.length; row++) {
      for (int column = index; column < index+1; column++) {
        col[counter++] = cardStack[row][column];
      }
    }

    int[] sortedCol = sort(col); //sort it increasing order
    int[] reversedCol = reverseArray(sortedCol); //reverse to achieve decreasing order when needed to output stacks

    for (int row = 0; row < cardStack.length; row++) {
      for (int column = index; column < index+1; column++) {
        cardStack[row][column] = reversedCol[--counter]; //update stacks with values that are sorted decreasingly
      }
    }

  }

  /**
   * changes array order by reversing it
   * @param arr array we want to reverse
   * @return reversed array
   */
  private static int[] reverseArray(int[] arr){
    int[] reversedArray = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      reversedArray[i] = arr[arr.length-1-i];
    }
    return reversedArray;
  }

  public static void outputStapel(int[][] stapel) {

    for (int row = 0; row < stapel.length; row++) {
      MiniJava.writeConsole("Stapel "+(row+1)+":");

      for (int column = 0; column < stapel[row].length; column++) {
        if (stapel[row][column] <= 0)
          break;
        MiniJava.writeConsole(" "+stapel[row][column]);
      }
      MiniJava.writeLineConsole();
    }
  }

  public static int playerSelectCard(int player, int[][] playerCards) {

    boolean askAgain = true;
    int choosenCard = 0;

    while (askAgain){
      MiniJava.writeLineConsole("Spieler "+(player+1)+", du hast die folgenden Karten:"+ printArray(playerCards[player-1]) );
      choosenCard = MiniJava.readInt("Welche Karte möchtest du ausspielen?");

      for (int i = 0; i < playerCards[player-1].length; i++) {
        if(playerCards[player-1][i] == choosenCard){
          playerCards[player-1][i] = 0; // if the card that the player choses is in his deck, we make it 0 and return the value otherwise continue prompting the player
          return choosenCard;
        }
      }

    }

    throw new RuntimeException();
  }

  /**
   * prints valid cards in a player's deck
   * @param arr card deck we want to print out
   * @return String with player's cards that are different from 0
   */
  private static String printArray(int[] arr){
    String output = "";
    for (int element : arr) {
      if (element == 0)
        continue;
      output += " "+element;
    }
    return output;
  }

  public static int calculatePoints(int[] lostCards) {
    int pointSum = 0;
    for (int card : lostCards) {
      pointSum += getValueOfCard(card);
    }
    return pointSum;
  }

  public static void outputResult(int[] playerPoints) {
    if (playerPoints.length > 2)
      return;
    // index [0] -> points of player 1
    // index [1] -> points of player 2
    if (playerPoints[0] < playerPoints[1]){
      MiniJava.writeLineConsole("Spieler 1 gewinnt mit "+playerPoints[0]+" gegen Spieler 2 mit "+playerPoints[1]+" Punkten.");
    }else if (playerPoints[0] > playerPoints[1]){
      MiniJava.writeLineConsole("Spieler 2 gewinnt mit "+playerPoints[1]+" gegen Spieler 1 mit "+playerPoints[0]+" Punkten.");
    }else {
      MiniJava.writeLineConsole("Unentschieden! Beide Spieler haben "+ playerPoints[0] +" Punkte.");
    }
  }

  public static int getValueOfCard(int card) {
    if (card == 0)
      return 0;
    String cardValue = "" + card;
    int points = 1;
    char lastNumber = cardValue.charAt( cardValue.length()-1 ); //get ASCII value of the last character in the string ~ digit in number

    if( isSchnappszahl(cardValue) ){
      points += 5;
    }

    if( lastNumber == 53){ //53 is 5 in ASCII Code
      points += 1;

    }else if(lastNumber == 48){ //48 is 0 in ASCII Code
      points += 2;
    }

    return points;
  }

  /**
   * checks if a number is a "Schnappszahl" by iterating the number given as a String parameter
   * @param number value of a card
   * @return true if number is "Schnappszahl" otherwise false
   */
  private static boolean isSchnappszahl(String number){
    if (number.length() < 2)
      return false;
    for (int i = 0; i < number.length()-1; i++) {
      if(number.charAt(i) != number.charAt(i+1)) //check if two numbers are different
        return false;
    }
    return true; //if no different numbers found, all numbers are the same so we return true
  }

  public static void givePlayerCards(int[][] playerCards) throws IllegalAccessException {

    for (int row = 0; row < playerCards.length; row++) {
      for (int column = 0; column < playerCards[row].length; column++) {
        playerCards[row][column] = MiniJava.drawCard();
      }
    }

  }
}
