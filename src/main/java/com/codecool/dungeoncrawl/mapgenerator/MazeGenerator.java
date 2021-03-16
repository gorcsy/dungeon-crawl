package com.codecool.dungeoncrawl.mapgenerator;

import com.codecool.dungeoncrawl.mapgenerator.GridCell;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MazeGenerator {

   public static final int WIDTH = 60;
   public static final int HEIGHT = 40;
   public static GridCell[][] grid = new GridCell[HEIGHT][WIDTH];

   public static final int START_X = 30;
   public static final int START_Y = 20;


   public static void main(String[] args) {

      // grid (maze) init
      for (int y = 0; y < HEIGHT; y++) {
         for (int x = 0; x < WIDTH; x++) {
            grid[y][x] = new GridCell(x, y);
         }
      }
      // setting border to WALL
      for (int i = 0; i < WIDTH; i++) {
         grid[0][i].setWall();
         grid[HEIGHT - 1][i].setWall();
      }
      for (int i = 0; i < HEIGHT; i++) {
         grid[i][0].setWall();
         grid[i][WIDTH - 1].setWall();
      }
      // putting random large wall blocks
      for (int i = 0; i < 8; i++) {
         int startX = (int) (Math.random() * (WIDTH - 12));
         int startY = (int) (Math.random() * (HEIGHT - 12));
         int width = (int) (Math.random() * 8) + 4;
         int heigth = (int) (Math.random() * 8) + 4;

         for (int y = startY; y < startY + heigth ; y++) {
            for (int x = startX; x < startX + width; x++) {
               grid[y][x].setWall();
            }
         }
      }


      // start position
      int curX = START_X;
      int curY = START_Y;
      grid[curY][curX].setVisited();



      // start building maze :)
      Stack<GridCell> stackOfSteps = new Stack<>();
      List<GridCell> neighbors = new ArrayList<>();

      int step = 0;
      do {
         neighbors.clear();
         if (!(curX - 1 < 0)) {
            neighbors = checkNeighbor(neighbors, curX - 1, curY); // check left cell
         }
         if (!(curY - 1 < 0)) {
            neighbors = checkNeighbor(neighbors, curX, curY - 1); // check top cell
         }
         if (!(curX + 1 == WIDTH)) {
            neighbors = checkNeighbor(neighbors, curX + 1, curY); // check right cell
         }
         if (!(curY + 1 == HEIGHT)) {
            neighbors = checkNeighbor(neighbors, curX, curY + 1); // check bottom cell
         }

         // stepping forward
         if (neighbors.size() > 0) {
            stackOfSteps.push(grid[curY][curX]);

            int index = (int) (Math.random() * neighbors.size());
            int newX = neighbors.get(index).getPosX();
            int newY = neighbors.get(index).getPosY();
            grid[newY][newX].setVisited();

            setWall(grid[curY][curX], grid[newY][newX]);
            curX = newX;
            curY = newY;
            setWall(curX, curY);

         // stepping back
         } else {
            GridCell previousCell = stackOfSteps.pop();
            curX = previousCell.getPosX();
            curY = previousCell.getPosY();
         }

         // OPTIONAL PART - FOR SLOW GENERATING
         // ***** BEGIN OFF show grid and current cell states - for debugging
         if (step > 5) {
            showGrid(grid[curY][curX]);
            step = 0;
            try {
               Thread.sleep(2000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         else step++;
         // ***** END OFF show grid and current cell states - for debugging

      } while (neighbors.size() > 0 || stackOfSteps.size() > 0);

      // filling the inclused area(s) with wall element
      for (int y = 1; y < HEIGHT - 1; y++) {
         for (int x = 1; x < WIDTH - 1; x++) {
            if (grid[y][x - 1].isWall() && grid[y][x + 1].isWall() &&
                    grid[y - 1][x].isWall() && grid[y + 1][x].isWall()) {
               grid[y][x].setWall();
            }
         }
      }

      Path file = Path.of("dungeon.txt");
      try (BufferedWriter writer = Files.newBufferedWriter(file)) {
         for (int y = 0; y < HEIGHT; y++) {
            String str = "";
            for (int x = 0; x < WIDTH; x++) {
               str = str + (grid[y][x].isVisited() ? "." : "#");
            }
            writer.write(str + "\n");
         }

      } catch (IOException ioe) {
         throw new IllegalStateException("Can not write file", ioe);
      }

      showGrid(grid[curY][curX]);
   }

   public static void setWall(GridCell current, GridCell next) {
      // vertical movement was happened
      if (current.getPosX() == next.getPosX()) {
         if (grid[current.getPosY()][current.getPosX() - 1].isVisited()) {
            grid[next.getPosY()][next.getPosX() - 1].setWall();
         }
         if (grid[current.getPosY()][current.getPosX() + 1].isVisited()) {
            grid[next.getPosY()][next.getPosX() + 1].setWall();
         }
      }
      // horizontal movement was happened
      else {
         if (grid[current.getPosY() - 1][current.getPosX()].isVisited()) {
            grid[next.getPosY() - 1][next.getPosX()].setWall();
         }
         if (grid[current.getPosY() + 1][current.getPosX()].isVisited()) {
            grid[next.getPosY() + 1][next.getPosX()].setWall();
         }

      }
   }

   public static void setWall(int x, int y) {
      // check left side
      if (x > 1) {
         if (grid[y][x - 2].isVisited() && !(grid[y][x - 1].isVisited())) {
            grid[y][x - 1].setWall();

            if (!grid[y - 1][x - 1].isVisited()) {
               grid[y - 1][x - 1].setWall();
            }
            if (!grid[y + 1][x - 1].isVisited()) {
               grid[y + 1][x - 1].setWall();
            }

         }
         if ((!grid[y][x - 1].isVisited() &&
                 (grid[y - 1][x - 2].isVisited() || grid[y + 1][x - 2].isVisited()))) {
            grid[y][x - 1].setWall();
         }
         if (grid[y][x - 1].isWall()) {
            if (grid[y - 1][x].isWall()) {
               grid[y - 1][x - 1].setWall();
            }
            if (grid[y + 1][x].isWall()) {
               grid[y + 1][x - 1].setWall();
            }
         }
      }

      // check right side
      if (x < WIDTH - 2) {
         if (grid[y][x + 2].isVisited() && !(grid[y][x + 1].isVisited())) {
            grid[y][x + 1].setWall();

            if (!grid[y - 1][x + 1].isVisited()) {
               grid[y - 1][x + 1].setWall();
            }
            if (!grid[y + 1][x + 1].isVisited()) {
               grid[y + 1][x + 1].setWall();
            }

         }
         if ((!grid[y][x + 1].isVisited() &&
                 (grid[y - 1][x + 2].isVisited() || grid[y + 1][x + 2].isVisited()))) {
            grid[y][x + 1].setWall();
         }
         if (grid[y][x + 1].isWall()) {
            if (grid[y - 1][x].isWall()) {
               grid[y - 1][x + 1].setWall();
            }
            if (grid[y + 1][x].isWall()) {
               grid[y + 1][x + 1].setWall();
            }
         }
      }

      // check top side
      if (y > 1) {
         if (grid[y - 2][x].isVisited() && !(grid[y - 1][x].isVisited())) {
            grid[y - 1][x].setWall();

            if (!grid[y - 1][x - 1].isVisited()) {
               grid[y - 1][x - 1].setWall();
            }
            if (!grid[y - 1][x + 1].isVisited()) {
               grid[y - 1][x + 1].setWall();
            }

         }
         if ((!grid[y - 1][x].isVisited() &&
                 (grid[y - 2][x - 1].isVisited() || grid[y - 2][x + 1].isVisited()))) {
            grid[y - 1][x].setWall();
         }
         if (grid[y - 1][x].isWall()) {
            if (grid[y][x - 1].isWall()) {
               grid[y - 1][x - 1].setWall();
            }
            if (grid[y][x + 1].isWall()) {
               grid[y - 1][x + 1].setWall();
            }
         }
      }

      // check bottom side
      if (y < HEIGHT - 2) {
         if (grid[y + 2][x].isVisited() && !(grid[y + 1][x].isVisited())) {
            grid[y + 1][x].setWall();

            if (!grid[y + 1][x - 1].isVisited()) {
               grid[y + 1][x - 1].setWall();
            }
            if (!grid[y + 1][x + 1].isVisited()) {
               grid[y + 1][x + 1].setWall();
            }

         }
         if ((!grid[y + 1][x].isVisited() &&
                 (grid[y + 2][x - 1].isVisited() || grid[y + 2][x + 1].isVisited()))) {
            grid[y + 1][x].setWall();
         }
         if (grid[y + 1][x].isWall()) {
            if (grid[y][x - 1].isWall()) {
               grid[y + 1][x - 1].setWall();
            }
            if (grid[y][x + 1].isWall()) {
               grid[y + 1][x + 1].setWall();
            }
         }
      }
   }

   public static List<GridCell> checkNeighbor(List<GridCell> nb, int x, int y) {
      GridCell cell = grid[y][x];
      if (!cell.isWall() && !cell.isVisited()) {
         nb.add(cell);
      }
      return nb;
   }

   public static void showGrid(GridCell current) {
      for (int y = 0; y < HEIGHT; y++) {
         for (int x = 0; x < WIDTH; x++) {
            GridCell cell = grid[y][x];
            System.out.print(cell.isWall() ? "#" :
                    cell.equals(current) ? "o" :
                            (cell.isVisited() ? "." : " "));
         }
         System.out.println();
      }
      System.out.println();
   }
}



