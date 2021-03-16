package com.codecool.dungeoncrawl.logic.items;


import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.Drawable;

public class Weapon extends Item implements Drawable {

    private final String name = "weapon";

    public Weapon(Cell cell) {
        super(cell);
        this.setName(name);
    }

    @Override
    public String getTileName() {
        return name;
    }

}
