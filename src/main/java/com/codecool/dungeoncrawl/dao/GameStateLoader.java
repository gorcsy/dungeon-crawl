package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.HpPotion;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.Stairs;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class GameStateLoader {

   public static GameMap readFromFile(String filename) {

      System.out.println(filename);
      GameMap map = new GameMap(1,1, CellType.EMPTY);
      if (filename != "") {
         BufferedReader br;
         try {
            br = new BufferedReader(new FileReader(filename));
            Scanner scanner = new Scanner(br);
            scanner.nextLine();

            int width = Integer.parseInt(getValueFromLine(scanner.nextLine()));
            int height = Integer.parseInt(getValueFromLine(scanner.nextLine()));
            map = new GameMap(width, height, CellType.EMPTY);

            scanner.nextLine();
            while(!(scanner.nextLine().trim().contains("]"))) {
               int x = Integer.parseInt(getValueFromLine(scanner.nextLine()));
               int y = Integer.parseInt(getValueFromLine(scanner.nextLine()));
               Cell cell = map.getCell(x, y);

               String cellType = getValueFromLine(scanner.nextLine());
               cell.setCellType(CellType.valueOf(cellType.toUpperCase()));

               String item = getValueFromLine(scanner.nextLine());
               switch (item) {
                  case "null": {
                     cell.setItem(null);
                     break;
                  }
                  case "hppotion": {
                     cell.setItem(new HpPotion(cell));
                     break;
                  }
                  case "key": {
                     cell.setItem(new Key(cell));
                     break;
                  }
                  case "weapon": {
                     cell.setItem(new Weapon(cell));
                     break;
                  }
               }

               String actor = getValueFromLine(scanner.nextLine());
               if (!actor.equals("null")) {
                  scanner.nextLine();
                  String actorName = getValueFromLine(scanner.nextLine());
                  int health = Integer.parseInt(getValueFromLine(scanner.nextLine()));
                  int strength = Integer.parseInt(getValueFromLine(scanner.nextLine()));
                  scanner.nextLine();
                  switch (actorName) {
                     case "Player": {
                        cell.setActor(new Player(cell));
                        cell.getActor().setHealth(health);
                        cell.getActor().setStrength(strength);
                        break;
                     }
                     case "Ghost": {
                        cell.setActor(new Ghost(cell));
                        cell.getActor().setHealth(health);
                        cell.getActor().setStrength(strength);
                        break;
                     }
                     case "Goblin": {
                        cell.setActor(new Goblin(cell));
                        cell.getActor().setHealth(health);
                        cell.getActor().setStrength(strength);
                        break;
                     }
                     case "Skeleton": {
                        cell.setActor(new Skeleton(cell));
                        cell.getActor().setHealth(health);
                        cell.getActor().setStrength(strength);
                        break;
                     }
                     case "Spider": {
                        cell.setActor(new Spider(cell));
                        cell.getActor().setHealth(health);
                        cell.getActor().setStrength(strength);
                        break;
                     }
                  }
               }
               else cell.setActor(null);

               String stairs = getValueFromLine(scanner.nextLine());
               if (!stairs.equals("null")) {
                  scanner.nextLine();
                  int moveToX = Integer.parseInt(getValueFromLine(scanner.nextLine()));
                  int moveToY = Integer.parseInt(getValueFromLine(scanner.nextLine()));
                  String ID = getValueFromLine(scanner.nextLine());
                  String direction = getValueFromLine(scanner.nextLine());
                  scanner.nextLine();
                  cell.setStairs(new Stairs(moveToX, moveToY, ID, direction));
               }
               scanner.nextLine();
            }

         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
      }
      return map;
   }

   private static String getValueFromLine(String line) {
      int index = 0;
      String value = "";
      while (line.charAt(index) != ':') {
         index++;
      }
      value = line.substring(index + 1);
      value = value.trim();
      value = value.replaceAll("[,\"]", "");
      return value;
   }
}
