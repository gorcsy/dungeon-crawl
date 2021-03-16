package com.codecool.dungeoncrawl.logic.map;

public class Stairs {

   private final int moveToX;
   private final int moveToY;
   private final String ID;
   private final String direction;

   public Stairs(int locationX, int locationY, String id, String direction) {
      this.moveToX = locationX;
      this.moveToY = locationY;
      this.ID = id;
      this.direction = direction;
   }

   public int getMoveToX() {
      return moveToX;
   }

   public int getMoveToY() {
      return moveToY;
   }

   public String getDirection() {
      return direction;
   }

   public String getID() {
      return ID;
   }

}
