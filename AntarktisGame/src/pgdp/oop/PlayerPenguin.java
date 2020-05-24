package pgdp.oop;

public class PlayerPenguin extends Penguin {
    public PlayerPenguin(int x, int y) {
        super(x, y);
    }

    public boolean canEat(Animal animal) {
        return animal.eatenBy(this);
    }

    public boolean move(int newX, int newY) {
        if (!this.alive)
            return true;

        //save actual coordinates of player penguin, need later to update his old position
        int tempPlayerX = this.x, tempPlayerY = this.y;

        this.x = circular41(newX);
        this.y = circular41(newY);

        //player penguin moved to an empty field
        if (antarktis[this.x][this.y] == null) {
            //update antarktis with his new position
            antarktis[tempPlayerX][tempPlayerY] = null;
            antarktis[this.x][this.y] = this;
            return false;
        }

        //player penguin went in a whale's tile or he found a seal or the other penguin
        //in each case we update position and return true
        if (antarktis[this.x][this.y].canEat(this) ||
                antarktis[this.x][this.y].canEat(new Fish(1,1))) {
            this.alive = false;
            antarktis[tempPlayerX][tempPlayerY] = null;
            return true;
        }

        //playerPenguin found lostPenguin
        if (foundLostPenguin()) {
            antarktis[tempPlayerX][tempPlayerY] = null;
            return true;
        }

        //playerPenguin eats fish
        if (this.canEat(antarktis[this.x][this.y])) {
            antarktis[this.x][this.y].alive = false;
            antarktis[tempPlayerX][tempPlayerY] = null;
            antarktis[this.x][this.y] = this;
            return false;
        }

        return false;
    }

    //helper method
    // returns true if the coordinates of the playerPenguin match the ones of lostPenguin, meaning both have met
    private boolean foundLostPenguin(){
        return this.x == Antarktis.getLostPenguin().x && this.y == Antarktis.getLostPenguin().y;
    }
}