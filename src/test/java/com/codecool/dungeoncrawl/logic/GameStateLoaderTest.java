package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.dao.GameStateLoader;
import org.junit.jupiter.api.Test;

public class GameStateLoaderTest {

   @Test
   void covertJSONFile() {
      GameStateLoader.readFromFile("./teszt.save");
   }
}
