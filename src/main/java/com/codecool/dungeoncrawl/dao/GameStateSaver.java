package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.HpPotion;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.Stairs;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameStateSaver {

   public static String saveToFile(GameMap map, String filename) {
      String convertedMapData = convertMapToJSONformat(map);
      return writeToFile(convertedMapData, filename);
   }

   public static String convertMapToString(GameMap map) {
      StringBuilder result = new StringBuilder();
      result.append(map.getWidth() + " ");
      result.append(map.getHeight()+ "\n");
      for (int y = 0; y < map.getHeight(); y++) {
         for (int x = 0; x < map.getWidth(); x++) {
            Cell cell = map.getCell(x, y);
            String element = "";
            switch (cell.getCellType()) {
               case OVEN:
                  element = "V"; break;
               case BOOKSHELF:
                  element = "B"; break;
               case HOUSEWALL:
                  element = "W"; break;
               case FENCE:
                  element = "F"; break;
               case SPIDERNET:
                  element = "X"; break;
               case ROCK:
                  element = "r"; break;
               case TORCH:
                  element = "T"; break;
               case GRASS:
                  element = "j"; break;
               case TREE:
                  element = "i"; break;
               case DOG:
                  element = "I"; break;
               case FOREST:
                  element = "o"; break;
               case EMPTY:
                  element = " "; break;
               case WALL:
                  element = "#"; break;
               case FLOOR:
                  element = "."; break;
               case CLOSED_DOOR:
                  element = "C"; break;
               case OPENED_DOOR:
                  element = "O"; break;
               case UPPER_STAIRS:
                  element = "U" + cell.getStairs().getID();
                  break;
               case DOWN_STAIRS:
                  element = "D" + cell.getStairs().getID();
                  break;
               default:
                  element = " ";
                  break;
            }
            if(cell.getActor() != null) {
               Actor actor = cell.getActor();
               if (actor.getClass().equals(Player.class)) {
                  element = "@";
               }
               else if (actor.getClass().equals(Skeleton.class)) {
                  element = "s";
               }
               else if (actor.getClass().equals(Goblin.class)) {
                  element = "g";
               }
               else if (actor.getClass().equals(Ghost.class)) {
                  element = "G";
               }
               else if (actor.getClass().equals(Spider.class)) {
                  element = "S";
               }
            }
            else if (cell.getItem() != null) {
               Item item = cell.getItem();
               if (item.getClass().equals(HpPotion.class)) {
                  element = "h";
               }
               else if (item.getClass().equals(Key.class)) {
                  element = "k";
               }
               else if (item.getClass().equals(Weapon.class)) {
                  element = "w";
               }
            }
            result.append(element);
         }
         result.append("\n");
      }
      return result.toString();
   }

   public static String writeToFile(String gamestate, String filename) {
      String fileName = "./"+ filename + ".save";

      Path file = Path.of(fileName);
      try (BufferedWriter writer = Files.newBufferedWriter(file)) {
         writer.write(gamestate + "\n");
      } catch (IOException ioe) {
         System.out.println("Can not open the file! " + filename);
         return "";
      }
      return fileName;
   }


   private static String convertMapToJSONformat(GameMap map) {
      StringBuilder result = new StringBuilder();
      String[] space = {"  ", "    ", "      ", "        ", "          "};
      int i = 0;

      result.append("{\n");
      result.append(space[i] + "\"width\": " + map.getWidth() + ",\n");
      result.append(space[i] + "\"height\": " + map.getHeight() + ",\n");
      result.append(space[i] + "\"cells\": [\n");
      i++;
      for (int y = 0; y < map.getHeight(); y++) {
         for (int x = 0; x < map.getWidth(); x++) {
            Cell cell = map.getCell(x, y);
            result.append(space[i] + "{\n");
            i++;
            result.append(space[i] + "\"x\": " + cell.getX() + ",\n");
            result.append(space[i] + "\"y\": " + cell.getY() + ",\n");
            result.append(space[i] + "\"cellType\": \"" + cell.getCellType().getTileName() + "\",\n");

            if (cell.getItem() == null) {
               result.append(space[i] + "\"item\": \"null\",\n");
            } else {
               result.append(space[i] + "\"item\": \"" + cell.getItem().getName() + "\",\n");
            }

            if (cell.getActor() == null) {
               result.append(space[i] + "\"actor\": \"null\",\n");
            } else {
               result.append(space[i] + "\"actor\":\n");
               Actor actor = cell.getActor();
               i++;
               result.append(space[i] + "{\n");
               i++;
               result.append(space[i] + "\"name\": \"" + actor.getName() + "\",\n");
               result.append(space[i] + "\"health\": " + actor.getHealth() + ",\n");
               result.append(space[i] + "\"strength\": " + actor.getStrength() + "\n");
               i--;
               result.append(space[i] + "}\n");
               i--;
            }

            if (cell.getStairs() == null) {
               result.append(space[i] + "\"stairs\": \"null\"\n");
            } else {
               result.append(space[i] + "\"stairs\":\n");
               Stairs stairs = cell.getStairs();
               i++;
               result.append(space[i] + "{\n");
               i++;
               result.append(space[i] + "\"moveToX\": " + stairs.getMoveToX() + ",\n");
               result.append(space[i] + "\"moveToY\": " + stairs.getMoveToY() + ",\n");
               result.append(space[i] + "\"ID\": \"" + stairs.getID() + "\",\n");
               result.append(space[i] + "\"direction\": \"" + stairs.getDirection() + "\"\n");
               i--;
               result.append(space[i] + "}\n");
               i--;
            }
            i--;
            if (y == map.getHeight() - 1 && x == map.getWidth() - 1) {
               result.append(space[i] + "}\n");
            } else {
               result.append(space[i] + "},\n");
            }
         }
      }
      i--;
      result.append(space[i] + "]\n");
      result.append("}\n");

      return result.toString();
   }

}
