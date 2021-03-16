package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Cell implements Drawable {

    private int x, y;
    private CellType cellType;
    private Item item;
    private Actor actor;
    private Stairs stairs = null;

    public Cell(int x, int y, CellType type) {
        this.x = x;
        this.y = y;
        this.cellType = type;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType type) {
        this.cellType = type;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public Cell getNeighbor(int dx, int dy, GameMap map) {
        return map.getCell(x + dx, y + dy);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setStairs(Stairs stairs) {
        this.stairs = stairs;
    }

    public Stairs getStairs() {
        return stairs;
    }

    @Override
    public String getTileName() {
        return cellType.getTileName();
    }

}
