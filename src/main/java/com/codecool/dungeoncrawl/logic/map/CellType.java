package com.codecool.dungeoncrawl.logic.map;

import java.util.Arrays;
import java.util.List;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    CLOSED_DOOR("closed_door"),
    OPENED_DOOR("opened_door"),
    UPPER_STAIRS("upper_stairs"),
    DOWN_STAIRS("down_stairs"),
    FOREST("forest"),
    TREE("tree"),
    GRASS("grass"),
    TORCH("torch"),
    ROCK("rock"),
    SPIDERNET("spidernet"),
    FENCE("fence"),
    HOUSEWALL("housewall"),
    BOOKSHELF("bookshelf"),
    OVEN("oven"),
    DOG("dog");

    private final String tileName;
    private static final List<CellType> listOfBlockedCellTypes =
            Arrays.asList(WALL, CLOSED_DOOR, TREE, FENCE, HOUSEWALL, BOOKSHELF, FOREST);


    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }

    public static List<CellType> getListOfBlockedCellTypes() {
        return listOfBlockedCellTypes;
    }
}
