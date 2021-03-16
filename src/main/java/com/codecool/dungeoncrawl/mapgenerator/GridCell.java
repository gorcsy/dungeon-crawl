package com.codecool.dungeoncrawl.mapgenerator;

public class GridCell {

   private int posX;
   private int posY;
   private boolean isWall;
   private boolean isVisited;

   public GridCell(int x, int y){
      this.posX = x;
      this.posY = y;
      this.isWall = false;
      this.isVisited = false;
   }

   public int getPosX() {
      return posX;
   }

   public int getPosY() {
      return posY;
   }

   public void setWall() {
      isWall = true;
   }

   public boolean isWall() {
      return isWall;
   }

   public void setVisited() {
      isVisited = true;
   }

   public boolean isVisited() {
      return isVisited;
   }
}
