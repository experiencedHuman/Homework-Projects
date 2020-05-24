package pgdp.games;

public class Penguin extends MiniJava {

    public static void printWorld(int[][] world, int pinguRow, int pinguColumn) {

        boolean lastColumn = false;

        //iterate rows and columns
        for (int row = 0; row < world.length; ++row) {
            for (int col = 0; col < world[row].length; ++col) {

                //if we are at the last column of the row we mark it with the boolean lastColumn to handle spacing in last column correctly
                if (col == world[row].length-1) {
                    lastColumn = true;
                }

                if (world[row][col] == 0) {
                    MiniJava.writeConsole("L"); //Kachel mit Landfläche
                }else if (world[row][col] == 1) {
                    MiniJava.writeConsole("E"); //Kachel mit Eisfläche
                }else if (world[row][col] == 2) {
                    MiniJava.writeConsole("W"); //Kachel mit Wasser
                }else if (world[row][col] == 3) {
                    MiniJava.writeConsole("H"); //Kachel mit Hai
                }else if (world[row][col] == 4) {
                    MiniJava.writeConsole("F"); //Kachel mit Fisch
                }

                //if pingu tile is found then print him as well
                if (row == pinguRow && col == pinguColumn)
                    MiniJava.writeConsole("(P)");

                //as long as we haven't arrived at the last column keep spacing the tiles
                if (!lastColumn) {
                    MiniJava.writeConsole("\t");
                }

            }
            MiniJava.writeLineConsole();
            lastColumn = false;
        }

    }

    public static boolean isFishReachable(int[][] world, int pinguRow, int pinguColumn){

        //if pingu is found in shark's tile right of the bat, return false
        if (world[pinguRow][pinguColumn] == 3)
            return false;

        int rows = world.length;
        int cols = world[0].length;

        //we use a visited matrix to keep track of tiles we visit to not end up in circles and never ending loops
        boolean[][] visited = new boolean[rows][cols];

        //fill matrix values with false
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                visited[row][col] = false;
            }
        }

        //apply recursion through DFS
        return depthFirstSearch(world,pinguRow,pinguColumn,visited);
    }

    private static boolean depthFirstSearch(int[][] world, int pinguRow, int pinguColumn, boolean[][] visited){
        //mark tile as visited
        visited[pinguRow][pinguColumn] = true;

        //if fisch is found, return true
        if (world[pinguRow][pinguColumn] == 4)
            return true;

        //to calculate new possible moves we create a small matrix
        // 4 possible moves, each has 2 coordinates, 1 of the row and 1 of the column
        int[][] possibleMoves = new int[4][2];

        if (world[pinguRow][pinguColumn] == 0) { // pingu is on land, meaning he can move 1 tile
            possibleMoves[0] = new int[]{pinguRow - 1 , pinguColumn}; //north
            possibleMoves[1] = new int[]{pinguRow + 1 , pinguColumn}; //south
            possibleMoves[2] = new int[]{pinguRow , pinguColumn - 1}; //west
            possibleMoves[3] = new int[]{pinguRow , pinguColumn + 1}; //east

        } else if (world[pinguRow][pinguColumn] == 1) { // pingu is on ice, means he can move diagonally by 1 tile
            possibleMoves[0] = new int[]{pinguRow - 1 , pinguColumn - 1};
            possibleMoves[1] = new int[]{pinguRow - 1 , pinguColumn + 1};
            possibleMoves[2] = new int[]{pinguRow + 1 , pinguColumn - 1};
            possibleMoves[3] = new int[]{pinguRow + 1 , pinguColumn + 1};

        }else if (world[pinguRow][pinguColumn] == 2) { // pingu is in water, means he can move diagonally by 3 tiles
            possibleMoves[0] = new int[]{pinguRow - 3 , pinguColumn - 3};
            possibleMoves[1] = new int[]{pinguRow - 3 , pinguColumn + 3};
            possibleMoves[2] = new int[]{pinguRow + 3 , pinguColumn - 3};
            possibleMoves[3] = new int[]{pinguRow + 3 , pinguColumn + 3};

        }

        for (int[] movement: possibleMoves) {
            //movement[0] is the coordinate of new pinguRow
            //movement[1] is the coordinate of new pinguColumn
            int newPinguRow = movement[0];
            int newPinguColumn = movement[1];


            //we eliminate all moves that aren't elligible by simply continuing
            if (newPinguRow >= world.length || newPinguRow < 0 ||
                newPinguColumn >= world[newPinguRow].length || newPinguColumn < 0 ||
                world[newPinguRow][newPinguColumn] == 3)
                continue;

            //for those tiles that are still unvisited we apply recursion with the new coordinates
            if (!visited[newPinguRow][newPinguColumn]) {
                return depthFirstSearch(world,newPinguRow,newPinguColumn,visited);
            }
        }

        //no possible paths found or we ended up in a shark's tile so we return false
        return false;
    }

    /**
     * Gibt die Beispielwelt Nr. 1 zurück.
     * Modifizieren Sie diese Methode nicht.
     * @return Eine Beispielwelt
     */
    public static int[][] generateExampleWorldOne(){
        return new int[][] {{2,3,3,3,3,3}, {3,0,3,3,4,3}, {3,3,3,3,3,1}, {3,3,3,0,1,3}, {3,3,3,3,3,3}};
    }

    /**
     * Gibt die Beispielwelt Nr. 2 zurück.
     * Modifizieren Sie diese Methode nicht.
     * @return Eine Beispielwelt
     */
    public static int[][] generateExampleWorldTwo(){
        return new int[][] {{0,0,0,2}, {0,0,0,1}, {0,1,3,4}, {3,4,3,3}};
    }

    /**
     *  Sie können die main-Methode verwenden, um Ihr Programm zu testen. 
     */
    public static void main(String[] args){
//        int pinguRow = 0;
//        int pinguColumn = 0;
//        int[][] world = generateExampleWorldOne();
//        printWorld(world, pinguRow, pinguColumn);
//        write(""+isFishReachable(world, pinguRow, pinguColumn));
    }

}