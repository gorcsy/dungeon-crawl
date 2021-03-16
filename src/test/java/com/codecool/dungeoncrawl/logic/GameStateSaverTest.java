package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.dao.GameStateSaver;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.mapgenerator.MapLoader;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static com.codecool.dungeoncrawl.dao.GameStateSaver.convertMapToString;

public class GameStateSaverTest {


   @Test
   void testJSONFile() {
      GameMap map = MapLoader.loadMap();
      String filename = GameStateSaver.saveToFile(map, "teszt");
      System.out.println(filename);
      if (filename != "") {
         BufferedReader br;
         try {
            br = new BufferedReader(new FileReader(filename));
            Scanner scanner = new Scanner(br);
            String str = "";

            while(scanner.hasNextLine()) {
               str = str + scanner.nextLine() + "\n";
            }
            System.out.println(str);

         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
      }
   }

   @Test
   void testConvertMapToString() {
      GameMap map = MapLoader.loadMap();
      String convertedMap = convertMapToString(map);
      map = MapLoader.loadMap(convertedMap);
      System.out.println(convertedMap);
   }


}
