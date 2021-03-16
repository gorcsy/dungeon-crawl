package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super("Skeleton", cell);
        super.setHealth(13);
        super.setStrength(3);
        cell.setActor(this);
    }

    public void enemyMove(GameMap map) {
        int dx = 1 - (int) (Math.random()*3.0);
        int dy = 1 - (int) (Math.random()*3.0);
        super.move(dx, dy, map);
    }
}
