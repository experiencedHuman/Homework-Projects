package pgdp.oop;

import java.awt.Toolkit;
import java.io.File;

public class Fish extends Animal {
  static String filename = "fish.png";

  public Fish(int x, int y) {
    super(x, y);

    f = new File(filename);
    image = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
  }

  public boolean canEat(Animal animal) {
    return animal.eatenBy(this);
  }

  @Override
  protected boolean eatenBy(Penguin penguin) {
    return true;
  }

  @Override
  protected boolean eatenBy(PlayerPenguin playerPenguin) {
    return true;
  }

  @Override
  protected boolean eatenBy(Whale whale) {
    return false;
  }

  @Override
  protected boolean eatenBy(LeopardSeal leopardSeal) {
    return true;
  }

  @Override
  protected boolean eatenBy(Fish fish) {
    return false;
  }

  public void move() {
    if (!this.alive)
      return;

    //save fish coordinates
    int tempFishX = this.x, tempFishY = this.y;

    //let fish move and update his coordinates
    if (antarktis[this.x][circular41(this.y - 1)] == null) { // go up if allowed
      this.y = circular41(this.y - 1);

    } else if (antarktis[circular41(this.x + 1)][this.y] == null) { // go right...
      this.y = circular41(this.y + 1);

    } else if (antarktis[this.x][circular41(this.y + 1)] == null) { // go down...
      this.x = circular41(this.x + 1);

    } else if (antarktis[circular41(this.x - 1)][this.y] == null) { // go left...
      this.y = circular41(this.y - 1);
    } else return;

    //update antarktis with new fish coordinates
    // and free his old position
    antarktis[this.x][this.y] = this;
    antarktis[tempFishX][tempFishY] = null;
  }
}