package com.codecool.dungeoncrawl.mapgenerator;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.HpPotion;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.Stairs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {

    public static GameMap loadMap(String database) {
        int index = database.indexOf(" ");
        String sub = database.substring(0, index);
        int width = Integer.parseInt(sub);

        int previous = index + 1;
        index = database.indexOf("\n");
        sub = database.substring(previous, index);
        int height = Integer.parseInt(sub);

        GameMap map = new GameMap(width, height, CellType.EMPTY);

        List<String> data = new ArrayList<>();
        previous = ++index;
        while(index < database.length()) {
            index++;
            if (database.charAt(index) == '\n') {
                data.add(database.substring(previous, index));
                previous = ++index;
            }
        }
        return readAllData(map, data);
    }

    public static Inventory loadInventoryFromString(String database) {
        int index = database.indexOf(" ");
        String sub = database.substring(0, index);
        int width = Integer.parseInt(sub);

        int previous = index + 1;
        index = database.indexOf("\n");
        sub = database.substring(previous, index);
        int height = Integer.parseInt(sub);

        Inventory inventory = new Inventory(width, height, CellType.EMPTY);

        List<String> data = new ArrayList<>();
        previous = ++index;
        while(index < database.length()) {
            index++;
            if (database.charAt(index) == '\n') {
                data.add(database.substring(previous, index));
                previous = ++index;
            }
        }
        GameMap gameMap = readAllData(inventory, data);
        return (Inventory) gameMap;
    }

    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);

        int width = scanner.nextInt();
        int height = scanner.nextInt();
        GameMap map = new GameMap(width, height, CellType.EMPTY);

        scanner.nextLine(); // step to next line
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }
        return readAllData(map, data);

    }

    public static GameMap readAllData(GameMap map, List<String> data) {
        int width = map.getWidth();
        int height = map.getHeight();
        for (int y = 0; y < height; y++) {
            String line = data.get(y);
            int shift = 0;  // because the current mapline might contains ID code(s)!
            for (int x = 0; x < width; x++) {
                if (x + shift < line.length()) {
                    Cell cell = map.getCell(x, y);

                    switch (line.charAt(x + shift)) {
                        case 'V':
                            cell.setCellType(CellType.OVEN);
                            break;
                        case 'B':
                            cell.setCellType(CellType.BOOKSHELF);
                            break;
                        case 'W':
                            cell.setCellType(CellType.HOUSEWALL);
                            break;
                        case 'F':
                            cell.setCellType(CellType.FENCE);
                            break;
                        case 'X':
                            cell.setCellType(CellType.SPIDERNET);
                            break;
                        case 'r':
                            cell.setCellType(CellType.ROCK);
                            break;
                        case 'T':
                            cell.setCellType(CellType.TORCH);
                            break;
                        case 'j':
                            cell.setCellType(CellType.GRASS);
                            break;
                        case 'i':
                            cell.setCellType(CellType.TREE);
                            break;
                        case 'I':
                            cell.setCellType(CellType.DOG);
                            break;
                        case 'o':
                            cell.setCellType(CellType.FOREST);
                            break;
                        case ' ':
                            cell.setCellType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setCellType(CellType.WALL);
                            break;
                        case '.':
                            cell.setCellType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setCellType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case '@':
                            cell.setCellType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'S':
                            cell.setCellType(CellType.FLOOR);
                            new Spider(cell);
                            break;
                        case 'g':
                            cell.setCellType(CellType.GRASS);
                            new Goblin(cell);
                            break;
                        case 'G':
                            cell.setCellType(CellType.GRASS);
                            new Ghost(cell);
                            break;
                        case 'h':
                            cell.setCellType(CellType.FLOOR);
                            new HpPotion(cell);
                            break;
                        case 'k':
                            cell.setCellType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case 'w':
                            cell.setCellType(CellType.FLOOR);
                            new Weapon(cell);
                            break;
                        case 'C':
                            cell.setCellType(CellType.CLOSED_DOOR);
                            break;
                        case 'O':
                            cell.setCellType(CellType.OPENED_DOOR);
                            break;
                        case 'U':
                            cell.setCellType(CellType.UPPER_STAIRS);
                            cell.setStairs(setNewStairs(line, x + shift, y, "up"));
                            shift = shift + cell.getStairs().getID().length();
                            break;
                        case 'D':
                            cell.setCellType(CellType.DOWN_STAIRS);
                            cell.setStairs(setNewStairs(line, x + shift, y, "down"));
                            shift = shift + cell.getStairs().getID().length();
                            break;

                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
       // System.out.println(".txt file content OK!)");
        return map;
    }

    public static Inventory loadInventory(){
        InputStream is = MapLoader.class.getResourceAsStream("/inventory.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        Inventory map = new Inventory(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setCellType(CellType.EMPTY);
                            break;
                        case '.':
                            cell.setCellType(CellType.FLOOR);
                            break;
                    }
                }
            }
        }

        return map;
    }

    public static Stairs setNewStairs(String line, int x, int y, String direction) {
        new StairsBuilder()
                .withLocX(x)
                .withLocY(y)
                .withDirection(direction)
                .withID(readID(line, x ));
        Stairs stairs = StairsBuilder.build();
        /*System.out.println("New stairs found with ID: " + stairs.getID() + ", X: " + stairs.getMoveToX() + ", Y: " +
                    stairs.getMoveToY() + ", direction: " + stairs.getDirection());*/
        return stairs;
    }

    public static String readID(String str, int index) {
        StringBuilder id = new StringBuilder();
        do {
            id.append(str.charAt(++index));
        } while (str.charAt(index) != '}');
        return id.toString();
    }

}
