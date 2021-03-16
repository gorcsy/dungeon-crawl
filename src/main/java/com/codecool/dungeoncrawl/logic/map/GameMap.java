package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.actors.*;

public class GameMap {
   private int width;
   private int height;
   private Cell[][] cells;
   private Player player;

   public GameMap(int width, int height, CellType defaultCellType) {
      this.width = width;
      this.height = height;
      cells = new Cell[height][width];
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            cells[y][x] = new Cell(x, y, defaultCellType);
         }
      }
   }

   public Cell getCell(int x, int y) {
      return cells[y][x];
   }

   public void setPlayer(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return player;
   }

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   public Stairs findOppositeStairs(String ID, String direction) {
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (cells[y][x].getStairs() != null) {
               Stairs currentSt = cells[y][x].getStairs();
               if (currentSt.getID().equals(ID) &&
                       !currentSt.getDirection().equals(direction)) {
                  return currentSt;
               }
            }
         }
      }
      System.out.println("Cannot find opposite side of the stairs!");
      return null;
   }

}
