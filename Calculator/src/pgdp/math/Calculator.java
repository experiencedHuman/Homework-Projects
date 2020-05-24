package pgdp.math;

public class Calculator extends MiniJava {

    public static void main(String[] args) {

        //show never ending loop prompting the user possible operations until he enters 6
        while (true){
            int operation = MiniJava.readInt("Wählen Sie eine Operation:\n1) +\n2) -\n3) *\n4) /\n5) %\n6) Programm beenden"); //get number from user determining the operation that will be executed

            //input must be 1-6
            while (operation <1 || operation > 6){
                operation = MiniJava.readInt("Wählen Sie eine Operation:\n1) +\n2) -\n3) *\n4) /\n5) %\n6) Programm beenden");
            }
            if (operation == 6)
                break; //break out the loop and terminate the program

            int op1 = 0, op2 = 0; //save two operands
            op1 = MiniJava.readInt("Ersten Operand eingeben:");
            op2 = MiniJava.readInt("Zweiten Operand eingeben:");

            //determine which operation should be executed
            if (operation == 1){
                MiniJava.write(op1 + op2);

            }else if( operation == 2){
                MiniJava.write(op1 - op2);

            }else if( operation == 3){
                MiniJava.write(op1 * op2);

            }else if( operation == 4){
                if (op2 == 0){
                    MiniJava.write("Fehler: Division durch 0!");
                    continue; //jump to line 13 to continue the program
                }
                MiniJava.write(op1 / op2);

            }else if( operation == 5){
                if (op2 == 0){
                    MiniJava.write("Fehler: Division durch 0!");
                    continue; //jump to line 13 to continue the program
                }
                MiniJava.write(op1 % op2);
            }
        }
    }

}
