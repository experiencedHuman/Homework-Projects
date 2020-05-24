package pgdp.w02.h02;

public class TwoMaximums extends MiniJava {

    public static void main(String[] args) {

      int k = MiniJava.readInt("Bitte Anzahl eingeben:");

      if (k < 2){
        MiniJava.writeLineConsole("Fehler: Anzahl >= 2 erwartet!");
        return;
      }

      //initialize two variables biggest and secondBiggest
      int biggest = Integer.MIN_VALUE, secondBiggest = Integer.MIN_VALUE;

      for (int i=0; i<k; i++){ //read exactly k numbers
        int number = MiniJava.readInt("Bitte Zahlen eingeben:");

        if(number > biggest){
          secondBiggest = biggest; //biggest becomes automatically secondBiggest
          biggest = number; //number becomes biggest

        }else if(number > secondBiggest){ // if number is not bigger then biggest but bigger then secondBiggest then update secondBiggest
          secondBiggest = number;
        }
      }

     MiniJava.writeLineConsole("Erster:\n"+biggest+"\nZweiter:\n"+secondBiggest);
    }
}
