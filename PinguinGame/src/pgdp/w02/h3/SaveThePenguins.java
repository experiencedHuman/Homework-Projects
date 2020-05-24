package pgdp.w02.h3;

public class SaveThePenguins extends MiniJava {
  public static void main(String[] args) {

    /*
    initialize an array @pinguinPopulation of size 5 storing populations for every pinguin type
    at index [0] => Jungtiere population
    at index [1] => Junge Erwachsene population
    at index [2] => Erwachsene population
    at index [3] => Alte Erwachsene population
    at index [4] => Weise population
     */
    int[] pinguinPopulation = new int[5];

    //we get input of timeSteps that the simulation will later run
    int timeSteps = MiniJava.readInt("Bitte Geben Sie die Anzahl an Zeitschritten ein (>= 1):");
    if (timeSteps <= 0){
      MiniJava.writeLineConsole("Zeitschritte >= 1 erforderlich");
      return;
    }

    //get the start size for every population
    pinguinPopulation[0] = MiniJava.readInt("Startpopulation Jungtiere");
    pinguinPopulation[1] = MiniJava.readInt("Startpopulation Junge Erwachsene");
    pinguinPopulation[2] = MiniJava.readInt("Startpopulation Erwachsene");
    pinguinPopulation[3] = MiniJava.readInt("Startpopulation Alte Erwachsene");
    pinguinPopulation[4] = MiniJava.readInt("Startpopulation Weise");

    //simulate the pinguin population for every timestep
    for (int timestep=1; timestep <= timeSteps; timestep++){
      int food = (pinguinPopulation[1]*3*timestep)+(pinguinPopulation[2]*2*timestep); //collect food

      //distribute food for all 5 pinguin populations, indexed 0 to 4
      for (int i=0; i < 4; i++){
        if (food == 0){ //if there is no food at all collected, all pinguins of the actual population die
          pinguinPopulation[i] = 0;
          continue;
        }
        if (food < pinguinPopulation[i]){ //when there is more pinguins then food, some pinguins die
          pinguinPopulation[i] = food/timestep; //each pinguin consumes 1 food for a timestep, the rest die to sleep :(
          food = 0;
          continue;
        }

        //there is plenty of food collected and all pinguins get to eat. food is reduced.
        food -= pinguinPopulation[i] * timestep; //pinguins eat one food portion for every timestep, so food gets reduced by that amount
      }

      //calculate newborn pinguins
      int new_bornedPinguins = 0;
      new_bornedPinguins += (pinguinPopulation[1]/4); //add (AnzahlJungeErwachsene/4) newborn pinguins
      new_bornedPinguins += (pinguinPopulation[2]/2); //add (AnzahlErwachsene/2) newborn pinguins
      new_bornedPinguins += (pinguinPopulation[3]/3); //add (AnzahlAlteErwachsene/3) newborn pinguins

      //make all pinguins one level older by copying elements of the array to the next bigger index
      //we start from the end so we don't override numbers~(populations)
      for (int i=4; i>0; i--){
        pinguinPopulation[i] = pinguinPopulation[i-1];
      }
      pinguinPopulation[0] = new_bornedPinguins;
    }

    //show end-results of the remaining populations
    MiniJava.writeLineConsole("Anzahl Jungtiere:\n"+pinguinPopulation[0]);
    MiniJava.writeLineConsole("Anzahl Junge Erwachsene:\n"+pinguinPopulation[1]);
    MiniJava.writeLineConsole("Anzahl Erwachsene:\n"+pinguinPopulation[2]);
    MiniJava.writeLineConsole("Anzahl Alte Erwachsene:\n"+pinguinPopulation[3]);
    MiniJava.writeLineConsole("Anzahl Weise:\n"+pinguinPopulation[4]);
  }
}