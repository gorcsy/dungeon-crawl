package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.Drawable;

public class Key extends Item implements Drawable {

    private final String name = "key";

    public Key(Cell cell) {
        super(cell);
        this.setName(name);
    }


    @Override
    public String getTileName() {
        return name;
    }

}



