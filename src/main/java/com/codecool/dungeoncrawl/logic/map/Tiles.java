package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();


    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("border", new Tile(1, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("Player", new Tile(27, 0));
        tileMap.put("Skeleton", new Tile(29, 6));
        tileMap.put("Spider",new Tile(28,5));
        tileMap.put("Goblin",new Tile(25,2));
        tileMap.put("Ghost",new Tile(27,6));
        tileMap.put("hppotion",new Tile(23,22));
        tileMap.put("key",new Tile(16,23));
        tileMap.put("closed_door",new Tile(11,11));
        tileMap.put("opened_door",new Tile(12,11));
        tileMap.put("upper_stairs",new Tile(2,6));
        tileMap.put("down_stairs",new Tile(3,6));
        tileMap.put("grass",new Tile(6,0));
        tileMap.put("tree",new Tile(0,1));
        tileMap.put("forest",new Tile(3,2));
        tileMap.put("rock",new Tile(5,2));
        tileMap.put("spidernet",new Tile(2,15));
        tileMap.put("torch",new Tile(3,15));
        tileMap.put("fence",new Tile(1,3));
        tileMap.put("housewall",new Tile(10,17));
        tileMap.put("bookshelf",new Tile(3,7));
        tileMap.put("oven",new Tile(11,8));
        tileMap.put("dog",new Tile(31,7));
        tileMap.put("weapon",new Tile(1,30));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }

    public static void drawTile(GraphicsContext context, CellType type, int x, int y) {
        Tile tile = tileMap.get(type.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
