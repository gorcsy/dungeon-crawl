package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.Stairs;
import com.codecool.dungeoncrawl.mapgenerator.MapLoader;
import com.codecool.dungeoncrawl.model.PlayerModel;

import static com.codecool.dungeoncrawl.Main.*;


public class Player extends Actor {

   private Inventory inventory;

   public Player(Cell cell) {
      super("Player", cell);
      super.setHealth(10);
      super.setStrength(5);
      inventory = MapLoader.loadInventory();
      cell.setActor(this);
   }
   public Player(String name, Cell cell, int healt){
      super(name, cell);
      this.setHealth(healt);
   }

   public void setInventory(Inventory inventory) {
      this.inventory = inventory;
   }

   public Inventory getInventory() {
      return inventory;
   }

   @Override
   public void move(int dx, int dy, GameMap map) {
      super.move(dx, dy, map);

      if (super.getCell().getStairs() != null) {
         String id = super.getCell().getStairs().getID();
         String direction = super.getCell().getStairs().getDirection();
         Stairs remoteStairs = map.findOppositeStairs(id, direction);

         super.getCell().setActor(null);
         super.setCell(map.getCell(remoteStairs.getMoveToX(), remoteStairs.getMoveToY()));
         super.getCell().setActor(this);
      }
   }

   @Override
   public void enemyMove(GameMap map) {

   }

   public void openingDoor(Cell nextCell) {
      // opening the door if it be able to -- there is a key in the Inventory
      for (Item item : inventory.getItems()) {
         if (item.getClass().equals(Key.class)) {
            // removing a key from the Player's Inventory
            this.inventory.removeItem(item);
            // change door to opened
            nextCell.setCellType(CellType.OPENED_DOOR);
            gate.play();
            break;
         }
      }
   }

   public void attack(int dx, int dy, GameMap map) {
      Cell nextCell = super.getCell().getNeighbor(dx, dy, map);
      Actor enemy = nextCell.getActor();
      if (enemy != null && enemy.getHealth() > 0) {
         int enemyHealthAfterHit = enemy.getHealth() - super.getStrength();
         System.out.println(enemyHealthAfterHit);
         enemy.setHealth(enemyHealthAfterHit);
         sword.play();
         System.out.println("I hit you, enemy health left: " + enemy.getHealth());
      }
      if (enemy != null && enemy.getHealth() <= 0) {
         dead.play();
         super.getCell().setActor(null);
         nextCell.setActor(this);
         super.setCell(nextCell);
      }
      assert enemy != null;
      int myHP = super.getHealth() - enemy.getStrength();
      super.setHealth(super.getHealth() - enemy.getStrength());
      System.out.println("my HP: " + myHP);
      if (super.getHealth() <= 0) {
         gameOver();
      }
   }

   public void gameOver() {
      System.out.println("GAME OVER");
   }

   public void setStrengthByWeapon() {
      super.setStrength(super.getStrength() + 3);
      System.out.println("set strength to : " + super.getStrength());
   }
}
