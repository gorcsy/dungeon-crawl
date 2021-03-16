package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;

public class Ghost extends Actor{

    public Ghost(Cell cell) {
        super("Ghost", cell);
        super.setHealth(10);
        super.setStrength(2);
        cell.setActor(this);
    }

    public void enemyMove(GameMap map) {
        int dx = 1 - (int) (Math.random()*3.0);
        int dy = 1 - (int) (Math.random()*3.0);
        super.move(dx, dy, map);
    }

}
