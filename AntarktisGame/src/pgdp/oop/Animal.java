package pgdp.oop;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;

public abstract class Animal {
  protected int x, y;
  static String filename;
  protected File f;
  protected Image image;

  protected static Animal[][] antarktis;

  protected boolean alive;

  public Animal(int x, int y) {
    this.x = x;
    this.y = y;
    this.alive = true;
  }

  public void move() {
    if (!this.alive /*|| antarktis == null*/)
      return;

    //check all 4 directions for food first, then move if allowed
    if (antarktis[circular41(this.x - 1)][this.y] != null && //check left for food
            this.canEat(antarktis[circular41(this.x - 1)][this.y])) {
      letHunterEatPrey(this, antarktis[circular41(this.x - 1)][this.y]);

    } else if (antarktis[this.x][circular41(this.y - 1)] != null && //check up for food
            this.canEat(antarktis[this.x][circular41(this.y - 1)])) {
      letHunterEatPrey(this, antarktis[this.x][circular41(this.y - 1)]);

    } else if (antarktis[circular41(this.x + 1)][this.y] != null && //check right for food
            this.canEat(antarktis[circular41(this.x + 1)][this.y])) {
      letHunterEatPrey(this, antarktis[circular41(this.x + 1)][this.y]);

    } else if (antarktis[this.x][circular41(this.y + 1)] != null && //check down for food
            this.canEat(antarktis[this.x][circular41(this.y + 1)])) {
      letHunterEatPrey(this, antarktis[this.x][circular41(this.y + 1)]);

    }

    //no food found, so go left if allowed
    //allowed means the cell must be empty and can not be hunted by other animals that are a danger to @this animal
    else if (antarktis[circular41(this.x - 1)][this.y] == null && canNotBeHunted(circular41(this.x - 1), y, this)) {
      letHunterMove(this,'L');

    } else if (antarktis[this.x][circular41(this.y - 1)] == null && canNotBeHunted(this.x, circular41(this.y - 1) ,this)) { //go up ...
      letHunterMove(this, 'U');

    } else if (antarktis[circular41(this.x + 1)][this.y] == null && canNotBeHunted(circular41(this.x + 1), this.y, this)) { //go right...
      letHunterMove(this, 'R');

    } else if (antarktis[this.x][circular41(this.y + 1)] == null && canNotBeHunted(this.x, circular41(this.y + 1), this)) { //go down...
      letHunterMove(this, 'D');
    }
  }

  //helper method
  // returns true if the cell in the coordinates @newX and @newY in the antarktis is free of threats to the actual @hunter
  private boolean canNotBeHunted(int newX, int newY, Animal hunter) {

    //keep a boolean for every direction, set them true if the hunter can not be eaten in the new position and the cell is empty
    boolean freeUp = false, freeRight = false, freeDown = false, freeLeft = false;
    if (antarktis[circular41(newX - 1)][newY] == null || !antarktis[circular41(newX - 1)][newY].canEat(hunter))
      freeLeft = true;
    if (antarktis[newX][circular41(newY - 1)] == null || !antarktis[newX][circular41(newY - 1)].canEat(hunter))
      freeUp = true;
    if (antarktis[circular41(newX + 1)][newY] == null || !antarktis[circular41(newX + 1)][newY].canEat(hunter))
      freeRight = true;
    if (antarktis[newX][circular41(newY + 1)] == null || !antarktis[newX][circular41(newY + 1)].canEat(hunter))
      freeDown = true;

    //return true only if all 4 directions are true, meaning threat-free
    return freeLeft && freeUp && freeRight && freeDown;
  }

  //helper method
  // removes the eaten animal from antarktis and updates it with new hunter
  private void letHunterEatPrey(Animal hunter, Animal prey) {
    prey.alive = false;

    //update hunter coordinates
    int tempHunterX = hunter.x, tempHunterY = hunter.y;
    hunter.x = prey.x;
    hunter.y = prey.y;

    //update antarktis
    antarktis[hunter.x][hunter.y] = hunter;
    antarktis[tempHunterX][tempHunterY] = null;
  }


  //helper method
  // simply changes coordinates of moving animal
  // and updates antarktis with his new position
  private void letHunterMove(Animal hunter, char direction) {
    int tempHunterX = hunter.x, tempHunterY = hunter.y;

    if (direction == 'L') {
      hunter.x = circular41(hunter.x - 1);
    } else if (direction == 'U') {
      hunter.y = circular41(hunter.y - 1);
    } else if (direction == 'R') {
      hunter.x = circular41(hunter.x + 1);
    } else if (direction == 'D') {
      hunter.y = circular41(hunter.y + 1);
    }

    antarktis[hunter.x][hunter.y] = hunter;
    antarktis[tempHunterX][tempHunterY] = null;
  }

  //helper method ~ makes the movement circular41
  // returns a valid coordinate between 0 and 40 (inclusive)
  protected static int circular41(int index) {
    if (index >= 0) {
      return index % 41;
    } else {
      return 41 + (index % 41);
    }
  }

  public abstract boolean canEat(Animal animal);

  protected abstract boolean eatenBy(Penguin penguin);
  protected abstract boolean eatenBy(PlayerPenguin playerPenguin);
  protected abstract boolean eatenBy(Whale whale);
  protected abstract boolean eatenBy(LeopardSeal leopardSeal);
  protected abstract boolean eatenBy(Fish fish);

  public static void setAntarktis(Animal[][] antarktis) {
    Animal.antarktis = antarktis;
  }
  // Graphics Stuff - You don't have to do anything here

  private void paintSymbol(Graphics g, Color c, int height, int width) {
    GradientPaint gradient = new GradientPaint(15, 0, c, width, 0, Color.LIGHT_GRAY);
    ((Graphics2D) g).setPaint(gradient);
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.fillOval((int) (width * 0.3), (int) (height * 0.3), (int) (width * 0.5),
            (int) (height * 0.5));
  }

  public void draw(Graphics g, int height, int width) {
    if (image == null) {
      paintSymbol(g, Color.YELLOW, height, width);
      return;
    }
    ((Graphics2D) g).drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(null),
            image.getHeight(null), null);
  }
}