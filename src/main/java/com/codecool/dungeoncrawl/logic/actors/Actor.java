package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.Drawable;
import com.codecool.dungeoncrawl.logic.map.GameMap;


public abstract class Actor implements Drawable {

   private String name;
   private Cell cell;
   private int health;
   private int strength;

   public Actor(String name, Cell cell) {
      this.setName(name);
      this.cell = cell;
   }

   public void move(int dx, int dy, GameMap map) {
      Cell nextCell = cell.getNeighbor(dx, dy, map);

      if (cell.getActor().getClass().equals(Player.class) &&
                 nextCell.getCellType() == CellType.CLOSED_DOOR) {
         map.getPlayer().openingDoor(nextCell);
      }
      else if(cell.getActor().getClass().equals(Player.class) && nextCell.getActor() != null) {
         ((Player) cell.getActor()).attack(dx, dy, map);
      }
      else if(!CellType.getListOfBlockedCellTypes().contains(nextCell.getCellType())) {
         cell.setActor(null);
         nextCell.setActor(this);
         cell = nextCell;
      }

   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public int getStrength() {
      return strength;
   }

   public void setStrength(int strength) {
      this.strength = strength;
   }

   public int getHealth() {
      return health;
   }

   public Cell getCell() {
      return cell;
   }

   public int getX() {
      return cell.getX();
   }

   public int getY() {
      return cell.getY();
   }

   public void setCell(Cell cell) {
      this.cell = cell;
   }

   public abstract void enemyMove(GameMap map);

   @Override
   public String getTileName() {
      return this.getName();
   }

}
