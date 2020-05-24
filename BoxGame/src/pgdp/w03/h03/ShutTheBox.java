package pgdp.w03.h03;

public class ShutTheBox extends MiniJava {
  public static void main(String[] args) throws IllegalAccessException {
      boolean[] boxes = {true, true, true, true, true, true, true, true};

      //points[0] -> store points of player 1
      //points[1] -> store points of player 2
      int[] points = new int[2];


      for (int i = 0; i < 16; i++) { //8 rounds x 2 players = 16 turns in total

          //get value of dices thrown
          int dice1 = MiniJava.dice();
          int dice2 = MiniJava.dice();

          //store value of combinations for both boxes
          int box1 = 0 , box2 = 0;
          boolean badCombination = false;

          //prompt user as long as boxes sum is not equal to the sum of both dices or when one box is already closed
          while (box1 == 0 ||(box1+box2) != (dice1+dice2) || boxes[box1-1]==false || boxes[box2-1]==false || box1 == box2){
              //we use modulo operator to show number of the player based on the fact there are two players only and each of them changes turns after every throw
              MiniJava.writeLineConsole("Spieler "+((i%2)+1)+" hat die folgenden Zahlen gewürfelt:");
              MiniJava.writeLineConsole(dice1);
              MiniJava.writeLineConsole(dice2);
              outputBoxes(boxes);
              MiniJava.writeLineConsole("Welche Boxen möchte Spieler "+((i%2)+1)+" schliessen? (0 für keine valide Kombination)");
              box1 = MiniJava.readInt("Box 1:");
              box2 = MiniJava.readInt("Box 2:");

              //if the user chooses to give no valid combination, we break out of while loop, otherwise check whether to prompt for input again or not
              if(box1 == 0 || box2 == 0 || box1>8 || box1 <1 || box2>8 || box2<1 ){
                  badCombination = true;
                  break;
              }
          }

          if(badCombination  || box1 == box2 ){ // calculate points
              //go through each of the 8 boxes and add sum of those who are true
              for (int j = 0; j < 8; j++) {
                  if(boxes[j]){
                      points[(i%2)] += j+1; //add (j+1) points where j is index of each box to the (i%2) index of points. we use modulo since we have only two players
                  }
              }

          }

          else { // close boxes & count them to check if we have a winner
                if(boxes[box1-1]){
                    boxes[box1-1]=false;
                }

                if (boxes[box2-1]){
                    boxes[box2-1]=false;
                }


                int closedBoxes=0;
                for (Boolean box: boxes) { //count closed boxes
                    if(!box) // ~ box==false
                        closedBoxes++;
                }

                if (closedBoxes == 8){
                    MiniJava.writeLineConsole("Spieler "+((i%2)+1)+" schliesst alle Boxen! Spieler "+((i%2)+1)+" gewinnt!");
                    return;
                }
          }

      }

      MiniJava.writeLineConsole("Spieler 1 Punktzahl");
      MiniJava.writeLineConsole(points[0]);
      MiniJava.writeLineConsole("Spieler 2 Punktzahl");
      MiniJava.writeLineConsole(points[1]);

      if(points[0] < points[1]){
          MiniJava.writeLineConsole("Spieler 1 gewinnt!");

      } else if(points[0] == points[1]){
          MiniJava.writeLineConsole("Unentschieden!");

      } else {
        MiniJava.writeLineConsole("Spieler 2 gewinnt!");
      }


  }

  /**
   * Do not modify this !
   * @param boxes
   */
  private static void outputBoxes(boolean[] boxes) {
    StringBuilder sb = new StringBuilder("Geöffnete Boxen: 1:");
    sb.append(boxes[0]);
    for (int i = 1; i < boxes.length; i++) {
      sb.append(" ").append(i + 1).append(":").append(boxes[i]);
    }
    write(sb.toString());
  }
}
