package com.codecool.dungeoncrawl.mapgenerator;

import com.codecool.dungeoncrawl.logic.map.Stairs;

public class StairsBuilder {

   private static int locX;
   private static int locY;
   private static String ID;
   private static String direction;


   public StairsBuilder withLocX(int locationX) {
      StairsBuilder.locX = locationX;
      return this;
   }

   public StairsBuilder withLocY(int locationY) {
      StairsBuilder.locY = locationY;
      return this;
   }

   public StairsBuilder withID(String ID) {
      StairsBuilder.ID = ID;
      return this;
   }

   public StairsBuilder withDirection(String direction) {
      StairsBuilder.direction = direction;
      return this;
   }

   public static Stairs build() {
      return new Stairs(locX, locY, ID, direction);
   }
}
