package pgdp.oop;

import java.awt.event.WindowEvent;

public class Antarktis extends Maze {
    private static int width, height;
    private static Penguin lostPenguin;
    private static Whale[] whales = new Whale[5];
    private static LeopardSeal[] leopardSeals = new LeopardSeal[20];
    private static Fish[] fishes = new Fish[500];
    private static PlayerPenguin playerPenguin;

    public static void main(String[] args) {
        width = 41;
        height = 41;
        antarktis = generateMaze(width, height);
        Animal.setAntarktis(antarktis);
        setupMaze();
        gameLoop();

        // Close the opnend frame
        closeFrame();
    }

    private static void gameLoop() {
        //repeat as long as user doesn't give input and lost penguin is alive
        while (currentEvent == NOTHING && lostPenguin.alive) {
            draw();
            if (currentEvent == Maze.LEFT) {
                if (playerPenguin.move(playerPenguin.x - 1, playerPenguin.y)) {
                    break;
                }
                moveAll();

            } else if (currentEvent == Maze.UP) {
                if (playerPenguin.move(playerPenguin.x, playerPenguin.y - 1)) {
                    break;
                }
                moveAll();

            } else if (currentEvent == Maze.RIGHT) {
                if (playerPenguin.move(playerPenguin.x + 1, playerPenguin.y)) {
                    break;
                }
                moveAll();

            } else if (currentEvent == Maze.DOWN) {
                if (playerPenguin.move(playerPenguin.x, playerPenguin.y + 1)) {
                    break;
                }
                moveAll();
            }
            currentEvent = NOTHING;
        }
    }

    private static void moveAll() {
        if (!lostPenguin.alive || !playerPenguin.alive)
            return;

        for (Whale whale : whales) {
            if (whale == null) continue;
            whale.move();
        }

        for (LeopardSeal leoSeal : leopardSeals) {
            if (leoSeal == null) continue;
            leoSeal.move();
        }

        if (lostPenguin != null && lostPenguin.alive)
            lostPenguin.move();
        else
            return;

        for (Fish fish : fishes) {
            if (fish == null) continue;
            fish.move();

        }
    }


    /**
     * Example Setup for easier Testing during development
     */
    private static void setupMaze() {
        int[] pos;
        playerPenguin = new PlayerPenguin(3, 3);
        antarktis[3][3] = playerPenguin;

        for (int i = 0; i < whales.length; i++) {
            pos = getRandomEmptyField();
            whales[i] = new Whale(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = whales[i];
        }

        for (int i = 0; i < leopardSeals.length; i++) {
            pos = getRandomEmptyField();
            leopardSeals[i] = new LeopardSeal(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = leopardSeals[i];
        }

        for (int i = 0; i < fishes.length; i++) {
            pos = getRandomEmptyField();
            fishes[i] = new Fish(pos[0], pos[1]);
            antarktis[pos[0]][pos[1]] = fishes[i];
        }

        antarktis[20][20] = new Penguin(20, 20);
        lostPenguin = (Penguin) antarktis[20][20];
    }

    //getter
    protected static Penguin getLostPenguin() {
        return lostPenguin;
    }
}
